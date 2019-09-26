package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.*;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class TickerSymbolSearchTest {


    @Test
    public void testInvalidIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("0065339B1004")
                .collect(Collectors.toList());

        assertTrue(tickers.isEmpty());
    }

    @Test
    public void testInvalidNullIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin(null)
                .collect(Collectors.toList());

        assertTrue(tickers.isEmpty());
    }


    @Test
    public void testNasdaqOmxNordicSearchByIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0000108656")
                .filter(t -> "SEK".equals(t.getCurrency()))
                .filter(t -> t.getSource() == Source.NASDAQ_OMX_NORDIC)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

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
    public void testNordicGrowthMarketSearchByIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0013108867")
                .filter(t -> t.getSource() == Source.NORDIC_GROWTH_MARKET)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Paynova", ticker.getName());
        assertEquals("PAY", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0013108867", ticker.getIsin());
        assertEquals("XNGM", ticker.getMic());
        assertEquals("", ticker.getDescription());
        assertEquals(Source.NORDIC_GROWTH_MARKET, ticker.getSource());
    }

/*
    @Test
    public void testSpotlightStockMarketSearchByIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0009548597")
                .filter(t -> t.getSource() == Source.SPOTLIGHT_STOCK_MARKET)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("EatGood", ticker.getName());
        assertEquals("EATG", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0009548597", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue(ticker.getDescription() != null);
        assertEquals(Source.SPOTLIGHT_STOCK_MARKET, ticker.getSource());
    }
*/

    @Test
    public void testAvanzaSearchByName() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByName("Norwegian Air Shuttle")
                .filter(t -> t.getSource() == Source.AVANZA)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Norwegian Air Shuttle", ticker.getName());
        assertEquals("NAS", ticker.getSymbol());
        assertEquals("NOK", ticker.getCurrency());
        assertEquals("NO0010196140", ticker.getIsin());
        assertEquals("XOSL", ticker.getMic());
        assertTrue(!ticker.getDescription().isEmpty());
        assertEquals(Source.AVANZA, ticker.getSource());
    }


    @Test
    public void testAvanzaSearchByIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0009548597")
                .filter(t -> t.getSource() == Source.AVANZA)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("EatGood", ticker.getName());
        assertEquals("EATG", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0009548597", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertEquals("Spotlight Stock Market", ticker.getDescription());
        assertEquals(Source.AVANZA, ticker.getSource());
    }


    @Test
    public void testNordnetSearchByName() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByName("Norwegian Air Shuttle")
                .filter(t -> t.getSource() == Source.NORDNET)
                .collect(Collectors.toList());

        assertTrue(tickers.size() > 0);

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
    public void testNordnetSearchByIsin() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0009548597")
                .filter(t -> t.getSource() == Source.NORDNET)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("EatGood", ticker.getName());
        assertEquals("EATG", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0009548597", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue(ticker.getDescription().contains("Spotlight"));
        assertEquals(Source.NORDNET, ticker.getSource());
    }


    @Test
    public void testEmptyIsinCodeNullProvider() {
        IsinTickerSymbolFinder finder = new IsinTickerSymbolFinder("", null);
        List<TickerSymbol> symbols = finder.call();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void testEmptyNameNullProvider() {
        NameTickerSymbolFinder finder = new NameTickerSymbolFinder("", null);
        List<TickerSymbol> symbols = finder.call();
        assertTrue(symbols.isEmpty());
    }

}
