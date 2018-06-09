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

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Ticker provider implementation that fetches ticker information from Morning Star.
 * Morning Start is a investment research firm that compiles and analyzes fund, stock and general market data.
 */
public class MorningStar extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL = "http://www.morningstar.com/api/v2/search/securities/5/usquote-v2/?q=%1$s";


    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String url = String.format(URL, isin);

        BufferedReader reader = sendRequest(url, "UTF-8");
        JsonReader jsonReader = new JsonReader(reader);

        return handleResponse(jsonReader);
    }


    private List<TickerSymbol> handleResponse(JsonReader reader) throws IOException {
        List<TickerSymbol> tickers = new ArrayList<>();
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if ("m".equals(name)) {
                JsonUtil.optBeginArray(reader);
                reader.beginObject();

                while (reader.hasNext()) {
                    String fieldName = reader.nextName();

                    if ("r".equals(fieldName))
                        parseTickers(reader, tickers);
                    else
                        reader.skipValue();
                }

                reader.endObject();
                JsonUtil.optEndArray(reader);
            }
            else {
                reader.skipValue();
            }
        }

        reader.endObject();
        return tickers;
    }


    @Override
    protected void parseTicker(JsonReader reader, TickerSymbol ticker) throws IOException {
        String name = reader.nextName();

        if ("OS001".equals(name)) {
            ticker.setSymbol(reader.nextString());
            ticker.setSource(Source.MORNING_STAR);
        }
        else if ("OS01W".equals(name))
            ticker.setName(reader.nextString());
        else if ("OS05J".equals(name))
            ticker.setIsin(reader.nextString());
        else if ("OS05M".equals(name))
            ticker.setCurrency(reader.nextString());
        else if ("LS01Z".equals(name))
            ticker.setMic(reader.nextString());
        else if ("OS01X".equals(name))
            ticker.setDescription(reader.nextString());
        else
            reader.skipValue();
    }

}

