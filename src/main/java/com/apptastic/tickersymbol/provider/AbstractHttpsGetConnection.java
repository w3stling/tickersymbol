package com.apptastic.tickersymbol.provider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;


public abstract class AbstractHttpsGetConnection {

    protected BufferedReader sendRequest(String url, String characterEncoding) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        setRequestHeaders(connection);

        InputStream inputStream = connection.getInputStream();

        if ("gzip".equals(connection.getContentEncoding()))
            inputStream = new GZIPInputStream(inputStream);

        return new BufferedReader(new InputStreamReader(inputStream, characterEncoding));
    }

    protected abstract void setRequestHeaders(URLConnection connection);
}
