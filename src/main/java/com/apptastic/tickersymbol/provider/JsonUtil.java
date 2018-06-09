package com.apptastic.tickersymbol.provider;

import com.apptastic.tickersymbol.TickerSymbol;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;


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

/*
    public static void parseArrayOfTickers(JsonReader reader, List<TickerSymbol> tickers, BiConsumer<JsonReader, List<TickerSymbol>> consumer) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_ARRAY)
            reader.beginArray();

        while (reader.hasNext()) {
            reader.beginObject();
            consumer.accept(reader, tickers);
            reader.endObject();
        }

        if (reader.peek() == JsonToken.END_ARRAY)
            reader.endArray();
    }
*/
}
