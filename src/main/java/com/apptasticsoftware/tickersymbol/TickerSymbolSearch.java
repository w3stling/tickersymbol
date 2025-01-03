/*
 * MIT License
 *
 * Copyright (c) 2022, Apptastic Software
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
package com.apptasticsoftware.tickersymbol;

import com.apptasticsoftware.lei.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Class for searching ticker symbols.
 */
public class TickerSymbolSearch {
    private static final String LOGGER = "com.apptasticsoftware.tickersymbol";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
    private static final String BASE_URL = "https://api.openfigi.com";
    private static final Pattern COLUMN_PATTERN = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private final int cacheSize;
    private final ConcurrentSkipListMap<String, List<TickerSymbol>> cache;
    private final ConcurrentHashMap<String, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Exchange> exchangeByExchangeCode = new ConcurrentHashMap<>();
    private static TickerSymbolSearch instance;

    TickerSymbolSearch(int cacheSize) {
        this.cacheSize = cacheSize;
        cache = new ConcurrentSkipListMap<>();
         getExchangesFromFile().forEach(
                 exchange -> exchangeByExchangeCode.put(exchange.getExchangeCode(), exchange)
         );
    }

    /**
     * Get instance for doing ticker symbol searches.
     * @return instance
     */
    public static TickerSymbolSearch getInstance() {
        return getInstance(100000);
    }

    /**
     * Get instance for doing ticker symbol searches.
     * @param cacheSize - number of LEI to hold in cache
     * @return instance
     */
    public static TickerSymbolSearch getInstance(int cacheSize) {
        if (instance != null && instance.cacheSize == cacheSize)
            return instance;

        instance = new TickerSymbolSearch(cacheSize);
        return instance;
    }

    /**
     * Search ticker symbol by identifier
     * @param identifier identifier
     * @return list of ticker symbols
     */
    public List<TickerSymbol> searchByIdentifier(String identifier) {
        if (!(IsinCodeValidator.isValid(identifier) || CusipValidator.isValid(identifier) || SedolValidator.isValid(identifier))) {
            return Collections.emptyList();
        }

        List<TickerSymbol> list = cache.get(identifier);
        if (list != null) {
            return list;
        }

        var pendingRequest = getPendingRequest(identifier);
        if (pendingRequest != null) {
            return getPendingResult(identifier);
        }

        pendingRequest = pendingRequests.get(identifier);
        list = searchByIdentifiers(identifier);

        if (list.isEmpty() && IsinCodeValidator.isValid(identifier)) {
            list = searchByIsin(identifier);
        }

        if (list.isEmpty() && CusipValidator.isValid(identifier)) {
            list = searchByCusip(identifier);
        }

        if (list.isEmpty() && SedolValidator.isValid(identifier)) {
            list = searchBySedol(identifier);
        }

        cacheSearchResult(identifier, list);
        pendingRequest.done();
        pendingRequests.remove(identifier);
        return list;
    }

    List<TickerSymbol> searchByIdentifiers(String identifier) {
        return getTickerSymbols(getIdType(identifier).orElse(null), identifier);
    }

    List<TickerSymbol> searchByIsin(String isin) {
        return getTickerSymbols("ID_ISIN", isin);
    }

    List<TickerSymbol> searchByCusip(String cusip) {
        return getTickerSymbols("ID_CUSIP", cusip);
    }

    List<TickerSymbol> searchBySedol(String sedol) {
        return getTickerSymbols("ID_SEDOL", sedol);
    }

    private Optional<String> getIdType(String identifier) {
        if (IsinCodeValidator.isValid(identifier)) {
            return Optional.of("ID_ISIN");
        } else if (CusipValidator.isValid(identifier)) {
            return Optional.of("ID_CUSIP");
        } else if (SedolValidator.isValid(identifier)) {
            return Optional.of("ID_SEDOL");
        } else {
            return Optional.empty();
        }
    }

    private List<TickerSymbol> getTickerSymbols(String identifierType, String identifier) {
        List<TickerSymbol> list = new ArrayList<>();

        try {
            String body = String.format("[{\"idType\": \"%s\", \"idValue\": \"%s\"}]", identifierType, identifier);
            var response = makePostApiCall("/v3/mapping", body, null);
            var tickerSymbol = parseResponse(response);
            if (tickerSymbol != null) {
                return List.of(tickerSymbol);
            }
        } catch (Exception ignored) { }

        return list;
    }

    private TickerSymbol parseResponse(String jsonData) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonData, JsonArray.class);

        if (jsonArray != null && !jsonArray.isEmpty()) {
            JsonObject firstElement = jsonArray.get(0).getAsJsonObject();

            if (firstElement.has("data")) {
                JsonArray dataArray = firstElement.getAsJsonArray("data");

                if (dataArray != null && !dataArray.isEmpty()) {
                    JsonObject firstDataElement = dataArray.get(0).getAsJsonObject();
                    TickerData tickerData = gson.fromJson(firstDataElement, TickerData.class);
                    System.out.println(tickerData);
                    var tickerSymbol = new TickerSymbol();
                    tickerSymbol.setSymbol(tickerData.ticker);
                    tickerSymbol.setDescription(tickerData.name);
                    tickerSymbol.setType(tickerData.securityType);
                    tickerSymbol.setCountry(Optional.ofNullable(exchangeByExchangeCode.get(tickerData.exchCode)).map(Exchange::getCountryCode).orElse(null));
                    tickerSymbol.setExchange(Optional.ofNullable(exchangeByExchangeCode.get(tickerData.exchCode)).map(Exchange::getExchangeName).orElse(null));
                    tickerSymbol.setExchangeCountry(Optional.ofNullable(exchangeByExchangeCode.get(tickerData.exchCode)).map(Exchange::getCountryCode).orElse(null));
                    tickerSymbol.setCategory1(tickerData.securityType2);
                    tickerSymbol.setCategory2(tickerData.securityDescription);
                    return tickerSymbol;
                }
            }
        }

        return null;
    }

    private void cacheSearchResult(String code, List<TickerSymbol> list) {
        if (cache.containsKey(code)) {
            return;
        }
        cache.put(code, list);
        if (cache.size() > cacheSize) {
            cache.pollLastEntry();
        }
    }

    private List<TickerSymbol> getPendingResult(String request) {
        var pendingRequest = pendingRequests.get(request);
        if (pendingRequest != null) {
            try {
                return pendingRequest.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Collections.emptyList();
            } catch (ExecutionException e) {
                return Collections.emptyList();
            }
        }

        return Collections.emptyList();
    }

    private PendingRequest getPendingRequest(String request) {
        PendingRequest newPendingRequest = new PendingRequest(request);
        PendingRequest pendingRequest = pendingRequests.computeIfAbsent(request, k -> newPendingRequest);

        if (newPendingRequest != pendingRequest) {
            return newPendingRequest;
        }

        return null;
    }

    private class PendingRequest implements Future<List<TickerSymbol>> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private final String request;

        public PendingRequest(String request) {
            this.request = request;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return latch.getCount() == 0;
        }

        @Override
        public List<TickerSymbol> get() throws InterruptedException, ExecutionException {
            latch.await();
            return cache.get(request);
        }

        @Override
        public List<TickerSymbol> get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
            if (latch.await(timeout, unit)) {
                return cache.get(request);
            } else {
                throw new TimeoutException();
            }
        }

        void done() {
            latch.countDown();
        }
    }

    public static String makePostApiCall(String path, String body, String apiKey) throws Exception {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("User-Agent", USER_AGENT)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (apiKey != null) {
            requestBuilder = requestBuilder.header("X-OPENFIGI-APIKEY", apiKey);
        }

        var request = requestBuilder.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private List<Exchange> getExchangesFromFile() {
        List<Exchange> exchanges = new ArrayList<>();

        try (
                final var inputStream = getClass().getResourceAsStream("/OpenFIGI_Exchange_Codes.csv");
                final var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            var firstLine = reader.readLine();
            String[] headers = toColumns(firstLine);
            if (headers.length != 7) {
                throw new IllegalArgumentException("Expected 7 columns but was " + headers.length + ".");
            }

            var line = reader.readLine();
            while (line != null) {
                var columns = toColumns(line);
                if (columns[0].length() == 2) {
                    var exchange = new Exchange(columns[0].trim(), columns[6].trim(), columns[4].trim());
                    exchanges.add(exchange);
                }
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            var logger = Logger.getLogger(LOGGER);
            logger.severe(e.getMessage());
        }

        return exchanges;
    }

    private static String[] toColumns(String text) {
        return COLUMN_PATTERN.split(text, -1);
    }


    private static class TickerData {
        private String figi;
        private String name;
        private String ticker;
        private String exchCode;
        private String compositeFIGI;
        private String securityType;
        private String marketSector;
        private String shareClassFIGI;
        private String securityType2;
        private String securityDescription;
    }

    private static class Exchange {
        private final String exchangeCode;
        private final String exchangeName;
        private final String countryCode;

        public Exchange(String exchangeCode, String exchangeName, String countryCode) {
            this.exchangeCode = exchangeCode;
            this.exchangeName = exchangeName;
            this.countryCode = countryCode;
        }

        public String getExchangeCode() {
            return exchangeCode;
        }

        public String getExchangeName() {
            return exchangeName;
        }

        public String getCountryCode() {
            return countryCode;
        }
    }
}
