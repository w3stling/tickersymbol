/*
 * MIT License
 *
 * Copyright (c) 2018, Apptastic Software
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

import com.apptastic.tickersymbol.provider.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Class for searching ticker symbols.
 */
public class TickerSymbolSearch {
    private static final Pattern ISIN_PATTERN = Pattern.compile("^[A-Z]{2}[A-Z\\d]{9}\\d$");
    private List<TickerSymbolProvider> tickerProviders;


    /**
     * Constructor.
     */
    public TickerSymbolSearch() {
        tickerProviders = Arrays.asList(new NasdaqOmxNordic(),
                                        new NordicGrowthMarket(),
                                        //new SpotlightStockMarket(),
                                        new MorningStar(),
                                        new Avanza(),
                                        new Nordnet());
    }


    /**
     * Search ticker symbol by name.
     * @param name name
     * @return stream of ticker symbols
     */
    public Stream<TickerSymbol> searchByName(String name) {
        List<Callable<List<TickerSymbol>>> finders = tickerProviders.stream()
                .map(p -> new NameTickerSymbolFinder(name, p))
                .collect(Collectors.toList());

        return invokeAll(finders).stream()
                .map(this::getTickerResponse)
                .flatMap(Collection::stream);
    }


    /**
     * Search ticker symbol by ISIN code.
     * @param isin ISIN code
     * @return stream of ticker symbols with the give ISIN code
     */
    public Stream<TickerSymbol> searchByIsin(String isin) {
        if (!validIsin(isin))
            return Stream.empty();

        List<Callable<List<TickerSymbol>>> finders = tickerProviders.stream()
                .map(p -> new IsinTickerSymbolFinder(isin, p))
                .collect(Collectors.toList());

        return invokeAll(finders).stream()
                .map(this::getTickerResponse)
                .flatMap(Collection::stream)
                .filter(t -> isin.equals(t.getIsin()));
    }


    private boolean validIsin(String text) {
        return text != null && ISIN_PATTERN.matcher(text).matches();
    }


    private List<Future<List<TickerSymbol>>> invokeAll(List<Callable<List<TickerSymbol>>> finders) {
        ExecutorService es = Executors.newCachedThreadPool();

        try {
            return es.invokeAll(finders);
        }
        catch (InterruptedException e) {
            logException("Failed to invoke all providers. ", e);
            Thread.currentThread().interrupt();
        }
        finally {
            es.shutdown();
        }

        return Collections.emptyList();
    }


    private List<TickerSymbol> getTickerResponse(Future<List<TickerSymbol>> future) {
        List<TickerSymbol> tickers = Collections.emptyList();

        try {
            tickers = future.get(8, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            logException("Failed to get response. ", e);
            Thread.currentThread().interrupt();
        }

        return tickers;
    }


    private void logException(String message, Exception e) {
        Logger logger = Logger.getLogger("com.apptastic.tickersymbol");

        if (logger.isLoggable(Level.WARNING))
            logger.log(Level.WARNING, message, e);
    }
}
