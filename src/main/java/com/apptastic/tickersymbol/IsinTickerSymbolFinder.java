/*
 * MIT License
 *
 * Copyright (c) 2020, Apptastic Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.apptastic.tickersymbol;

import com.apptastic.tickersymbol.provider.TickerSymbolProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Internal class for making parallel ticker provider searches.
 */
public class IsinTickerSymbolFinder implements Callable<List<TickerSymbol>> {
    private String isin;
    private TickerSymbolProvider provider;

    /**
     * Constructor
     * @param isin ISIN code
     * @param provider the provider of the ticker
     */
    public IsinTickerSymbolFinder(String isin, TickerSymbolProvider provider) {
        this.isin = isin;
        this.provider = provider;
    }

    /**
     * Fetches the ticker.
     * @return list of tickers
     */
    @Override
    public List<TickerSymbol> call() {
        try {
            return provider.searchByIsin(isin);
        }
        catch (Exception e) {
            Logger logger = Logger.getLogger("com.apptastic.tickersymbol");

            if (logger.isLoggable(Level.WARNING))
                logger.log(Level.WARNING, "Failed to get response. ", e);

            return Collections.emptyList();
        }
    }
}

