/*
 * MIT License
 *
 * Copyright (c) 2018, Apptastic Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.apptastic.tickersymbol.provider;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Ticker provider implementation that fetches ticker information from Avanza.
 * Avanza is a swedish internet broker.
 */
public class Avanza extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL_BASE = "https://www.avanza.se";
    private static final String URL_SUGGESTION = "https://www.avanza.se/ab/sok/inline?query=%1$s&_=%2$s";


    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    @Override
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String url = String.format(URL_SUGGESTION, isin, Long.toString(System.currentTimeMillis()/1000));

        try (BufferedReader reader = sendRequest(url,"UTF-8")) {

            return parseSuggestionResponse(reader)
                    .map(this::getTickerSymbol)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }


    private Stream<String> parseSuggestionResponse(BufferedReader reader) throws IOException {
        String line = reader.readLine();

        if (line.toLowerCase().contains("du fick inga träffar")) {
            reader.close();
            return Stream.empty();
        }

        List<String> results = new ArrayList<>();
        line = reader.readLine();

        while (line != null) {
            if (line.contains("srchResLink leftSpace")) {
                String link = getLink(line);

                if (link != null)
                    results.add(URL_BASE + link.trim());
            }

            line = reader.readLine();
        }

        return results.stream().parallel();
    }


    private String getLink(String line) {
        int index = line.indexOf(" href=");

        if (index == -1)
            return null;

        int start = line.indexOf('"', index + 6);
        int end = line.indexOf('"', start + 1);

        if (start == -1 || end == -1 || start + 1 > end)
            return null;

        return line.substring(start + 1, end).trim();
    }


    private TickerSymbol getTickerSymbol(String url) {
        TickerSymbol ticker = null;

        try (BufferedReader reader = sendRequest(url, "UTF-8")) {
            ticker = parsePageResponse(reader);
        }
        catch (IOException e) {
            Logger logger = Logger.getLogger("com.apptastic.tickersymbol");

            if (logger.isLoggable(Level.WARNING))
                logger.log(Level.WARNING, "Failed to get ticker symbol", e);
        }

        if (!isTickerSymbolValid(ticker))
            ticker = null;

        return ticker;
    }


    private TickerSymbol parsePageResponse(BufferedReader reader) throws IOException {
        if (reader == null)
            return null;

        TickerSymbol ticker = getTickerNameOnPage(reader);
        String line = "";

        while (line != null && !line.contains("Kortnamn"))
            line = reader.readLine();

        if (ticker == null || line == null)
            return null;

        parseTickerSymbol(reader, ticker, line);

        return ticker;
    }


    private void parseTickerSymbol(BufferedReader reader, TickerSymbol ticker, String line) throws IOException {

        while (line != null) {

            if (!line.contains("<dt><span>"))
                line = reader.readLine();

            if (line.contains("Kortnamn"))
                parseKortnamn(reader, ticker);
            else if (line.contains("ISIN"))
                parseIsin(reader, ticker);
            else if (line.contains("Marknad"))
                parseMarknad(reader, ticker);
            else if (line.contains("Handlas i"))
                parseHandlasI(reader, ticker);

            if (isTickerSymbolValid(ticker))
                break;

            line = reader.readLine();
        }
    }


    private void parseKortnamn(BufferedReader reader, TickerSymbol ticker) throws IOException {
        String symbol = getValue(reader);

        if (symbol != null)
            symbol = symbol.toUpperCase();

        ticker.setSymbol(symbol);
    }


    private void parseIsin(BufferedReader reader, TickerSymbol ticker) throws IOException {
        String isin = getValue(reader);
        ticker.setIsin(isin);
    }


    private void parseMarknad(BufferedReader reader, TickerSymbol ticker) throws IOException {
        String market = getValue(reader);
        String mic = market2Mic(market);
        ticker.setMic(mic);
        ticker.setDescription(market);
    }


    private void parseHandlasI(BufferedReader reader, TickerSymbol ticker) throws IOException {
        String currency = getValue(reader);
        ticker.setCurrency(currency);
    }


    private TickerSymbol getTickerNameOnPage(BufferedReader reader) throws IOException {
        TickerSymbol ticker = null;
        String line = "";

        while (line != null && !line.contains("data-intrument_name"))
            line = reader.readLine();

        if (line != null) {
            int start = line.indexOf("data-intrument_name");
            int end = line.indexOf('"', start + 21);

            if (start != -1 && end != -1 && end >= start + 21) {
                String name = line.substring(start + 21, end);

                ticker = new TickerSymbol();
                ticker.setSource(Source.AVANZA);
                ticker.setName(name.trim());
            }
        }

        return ticker;
    }


    private String getValue(BufferedReader reader) throws IOException {
        String value = "";
        String line = reader.readLine();

        if (line == null)
            return "";

        int start = line.indexOf("<dd><span>");
        int end = line.indexOf("</span></dd>", start);

        if (start != -1 && end != -1 && end >= start)
            value = line.substring(start + 10, end).trim();
        else if (start != -1)
            value = getMultilineValue(reader, line, start, end);

        return value;
    }


    private String getMultilineValue(BufferedReader reader, String line, int start, int end) throws IOException {
        StringBuilder valueBuilder = new StringBuilder();
        line = line.substring(start + 10).trim();

        while (line != null && end == -1) {
            end = line.lastIndexOf("</span></dd>", start);

            if (end != -1)
                line = line.substring(0, end);

            line = line.trim();

            if (!line.isEmpty())
                valueBuilder.append(line);

            if (end != -1)
                break;

            line = reader.readLine();
            line = removeBrTag(line);
        }

        return valueBuilder.toString().trim();
    }


    private String removeBrTag(String text) {
        if (text != null && text.contains("<br")) {
            int brStart = text.indexOf("<br");
            int brEnd = text.indexOf("/>", brStart);
            String part1 = text.substring(0, brStart);
            String part2 = text.substring(brEnd + 2);
            text = part1 + part2;
        }

        return text;
    }


    private String market2Mic(String text) {
        String mic = "";
        text = text.toLowerCase();

        if (text.contains("stockholmsbörsen") || text.contains("cap stockholm") || text.contains("first north stockholm"))
            mic = "XSTO";
        else if (text.contains("ngm"))
            mic = "XNGM";
        else if (text.contains("aktietorget"))
            mic = "XSAT";
        else if (text.contains("köpenhamnsbörsen") || text.contains("cap copenhagen"))
            mic = "XCSE";
        else if (text.contains("helsingforsbörsen") || text.contains("cap helsinki"))
            mic = "XHEL";
        else if (text.contains("oslobörsen"))
            mic = "XOSL";
        else if (text.contains("nasdaq"))
            mic = "XNYS";
        else if (text.contains("toronto stock exchange"))
            mic = "XTSE";
        else if (text.contains("equiduct"))
            mic = "XBER";

        return mic;
    }

}

