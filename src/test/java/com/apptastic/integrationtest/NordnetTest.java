package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.provider.Nordnet;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class NordnetTest {

    @Test
    public void testNameNorwegianAirShuttle() throws IOException {
        Nordnet provider = new Nordnet();
        List<TickerSymbol> tickers = provider.searchByName("Norwegian Air");

        assertNotNull(tickers);
        assertTrue(tickers.size() > 1);

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Norwegian Air Shuttle", ticker.getName());
        assertEquals("NAS", ticker.getSymbol());
        assertEquals("NOK", ticker.getCurrency());
        assertEquals("NO0010196140", ticker.getIsin());
        assertEquals("XOSL", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void isinNotFound() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("CA65339B1000");
        assertEquals(0, tickers.size());
    }

    @Test
    public void testIsinNgexResources() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("CA65339B1004");
        assertEquals(3, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("NGEx Resources Inc", ticker.getName());
        assertEquals("NGQ", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());

        ticker = tickers.get(1);
        assertEquals("NGEX RESOURCES INC", ticker.getName());
        assertEquals("NGQ", ticker.getSymbol());
        assertEquals("CAD", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        assertEquals("XTSE", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());

        ticker = tickers.get(2);
        assertEquals("NGEX RESOURCES INC", ticker.getName());
        assertEquals("NGQRF", ticker.getSymbol());
        assertEquals("USD", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        //assertEquals("Nasdaq OTC Foreign", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinPandora() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("DK0060252690");
        assertTrue(tickers.size() >= 1);

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Pandora A/S", ticker.getName());
        assertEquals("PNDORA", ticker.getSymbol());
        assertEquals("DKK", ticker.getCurrency());
        assertEquals("DK0060252690", ticker.getIsin());
        assertEquals("XCSE", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinYaraInternational() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("NO0010208051");
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Yara International", ticker.getName());
        assertEquals("YAR", ticker.getSymbol());
        assertEquals("NOK", ticker.getCurrency());
        assertEquals("NO0010208051", ticker.getIsin());
        assertEquals("XOSL", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinKone() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("FI0009013403");
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("KONE Corporation", ticker.getName());
        assertEquals("KNEBV", ticker.getSymbol());
        assertEquals("EUR", ticker.getCurrency());
        assertEquals("FI0009013403", ticker.getIsin());
        assertEquals("XHEL", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinNetflix() throws IOException {
        Nordnet provider = new Nordnet();

        List<TickerSymbol> tickers = provider.searchByIsin("US64110L1061");
        assertTrue(tickers.size() >= 1);

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Netflix, Inc", ticker.getName());
        assertEquals("NFLX", ticker.getSymbol());
        assertEquals("USD", ticker.getCurrency());
        assertEquals("US64110L1061", ticker.getIsin());
        assertEquals("XNYS", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinSpectraCure() throws IOException {
        Nordnet provider = new Nordnet();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0007158118");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("SpectraCure AB", ticker.getName());
        assertEquals("SPEC", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0007158118", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

    @Test
    public void testIsinPaynova() throws IOException {
        Nordnet provider = new Nordnet();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0001162462");

        assertNotNull(tickers);
        assertTrue(tickers.size() >= 1);

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Paynova", ticker.getName());
        assertEquals("PAY", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0001162462", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.NORDNET, ticker.getSource());
    }

}
