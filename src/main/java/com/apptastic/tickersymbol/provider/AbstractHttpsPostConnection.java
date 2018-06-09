package com.apptastic.tickersymbol.provider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;


public abstract class AbstractHttpsPostConnection {

    protected BufferedReader sendRequest(String url, byte[] postBody, String characterEncoding) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        setRequestHeaders(connection, postBody);

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

    protected abstract void setRequestHeaders(URLConnection connection, byte[] postBody);

}
