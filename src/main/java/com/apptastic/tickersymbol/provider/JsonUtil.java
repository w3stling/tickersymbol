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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;


public class JsonUtil {

    private JsonUtil() {

    }


    public static String nextOptString(JsonReader reader, String pDefaultValue) throws IOException {
        String tValue = pDefaultValue;
        JsonToken tToken = reader.peek();

        if (tToken != JsonToken.NULL && tToken == JsonToken.STRING)
            tValue = reader.nextString();
        else
            reader.skipValue();

        return tValue;
    }


    public static void optBeginObject(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
    }


    public static void optEndObject(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.END_OBJECT)
            reader.endObject();
    }

    public static void optBeginArray(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_ARRAY)
            reader.beginArray();
    }


    public static void optEndArray(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.END_ARRAY)
            reader.endArray();
    }

}
