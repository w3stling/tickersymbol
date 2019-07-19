package com.apptastic.integrationtest;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;
import com.apptastic.tickersymbol.provider.MorningStar;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class MorningStarTest {

    @Test
    public void isinNotFound() throws IOException {
        MorningStar provider = new MorningStar();
        List<TickerSymbol> tickers = provider.searchByIsin("SE0000584940");

        assertNotNull(tickers);
        assertEquals(0, tickers.size());
    }


    @Test
    public void testNgexResources() throws IOException {
        MorningStar provider = new MorningStar();
        List<TickerSymbol> tickers = provider.searchByIsin("CA65339B1004");

        assertNotNull(tickers);
        assertTrue(tickers.size() > 0);

        TickerSymbol ticker = null;

        for (TickerSymbol t : tickers) {
            if ("XTSE".equals(t.getMic())) {
                ticker = t;
                break;
            }
        }

        assertNotNull(ticker);
        assertEquals("Josemaria Resources Inc", ticker.getName());
        assertEquals("NGQ", ticker.getSymbol());
        assertEquals("CAD", ticker.getCurrency());
        assertEquals("CA65339B1004", ticker.getIsin());
        assertEquals("XTSE", ticker.getMic());
        assertEquals("TORONTO STOCK EXCHANGE", ticker.getDescription());
        assertEquals(Source.MORNING_STAR, ticker.getSource());
    }

}
