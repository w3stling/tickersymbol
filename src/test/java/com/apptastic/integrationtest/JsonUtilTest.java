package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.provider.JsonUtil;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;


public class JsonUtilTest {

    private static final String JSON_DATA1 = "{\n" +
            "  \"data\": \"some text\"\n" +
            "}";

    private static final String JSON_DATA2 = "{\n" +
            "  \"data\": null\n" +
            "}";

    private static final String JSON_DATA3 = "{\n" +
            "  \"data\": 1234\n" +
            "}";

    private static final String JSON_DATA4 = "{\n" +
           "  \"array\": [\n" +
            "      {\n" +
            "        \"data\": \"some text\"\n" +
            "      }" +
            "   ]" +
            " }";


    @Test
    public void jsonString() throws IOException {
        Reader reader = new StringReader(JSON_DATA1);
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        assertEquals("data", jsonReader.nextName());
        assertEquals("some text", JsonUtil.nextOptString(jsonReader, ""));
        jsonReader.endObject();
    }


    @Test
    public void jsonStringMissing() throws IOException {
        Reader reader = new StringReader(JSON_DATA2);
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        assertEquals("data", jsonReader.nextName());
        assertEquals("null string", JsonUtil.nextOptString(jsonReader, "null string"));
        jsonReader.endObject();
    }


    @Test
    public void jsonNumber() throws IOException {
        Reader reader = new StringReader(JSON_DATA3);
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        assertEquals("data", jsonReader.nextName());
        assertEquals("not a string", JsonUtil.nextOptString(jsonReader, "not a string"));
        jsonReader.endObject();
    }

    @Test
    public void jsonObjectBeginEnd() throws IOException {
        Reader reader = new StringReader(JSON_DATA1);
        JsonReader jsonReader = new JsonReader(reader);

        JsonUtil.optEndObject(jsonReader);
        JsonUtil.optBeginObject(jsonReader);
        assertEquals("data", jsonReader.nextName());
        assertEquals("some text", JsonUtil.nextOptString(jsonReader, ""));
        JsonUtil.optBeginObject(jsonReader);
        JsonUtil.optEndObject(jsonReader);
    }

    @Test
    public void jsonArrayBeginEnd() throws IOException {
        Reader reader = new StringReader(JSON_DATA4);
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        assertEquals("array", jsonReader.nextName());
        JsonUtil.optEndArray(jsonReader);
        JsonUtil.optBeginArray(jsonReader);
        jsonReader.beginObject();
        assertEquals("data", jsonReader.nextName());
        assertEquals("some text", JsonUtil.nextOptString(jsonReader, ""));
        jsonReader.endObject();
        JsonUtil.optBeginArray(jsonReader);
        JsonUtil.optEndArray(jsonReader);
        jsonReader.endObject();
    }
}
