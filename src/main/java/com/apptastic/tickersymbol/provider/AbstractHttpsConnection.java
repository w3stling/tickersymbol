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

import com.apptastic.tickersymbol.TickerSymbol;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;


public abstract class AbstractHttpsConnection {
    private static final String HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";


    protected BufferedReader sendRequest(String url, String characterEncoding) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        setTimeouts(connection);
        setGetRequestHeaders(connection);

        InputStream inputStream = connection.getInputStream();

        if ("gzip".equals(connection.getContentEncoding()))
            inputStream = new GZIPInputStream(inputStream);

        return new BufferedReader(new InputStreamReader(inputStream, characterEncoding));
    }


    protected BufferedReader sendRequest(String url, byte[] postBody, String characterEncoding) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        setTimeouts(connection);
        setPostRequestHeaders(connection, postBody);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(postBody);
        os.flush();
        os.close();

        InputStream inputStream = connection.getInputStream();

        if ("gzip".equals(connection.getContentEncoding()))
            inputStream = new GZIPInputStream(inputStream);

        return new BufferedReader(new InputStreamReader(inputStream, characterEncoding));
    }

    protected void setTimeouts(URLConnection connection) {
        connection.setConnectTimeout(10 * 1000);
        connection.setReadTimeout(10 * 1000);
    }

    protected void setGetRequestHeaders(URLConnection connection) {
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("User-Agent", HTTP_USER_AGENT);
    }


    protected void setPostRequestHeaders(URLConnection connection, byte[] postBody) {
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("User-Agent", HTTP_USER_AGENT);
        connection.setRequestProperty("Content-Length", String.valueOf(postBody.length));
    }

    protected void parseTickers(JsonReader reader, List<TickerSymbol> tickers) throws IOException {
        JsonUtil.optBeginArray(reader);

        while (reader.hasNext()) {
            reader.beginObject();
            TickerSymbol ticker = new TickerSymbol();

            while (reader.hasNext()) {
                parseTicker(reader, ticker);
            }

            if (isTickerSymbolValid(ticker))
                tickers.add(ticker);

            reader.endObject();
        }

        JsonUtil.optEndArray(reader);
    }


    protected void parseTicker(JsonReader reader, TickerSymbol ticker) throws IOException {

    }


    protected boolean isTickerSymbolValid(TickerSymbol ticker) {
        return ticker != null && ticker.getSymbol() != null && ticker.getName() != null && ticker.getIsin() != null &&
                ticker.getCurrency() != null && ticker.getMic() != null;
    }
}
