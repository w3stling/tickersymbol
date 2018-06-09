package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.provider.AktieTorget;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;


public class AktieTorgetTest {

    @Test
    public void isinNotFound() throws IOException {
        AktieTorget provider = new AktieTorget();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0011204486");

        assertNotNull(tickers);
        assertEquals(0, tickers.size());
    }


    @Test
    public void testTransferatorB() throws IOException {
        AktieTorget provider = new AktieTorget();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0002658278");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Transferator B", ticker.getName());
        assertEquals("TRAN B", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0002658278", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue("Share".equals(ticker.getDescription()) || "Aktie".equals(ticker.getDescription()));
        assertEquals(Source.AKTIE_TORGET, ticker.getSource());
    }


    @Test
    public void testEatGood() throws IOException {
        AktieTorget provider = new AktieTorget();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0009548597");

        assertNotNull(tickers);
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
    public void testLatvianForestB() throws IOException {
        AktieTorget provider = new AktieTorget();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0003883008");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("Latvian Forest B", ticker.getName());
        assertEquals("LATF B", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0003883008", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue("Share".equals(ticker.getDescription()) || "Aktie".equals(ticker.getDescription()));
        assertEquals(Source.AKTIE_TORGET, ticker.getSource());
    }


    @Test
    public void testSpectraCure() throws IOException {
        AktieTorget provider = new AktieTorget();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0007158118");

        assertNotNull(tickers);
        assertEquals(1, tickers.size());

        TickerSymbol ticker = tickers.get(0);
        assertEquals("SpectraCure", ticker.getName());
        assertEquals("SPEC", ticker.getSymbol());
        assertEquals("SEK", ticker.getCurrency());
        assertEquals("SE0007158118", ticker.getIsin());
        assertEquals("XSAT", ticker.getMic());
        assertTrue("Share".equals(ticker.getDescription()) || "Aktie".equals(ticker.getDescription()));
        assertEquals(Source.AKTIE_TORGET, ticker.getSource());
    }
}
