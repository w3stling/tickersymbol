package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.provider.NasdaqOmxNordic;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class NasdaqOmxNordicTest {

    @Test
    public void isinNotFound() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0006877700");

        assertNotNull(tickers);
        assertEquals(0, tickers.size());
    }


    @Test
    public void testEricsson() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0000108656");

        assertNotNull(tickers);
        assertEquals(2, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Ericsson B", ticker.getName());
        assertEquals("ERIC B", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0000108656", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void testClasOhlson() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0000584948");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Clas Ohlson B", ticker.getName());
        assertEquals("CLAS B", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0000584948", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void testMrGreen() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0010949750");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Mr Green & Co", ticker.getName());
        assertEquals("MRG", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0010949750", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void testMycronic() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0000375115");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Mycronic", ticker.getName());
        assertEquals("MYCR", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0000375115", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void testLivIhop() throws IOException {
        NasdaqOmxNordic provider = new NasdaqOmxNordic();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0010769356");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Liv ihop", ticker.getName());
        assertEquals("LIVI", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0010769356", ticker.getIsin());
        assertEquals("FNSE", ticker.getMic());
        assertEquals("Miscellaneous", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }

}
