package com.apptastic.tickersymbol.provider;

import com.apptastic.tickersymbol.TickerSymbol;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

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

        setGetRequestHeaders(connection);

        InputStream inputStream = connection.getInputStream();

        if ("gzip".equals(connection.getContentEncoding()))
            inputStream = new GZIPInputStream(inputStream);

        return new BufferedReader(new InputStreamReader(inputStream, characterEncoding));
    }


    protected BufferedReader sendRequest(String url, byte[] postBody, String characterEncoding) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

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
        if (reader.peek() == JsonToken.BEGIN_ARRAY)
            reader.beginArray();

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

        if (reader.peek() == JsonToken.END_ARRAY)
            reader.endArray();
    }


    protected void parseTicker(JsonReader reader, TickerSymbol ticker) throws IOException {

    }


    protected boolean isTickerSymbolValid(TickerSymbol ticker) {
        return ticker != null && ticker.getSymbol() != null && ticker.getName() != null && ticker.getIsin() != null &&
                ticker.getCurrency() != null && ticker.getMic() != null;
    }
}
