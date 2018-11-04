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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;


public abstract class AbstractHttpsConnection {
    private static final String HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
    private final HttpClient httpClient;

    protected AbstractHttpsConnection() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    protected BufferedReader sendRequest(String url, String characterEncoding) throws IOException {
        var builder = HttpRequest.newBuilder(URI.create(url)).GET();
        setTimeouts(builder);
        setRequestHeaders(builder);
        var req = builder.build();

        try {
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
            var inputStream = resp.body();

            if (Optional.of("gzip").equals(resp.headers().firstValue("Content-Encoding")))
                inputStream = new GZIPInputStream(inputStream);

            var reader = new InputStreamReader(inputStream, characterEncoding);
            return new BufferedReader(reader);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    protected BufferedReader sendRequest(String url, byte[] postBody, String characterEncoding) throws IOException {
        var builder = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofByteArray(postBody));

        setTimeouts(builder);
        setRequestHeaders(builder);

        var req = builder.build();

        try {
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
            var inputStream = resp.body();

            if (Optional.of("gzip").equals(resp.headers().firstValue("Content-Encoding")))
                inputStream = new GZIPInputStream(inputStream);

            var reader = new InputStreamReader(inputStream, characterEncoding);
            return new BufferedReader(reader);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    protected void setTimeouts(HttpRequest.Builder requestBuilder) {
        requestBuilder.timeout(Duration.ofSeconds(15));
    }

    protected void setRequestHeaders(HttpRequest.Builder requestBuilder) {
        requestBuilder.header("Accept-Encoding", "gzip, deflate");
        requestBuilder.header("User-Agent", HTTP_USER_AGENT);
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
