package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.provider.NordicGrowthMarket;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NordicGrowthMarketTest {

    @Test
    public void isinNotFound() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0002658278");

        assertNotNull(tickers);
        assertEquals(0, tickers.size());
    }


    @Test
    public void testTransferatorB() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0006877700");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Tectona Capital B", ticker.getName());
        assertEquals("TCAB", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0006877700", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }


    @Test
    public void testWilLak() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0008964266");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("WilLak", ticker.getName());
        assertEquals("WIL", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0008964266", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }


    @Test
    public void testSwemetB() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0006886917");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Swemet B", ticker.getName());
        assertEquals("SWEM", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0006886917", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }


    @Test
    public void testGuidelineGeo() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0005562014");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Guideline Geo", ticker.getName());
        assertEquals("GGEO", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0005562014", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }


    @Test
    public void testPaynova() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0001162462");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Paynova", ticker.getName());
        assertEquals("PAY", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0001162462", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }


    @Test
    public void testChordateMedical() throws IOException {
        NordicGrowthMarket provider = new NordicGrowthMarket();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0009495559");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Chordate Medical Holding", ticker.getName());
        assertEquals("CMH", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0009495559", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }

}
