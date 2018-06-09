package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.TickerSymbolSearch;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TickerSymbolSearchTest {

    @Test
    public void testNasdaqOmxNordic() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("CA65339B1004")
                .filter(t -> "SEK".equals(t.getCurrency()))
                .filter(t -> t.getSource() == Source.NASDAQ_OMX_NORDIC)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("NGEx Resources", ticker.getName());
        assertEquals("NGQ", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        assertEquals("XSTO", ticker.getMic());
        assertEquals("Ordinary shares", ticker.getDescription());
        assertEquals(Source.NASDAQ_OMX_NORDIC, ticker.getSource());
    }


    @Test
    public void testNordicGrowthMarket() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0001162462")
                .filter(t -> t.getSource() == Source.NORDIC_GROWTH_MARKET)
                .collect(Collectors.toList());

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
    public void testAktieTorget() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("SE0009548597")
                .filter(t -> t.getSource() == Source.AKTIE_TORGET)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("EatGood", ticker.getName());
        assertEquals("EATG", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0009548597", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue("Share".equals(ticker.getDescription()) || "Aktie".equals(ticker.getDescription()));
        assertEquals(Source.AKTIE_TORGET, ticker.getSource());
    }

    @Test
    public void testMorningStar() {
        TickerSymbolSearch ts = new TickerSymbolSearch();
        List<TickerSymbol> tickers = ts.searchByIsin("CA65339B1004")
                .filter(t -> "CAD".equals(t.getCurrency()))
                .filter(t -> t.getSource() == Source.MORNING_STAR)
                .collect(Collectors.toList());

        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("NGEx Resources Inc", ticker.getName());
        assertEquals("NGQ", ticker.getSymbol());
        assertEquals("CAD", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        assertEquals("XTSE", ticker.getMic());
        assertEquals("TORONTO STOCK EXCHANGE", ticker.getDescription());
        assertEquals(Source.MORNING_STAR, ticker.getSource());
    }

}
