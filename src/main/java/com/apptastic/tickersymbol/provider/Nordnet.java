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
import com.google.gson.stream.JsonReader;

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
 * Ticker provider implementation that fetches ticker information from Nordnet.
 * Nordnet is a swedish internet broker.
 */
public class Nordnet extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL_BASE = "https://www.nordnet.se";
    private static final String URL_SUGGESTION = "https://www.nordnet.se/search/suggest.html";
    private static final String HTTP_POST_BODY = "q=%1$s";


    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    @Override
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String postBody =  String.format(HTTP_POST_BODY, isin);

        try (BufferedReader reader = sendRequest(URL_SUGGESTION, postBody.getBytes(), "UTF-8")) {

            return parseSuggestionResponse(reader, isin)
                    .map(this::getTickerSymbol)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }


    private Stream<Suggestion> parseSuggestionResponse(BufferedReader reader, String isin) throws IOException {
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.beginObject();
        List<Suggestion> suggestions = new ArrayList<>();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if (name.equals("instruments")) {
                jsonReader.beginArray();

                while (jsonReader.hasNext()) {
                    Suggestion suggestion = parseSuggestions(jsonReader, isin);
                    suggestions.add(suggestion);
                }

                jsonReader.endArray();
            }
            else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();
        return suggestions.stream().parallel();
    }


    private Suggestion parseSuggestions(JsonReader jsonReader, String isin) throws IOException {
        Suggestion suggestion = null;
        jsonReader.beginObject();

        if (jsonReader.hasNext()) {
            suggestion = new Suggestion();
            suggestion.getTickerSymbol().setIsin(isin);
        }

        while (suggestion != null && jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if (name.equals("name")) {
                String symbolName = formatName(jsonReader.nextString());
                suggestion.getTickerSymbol().setName(symbolName);
            }
            else if (name.equals("currency")) {
                suggestion.getTickerSymbol().setCurrency(jsonReader.nextString());
            }
            else if (name.equals("identifier")) {
                suggestion.getTickerSymbol().setSymbol(jsonReader.nextString());
            }
            else if (name.equals("url")) {
                String url = jsonReader.nextString();
                url = URL_BASE + url.replace("index", "bolagsfakta").trim();
                suggestion.setUrl(url);
            }
            else if (name.equals("market_name")) {
                suggestion.getTickerSymbol().setDescription(jsonReader.nextString());
            }
            else if (name.equals("instrument_group_type_name")) {
                suggestion.getTickerSymbol().setDescription(jsonReader.nextString());
            }
            else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();
        return suggestion;
    }


    private TickerSymbol getTickerSymbol(Suggestion suggestion) {
        TickerSymbol ticker = null;

        try (BufferedReader reader = sendRequest(suggestion.getUrl(), "ISO-8859-1")) {
            ticker = parsePageResponse(reader, suggestion.getTickerSymbol());
        }
        catch (IOException e) {
            Logger logger = Logger.getLogger("com.apptastic.tickersymbol");

            if (logger.isLoggable(Level.WARNING))
                logger.log(Level.WARNING, "Failed to get ticker symbol", e);
        }

        return ticker;
    }


    private TickerSymbol parsePageResponse(BufferedReader reader, TickerSymbol ticker) throws IOException {
        String line = "";

        while (line != null && !line.contains("Företagsinformation"))
            line = reader.readLine();

        if (line == null)
            return null;

        while (line != null) {

            if (!line.contains("class=\"betona\"")) {
                line = reader.readLine();
                continue;
            }

            if (line.contains("Handelsplats")) {
                String market = getValue(reader);
                String mic = market2Mic(market);
                ticker.setMic(mic);
            }
            else if (line.contains("Valuta")) {
                String currency = getValue(reader).trim();

                if (!currency.isEmpty())
                    ticker.setCurrency(currency);
            }
            else if (line.contains("ISIN")) {
                List<String> values = getValues(reader);

                if (values.size() == 3) {
                    if (!values.get(0).isEmpty())
                        ticker.setSymbol(values.get(0));

                    if (!values.get(2).isEmpty())
                        ticker.setIsin(values.get(2));
                }
            }
            else if (line.contains("Namn")) {
                String name = formatName(getValue(reader));

                if (!name.isEmpty())
                    ticker.setName(name);

                if (isTickerSymbolValid(ticker))
                    break;
            }

            line = reader.readLine();
        }

        return ticker;
    }

    private String getValue(BufferedReader reader) throws IOException {
        String value = null;
        String line = reader.readLine();

        if (line == null)
            return "";

        int start = line.indexOf(">");
        int end = line.indexOf("<", start);

        if (start != -1 && end != -1 && end >= start)
            value = line.substring(start + 1, end).trim();

        return value;
    }

    private List<String> getValues(BufferedReader reader) throws IOException {
        List<String> values = new ArrayList<>();
        String line = reader.readLine();

        if (line == null)
            return values;

        int start = line.indexOf(">", 0);

        while (start != -1) {
            int end = line.indexOf("<", start);

            if (end != -1 && end == start + 1)
                values.add("");
            else if (end != -1 && end > start + 1)
                values.add(line.substring(start + 1, end).trim());

            start = line.indexOf(">", start + 1);
        }

        return values;
    }


    private String market2Mic(String text) {
        String mic = "";
        text = text.toLowerCase();

        if (text.contains("nasdaq stockholm"))
            mic = "XSTO";
        else if (text.contains("spotlight"))
            mic = "XSAT";
        else if (text.contains("ngm"))
            mic = "XNGM";
        else if (text.contains("nasdaq copenhagen"))
            mic = "XCSE";
        else if (text.contains("nasdaq helsinki"))
            mic = "XHEL";
        else if (text.contains("ose"))
            mic = "XOSL";
        else if (text.equals("nasdaq") || text.equals("otc foreign"))
            mic = "XNYS";
        else if (text.contains("toronto stock exchange"))
            mic = "XTSE";
        else if (text.contains("xetra"))
            mic = "XETR";

        return mic;
    }


    private class Suggestion {
        private String url;
        private TickerSymbol tickerSymbol;

        public Suggestion() {
            url = "";
            tickerSymbol = new TickerSymbol();
            tickerSymbol.setSource(Source.NORDNET);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public TickerSymbol getTickerSymbol() {
            return tickerSymbol;
        }

    }


    private String formatName(String name) {
        if (name == null)
            return "";

        int length = name.length();
        int end = name.lastIndexOf('.');

        if (length > 1 && end != -1)
            name = name.substring(0, end);

        return name;
    }
}
