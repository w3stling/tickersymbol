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
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Ticker provider implementation that fetches ticker information from Nasdaq OMX Nordic.
 * Nasdaq OMX Nordic is a stock exchange for swedish stocks and nordic stocks.
 */
public class NasdaqOmxNordic extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx";
    private static final String HTTP_POST_BODY = "<post>\n" +
            "<param name=\"SubSystem\" value=\"Prices\"/>\n" +
            "<param name=\"Action\" value=\"Search\"/>\n" +
            "<param name=\"inst.an\" value=\"nm,fnm,isin,tp,cr,chp,tb,mkt,st,isrid,ec,isr\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR-CCP\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR-IC\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XCSE:DKEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XCSE:DKEQ-SHR-CCP\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XCSE:DKEQ-SHR-IC\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XHEL:FIEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XHEL:FIEQ-SHR-CCP\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XHEL:FIEQ-SHR-IC\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XICE:ISEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR-NOK\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR-AO\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNSE:SEMM-NM\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNDK:FNDK-CPH\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNFI:SEMM-FN-HEL\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNIS:ISEC-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNSE:SEMM-FN-NOK\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNEE:EEMM-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNLV:LVMM-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNLT:LTMM-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:FNFI:SEMM-FN-HE-ERW\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XTAL:EEEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XRIS:LVEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XLIT:LTEQ-SHR\"/>\n" +
            "<param name=\"List\" value=\"M:GITS:RI:RSEBA\"/>\n" +
            "<param name=\"List\" value=\"M:GITS:TA:TSEBA\"/>\n" +
            "<param name=\"List\" value=\"M:GITS:VI:VSEBA\"/>\n" +
            "<param name=\"InstrumentISIN\" value=\"%1$s\"/>\n" +
            "<param name=\"InstrumentName\" value=\"%2$s\"/>\n" +
            "<param name=\"InstrumentFullName\" value=\"%3$s\"/>\n" +
            "<param name=\"json\" value=\"1\"/>\n" +
            "<param name=\"app\" value=\"/\"/>\n" +
            "</post>";

    /**
     * Search ticker by name.
     * @param name name.
     * @return stream of tickers
     */
    @Override
    public List<TickerSymbol> searchByName(String name) {
        return Collections.emptyList();
    }

    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String postBody =  String.format(HTTP_POST_BODY, isin, "", "");
        postBody = "xmlquery=" + URLEncoder.encode(postBody, StandardCharsets.UTF_8);

        try (BufferedReader reader = sendRequest(URL, postBody.getBytes(), "UTF-8")) {
            return handleResponse(reader);
        }
    }

    @Override
    protected void setRequestHeaders(HttpRequest.Builder requestBuilder) {
        super.setRequestHeaders(requestBuilder);

        requestBuilder.header("Accept", "*/*");
        requestBuilder.header("Accept-Encoding", "gzip, deflate");
        requestBuilder.header("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,sv;q=0.7");
        requestBuilder.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestBuilder.header("X-Requested-With", "XMLHttpRequest");
    }

    private List<TickerSymbol> handleResponse(BufferedReader reader) throws IOException {
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);

        if (jsonReader.peek() == JsonToken.STRING)
            return Collections.emptyList();

        List<TickerSymbol> tickers = new ArrayList<>();
        JsonUtil.optBeginObject(jsonReader);

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if ("inst".equals(name))
                parseTickers(jsonReader, tickers);
            else
                jsonReader.skipValue();
        }

        JsonUtil.optEndObject(jsonReader);
        return tickers;
    }

    @Override
    protected void parseTicker(JsonReader reader, TickerSymbol ticker) throws IOException {
        String name = reader.nextName();

        if ("@nm".equals(name)) {
            ticker.setSymbol(reader.nextString());
            ticker.setSource(Source.NASDAQ_OMX_NORDIC);
        }
        else if ("@fnm".equals(name))
            ticker.setName(reader.nextString());
        else if ("@isin".equals(name))
            ticker.setIsin(reader.nextString());
        else if ("@cr".equals(name))
            ticker.setCurrency(reader.nextString());
        else if ("@mkt".equals(name))
            ticker.setMic(getMic(reader.nextString()));
        else if ("@st".equals(name))
            ticker.setDescription(JsonUtil.nextOptString(reader, ""));
        else
            reader.skipValue();
    }

    private String getMic(String text) {
        String[] mkt = text.split(":");

        if (mkt.length <= 2)
            return null;

        return mkt[2];
    }
}
