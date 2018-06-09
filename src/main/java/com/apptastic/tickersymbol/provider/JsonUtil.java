package com.apptastic.tickersymbol.provider;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;


public class JsonUtil {

    private JsonUtil() {

    }


    public static String nextOptString(JsonReader reader, String pDefaultValue) throws IOException {
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
