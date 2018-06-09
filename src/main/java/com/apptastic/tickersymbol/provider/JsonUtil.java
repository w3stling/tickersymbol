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
}
