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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Ticker provider implementation that fetches ticker information from Nasdaq OMX Nordic.
 * Nasdaq OMX Nordic is a stock exchange for swedish stocks and nordic stocks.
 */
public class NasdaqOmxNordic extends AbstractHttpsPostConnection implements TickerSymbolProvider {
    private static final String URL = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx";
    private static final String HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
    private static final String HTTP_POST_BODY = "<post>\n" +
            "<param name=\"SubSystem\" value=\"Prices\"/>\n" +
            "<param name=\"Action\" value=\"Search\"/>\n" +
            "<param name=\"inst.an\" value=\"nm,fnm,isin,tp,cr,chp,tb,mkt,st,isrid,ec\"/>\n" +
            "<param name=\"List\" value=\"M:INET:XSTO:SEEQ-SHR,M:INET:XSTO:SEEQ-SHR-CCP,M:INET:XSTO:SEEQ-SHR-IC,M:INET:XCSE:DKEQ-SHR,M:INET:XCSE:DKEQ-SHR-CCP,M:INET:XCSE:DKEQ-SHR-IC,M:INET:XHEL:FIEQ-SHR,M:INET:XHEL:FIEQ-SHR-CCP,M:INET:XHEL:FIEQ-SHR-IC,M:INET:XICE:ISEQ-SHR,M:INET:XSTO:SEEQ-SHR-NOK,M:INET:XSTO:SEEQ-SHR-AO,M:INET:FNSE:SEMM-NM,M:INET:FNDK:FNDK-CPH,M:INET:FNFI:SEMM-FN-HEL,M:INET:FNIS:ISEC-SHR,M:INET:FNSE:SEMM-FN-NOK,M:INET:FNEE:EEMM-SHR,M:INET:FNLV:LVMM-SHR,M:INET:FNLT:LTMM-SHR,M:INET:XTAL:EEEQ-SHR,M:INET:XRIS:LVEQ-SHR,M:INET:XLIT:LTEQ-SHR,M:GITS:RI:RSEBA,M:GITS:TA:TSEBA,M:GITS:VI:VSEBA,INET:XSTO:SEEQXFUNOK,INET:XSTO:SEEQXFU,INET:XSTO:SEEQXFUNOK,INET:XHEL:FIEQFUI,M:INET:XSTO:SEEQ-AIF,M:INET:XCSE:DKEQ-OCIS,M:INET:FNSE:STO-CERT,M:INET:FNFI:HEL-CERT,L:INET:H11074310,L:INET:H11074310,M:INET:FNDK:CPH-CERT,M:INET:FNSE:STO-WAR,M:INET:FNFI:HEL-WAR,M:INET:FNDK:CPH-WAR,GITS:SE:SEI,GITS:SE:SES,GITS:SE:DAI,GITS:SE:DAS,GITS:SE:HXS,GITS:SE:HXSOR,GITS:SE:EUI,GITS:SE:NNOI,M:GITS:SE:NOS,GITS:BONDPRICE30426,GITS:BONDYIELD87750,GITS:RETAILCORPORATEBONDSPRICE118,GITS:STRUCTUREDPRODUCTS30428,GITS:SUBORDINATEDLOANS30427,M:GITS:CO:CPHCB,M:GITS:FC:CPHFB,M:GITS:CO:CPHAU,GITS:BSTR,GITS:BCOR,INET:ISBS,INET:ISFO,INET:ISHU,INET:ISTO,INET:ISLI,INET:ISMB,INET:ISTN,INET:ISCB,INET:ISHA,GITS:HEBE,GITS:HECV,GITS:HE:HELRS,M:GITS:ST:STOCB,M:GITS:ST:STOGB,M:GITS:ST:STOMU,M:GITS:ST:STORB,M:GITS:ST:STOMB,M:GITS:FS:STOFR,M:GITS:FS:STOFC,M:GITS:FS:STOFI,M:GITS:ST:STOST,M:GITS:ST:STOLB,M:GITS:RI:RSEBA,M:GITS:TA:TSEBA,M:GITS:VI:VSEBA,INET:XCSE:DKEQUTC,GlobalIndex,INET:XSTO:SEEQEQR,INET:XCSE:DKEQEQR,INET:XHEL:FIEQEQR,M:INET:FNSE:STO-LEV,M:INET:FNFI:HEL-LEV,M:INET:FNDK:CPH-LEV,M:INET:FNSE:STO-TRA,M:INET:FNSE:STO-TNM,M:INET:FNFI:HEL-TRA,M:INET:FNDK:CPH-TRA,,\"/>\n" +
            "<param name=\"InstrumentISIN\" value=\"%1$s\"/>\n" +
            "<param name=\"InstrumentName\" value=\"%2$s\"/>\n" +
            "<param name=\"InstrumentFullName\" value=\"%3$s\"/>\n" +
            "<param name=\"json\" value=\"1\"/>\n" +
            "<param name=\"app\" value=\"/aktier/microsite\"/>\n" +
            "</post>";


    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String postBody =  String.format(HTTP_POST_BODY, isin, "", "");
        postBody = "xmlquery=" + URLEncoder.encode(postBody, "UTF-8");

        BufferedReader reader = sendRequest(URL, postBody.getBytes(), "UTF-8");
        JsonReader jsonReader = new JsonReader(reader);

        return handleResponse(jsonReader);
    }


    @Override
    protected void setRequestHeaders(URLConnection connection, byte[] postBody) {
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,sv;q=0.7");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(postBody.length));
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Host", "www.nasdaqomxnordic.com");
        connection.setRequestProperty("Origin", "http://www.nasdaqomxnordic.com");
        connection.setRequestProperty("Referer", "http://www.nasdaqomxnordic.com/aktier/microsite?Instrument=SSE323");
        connection.setRequestProperty("User-Agent", HTTP_USER_AGENT);
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
    }


    private List<TickerSymbol> handleResponse(JsonReader reader) throws IOException {
        List<TickerSymbol> tickers = new ArrayList<>();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if ("inst".equals(name))
                parseTickers(reader, tickers);
            else
                reader.skipValue();
        }

        if (reader.peek() == JsonToken.END_OBJECT)
            reader.endObject();

        return tickers;
    }


    private void parseTickers(JsonReader reader, List<TickerSymbol> tickers) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_ARRAY)
            reader.beginArray();

        while (reader.hasNext()) {
            reader.beginObject();

            TickerSymbol ticker = new TickerSymbol();
            ticker.setSource(Source.NASDAQ_OMX_NORDIC);

            while (reader.hasNext()) {
                String name = reader.nextName();

                if ("@nm".equals(name))
                    ticker.setSymbol(reader.nextString());
                else if ("@fnm".equals(name))
                    ticker.setName(reader.nextString());
                else if ("@isin".equals(name))
                    ticker.setIsin(reader.nextString());
                else if ("@cr".equals(name))
                    ticker.setCurrency(reader.nextString());
                else if ("@mkt".equals(name)) {
                    ticker.setMic(getMic(reader.nextString()));
                }
                else if ("@st".equals(name))
                    ticker.setDescription(nextOptString(reader, ""));
                else
                    reader.skipValue();
            }

            reader.endObject();

            if (isValid(ticker))
                tickers.add(ticker);
        }

        if (reader.peek() == JsonToken.END_ARRAY)
            reader.endArray();
    }

    private String getMic(String text) {
        String[] mkt = text.split(":");
        if (mkt.length <= 2)
            return null;

        return mkt[2];
    }

    private String nextOptString(JsonReader reader, String pDefaultValue) throws IOException {
        String tValue;
        JsonToken tToken = reader.peek();

        if (tToken != JsonToken.NULL && tToken == JsonToken.STRING) {
            tValue = reader.nextString();
        }
        else {
            tValue = pDefaultValue;
            reader.skipValue();
        }

        return tValue;
    }


    private boolean isValid(TickerSymbol ticker) {
        return ticker != null && ticker.getSymbol() != null && ticker.getName() != null && ticker.getIsin() != null &&
                ticker.getCurrency() != null && ticker.getMic() != null;
    }

}
