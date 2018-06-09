package com.apptastic.tickersymbol;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TickerTest {

    private TickerSymbol defaultTicker() {
        return new TickerSymbol("ERIC B", "Ericsson B", "SEK", "SE0000108656", "XSTO", "Ordinary shares", Source.NASDAQ_OMX_NORDIC);
    }


    @Test
    public void tickerSimple() {
        TickerSymbol ticker = defaultTicker();

        assertNotNull(ticker);
        assertEquals("ERIC B", ticker.getSymbol());
        assertEquals("Ericsson B", ticker.getName());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0000108656", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void tickerCopy() {
        TickerSymbol ticker = defaultTicker();

        TickerSymbol copy = new TickerSymbol(ticker);

        assertEquals(ticker.getSymbol(), copy.getSymbol());
        assertEquals(ticker.getName(), copy.getName());
        assertEquals(ticker.getCurrency(), copy.getCurrency());
        assertEquals(ticker.getIsin(), copy.getIsin());
        assertEquals(ticker.getMic(), copy.getMic());
        assertEquals(ticker.getDescription(), copy.getDescription());
        assertEquals(ticker.getSource(), copy.getSource());
    }


    @Test
    public void tickerEquals() {
        TickerSymbol ticker = defaultTicker();
        assertTrue(ticker.equals(ticker));

        TickerSymbol copy = new TickerSymbol(ticker);
        assertTrue(ticker.equals(copy));


        TickerSymbol symbol = defaultTicker();
        symbol.setSymbol("symbol");
        assertFalse(ticker.equals(symbol));

        TickerSymbol name = defaultTicker();
        name.setName("name");
        assertFalse(ticker.equals(name));

        TickerSymbol currency = defaultTicker();
        currency.setCurrency("USD");
        assertFalse(ticker.equals(currency));

        TickerSymbol isin = defaultTicker();
        isin.setCurrency("SE0000000000");
        assertFalse(ticker.equals(isin));

        TickerSymbol mic = defaultTicker();
        mic.setCurrency("XNGM");
        assertFalse(ticker.equals(mic));

        TickerSymbol description = defaultTicker();
        description.setDescription("description");
        assertFalse(ticker.equals(description));

        TickerSymbol source = defaultTicker();
        source.setSource(Source.NORDIC_GROWTH_MARKET);
        assertFalse(ticker.equals(source));
    }


    @Test
    public void tickerHash() {
        TickerSymbol ticker1 = defaultTicker();

        TickerSymbol ticker2 = defaultTicker();
        ticker2.setSymbol("symbol");

        TickerSymbol ticker3 = defaultTicker();
        ticker3.setName("name");

        HashMap<TickerSymbol, String> map = new HashMap<>();
        map.put(ticker1, "ticker1");
        map.put(ticker2, "ticker2");
        map.put(ticker3, "ticker3");

        assertEquals("ticker1", map.get(ticker1));
        assertEquals("ticker2", map.get(ticker2));
        assertEquals("ticker3", map.get(ticker3));


        TickerSymbol ticker4 = defaultTicker();
        ticker4.setName("name");

        map.put(ticker4, "ticker4");
        assertEquals("ticker4", map.get(ticker4));
        assertEquals("ticker4", map.get(ticker3));
    }

}
