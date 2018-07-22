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
/*
package com.apptastic.tickersymbol.provider;


import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



// Ticker provider implementation that fetches ticker information from Spotlight Stock Market (formerly known as Aktie Torget).
//Spotlight Stock Market is a small swedish market place.
public class SpotlightStockMarket extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL = "https://www.spotlightstockmarket.com/sv/bolag/irtrade?InstrumentId=%1$s";


//     **
//     * Search ticker by name.
//     * @param name name.
//     * @return stream of tickers
//     *
    @Override
    public List<TickerSymbol> searchByName(String name) {
        return Collections.emptyList();
    }


//     **
//     * Search ticker by ISIN code.
//     * @param isin ISIN code.
//     * @return stream of tickers
//     * @throws IOException IO exception
//     *
    @Override
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String url = String.format(URL, isin);

        try (BufferedReader reader = sendRequest(url, "UTF-8")) {

            TickerSymbol ticker = handleResponse(reader);

            if (ticker == null)
                return Collections.emptyList();

            return Arrays.asList(ticker);
        }
    }


    private TickerSymbol handleResponse(BufferedReader reader) throws IOException {
        TickerSymbol ticker = new TickerSymbol();
        ticker.setMic("XSAT");
        ticker.setSource(Source.SPOTLIGHT_STOCK_MARKET);

        String line = reader.readLine();

        while (line != null) {
            if (line.contains("component__list-item__title")) {
                parseFieldValue(line, ticker, reader);

                if (isTickerSymbolValid(ticker))
                    break;
            }

            line = reader.readLine();
        }

        if (!isTickerSymbolValid(ticker))
            ticker = null;

        return ticker;
    }


    private void parseFieldValue(String line, TickerSymbol ticker, BufferedReader reader) throws IOException {
        if (line.contains("Aktienamn") || line.contains("Share name")) {
            String shareName = getValue(reader);
            ticker.setName(shareName);
        }
        else if (line.contains("Kortnamn") || line.contains("Short name")) {
            String shortName = getValue(reader);
            ticker.setSymbol(shortName);
        }
        else if (line.contains("ISIN-Kod") || line.contains("ISIN Code")) {
            String isinCode = getValue(reader);
            ticker.setIsin(isinCode);
        }
        else if (line.contains("Tillgångsklass") || line.contains("Type")) {
            String type = getValue(reader);
            ticker.setDescription(type);
        }
        else if (line.contains("Valuta") || line.contains("Currency")) {
            String currency = getValue(reader);
            ticker.setCurrency(currency);
        }
    }


    private String getValue(BufferedReader reader) throws IOException {
        String skip = reader.readLine();
        String line = reader.readLine();

        if (skip == null || line == null)
            return null;

        int start = line.indexOf('>');
        int end = line.lastIndexOf('<');

        if (start == -1 || end == -1 || start + 1 > end)
            return null;

        return line.substring(start + 1, end).trim();
    }


    @Override
    protected boolean isTickerSymbolValid(TickerSymbol ticker) {
        return super.isTickerSymbolValid(ticker) && ticker.getDescription() != null;
    }

}
*/
