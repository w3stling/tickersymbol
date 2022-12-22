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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Class for searching ticker symbols.
 */
public class TickerSymbolSearch {
    private static final String LOGGER = "com.apptasticsoftware.tickersymbol";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
    private static final String SEARCH_BY_IDENTIFIERS_URL = "https://stockmarketmba.com/symbollookupusingidentifier.php";
    private static final String SEARCH_BY_ISIN_URL = "https://stockmarketmba.com/lookupisinonopenfigi.php";
    private static final String SEARCH_BY_CUSIP_URL = "https://stockmarketmba.com/lookupcusiponopenfigi.php";
    private static final String SEARCH_BY_SEDOL_URL = "https://stockmarketmba.com/lookupsedolonopenfigi.php";
    private final int cacheSize;
    private final ConcurrentSkipListMap<String, List<TickerSymbol>> cache;
    private final ConcurrentHashMap<String, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    private static TickerSymbolSearch instance;
    private final Session session = new Session();
    private static final SSLSocketFactory SOCKET_FACTORY = socketFactory();

    TickerSymbolSearch(int cacheSize) {
        this.cacheSize = cacheSize;
        cache = new ConcurrentSkipListMap<>();
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
        return getTickerSymbols(SEARCH_BY_IDENTIFIERS_URL, identifier);
    }

    List<TickerSymbol> searchByIsin(String isin) {
        return getTickerSymbols(SEARCH_BY_ISIN_URL, isin);
    }

    List<TickerSymbol> searchByCusip(String cusip) {
        return getTickerSymbols(SEARCH_BY_CUSIP_URL, cusip);
    }

    List<TickerSymbol> searchBySedol(String sedol) {
        return getTickerSymbols(SEARCH_BY_SEDOL_URL, sedol);
    }

    private List<TickerSymbol> getTickerSymbols(String url, String identifier) {
        List<TickerSymbol> list = new ArrayList<>();

        try {
            if (session.hasExpired()) {
                Connection.Response searchForm = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .ignoreHttpErrors(true)
                        .sslSocketFactory(SOCKET_FACTORY)
                        .userAgent(USER_AGENT)
                        .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("accept-encoding", "gzip, deflate")
                        .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,sv;q=0.7")
                        .followRedirects(true)
                        .execute();

                var responseDocument = searchForm.parse();
                var actionElement = responseDocument.select("input[name=action]").first();
                var action = actionElement != null ? actionElement.attr("value") : "";
                var versionElement = responseDocument.select("input[name=version]").first();
                var version = versionElement != null ? versionElement.attr("value") : "";
                session.refresh(action, version, searchForm.cookies());
            }

            var response = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .ignoreHttpErrors(true)
                    .sslSocketFactory(SOCKET_FACTORY)
                    .userAgent(USER_AGENT)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("accept-encoding", "gzip, deflate")
                    .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,sv;q=0.7")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("origin", "https://stockmarketmba.com")
                    .header("referer", url)
                    .header("x-requested-with", "XMLHttpRequest")
                    .data("action", session.getAction())
                    .data("version", session.getVersion())
                    .data("search", identifier)
                    .cookies(session.getCookies())
                    .followRedirects(true)
                    .execute();

            session.incrementRequestCount();

            var document = response.parse();
            var table = document.select("table[id=searchtable]");
            if (table.isEmpty()) {
                table = document.select("table[id=results]");
            }

            if (table.isEmpty()) {
                return list; // identifier not found
            }

            var rows = table.get(0).select("tr");

            HashMap<String, Integer> headers = new HashMap<>();
            var headerRow = rows.get(0);
            var headerCols = headerRow.select("th");

            for (int i = 0; i < headerCols.size(); ++i) {
                var header = headerCols.get(i).text().toLowerCase().trim();
                headers.put(header, i);
            }

            for (int i = 1; i < rows.size(); i++) {
                var row = rows.get(i);
                var cols = row.select("td");

                var ticker = new TickerSymbol();
                ticker.setSymbol(getColumnValue(cols, headers, "symbol"));
                ticker.setDescription(getColumnValue(cols, headers, "description"));
                ticker.setType(getColumnValue(cols, headers, "type"));
                ticker.setCountry(getColumnValue(cols, headers, "country"));
                ticker.setExchange(getColumnValue(cols, headers, "exchange"));
                ticker.setExchangeCountry(getColumnValue(cols, headers, "exchange country"));
                ticker.setCategory1(getColumnValue(cols, headers, "category1"));
                ticker.setCategory2(getColumnValue(cols, headers, "category2"));
                ticker.setCategory3(getColumnValue(cols, headers, "category3"));
                ticker.setSedol(getColumnValue(cols, headers, "sedol"));
                list.add(ticker);
            }
        } catch (Exception e) {
            session.reset();
            var logger = Logger.getLogger(LOGGER);
            logger.severe(String.format("Search failed. URL: %s, identifier: %s, message: %s", url, identifier, e.getMessage()));
        }

        return list;
    }

    private String getColumnValue(Elements cols, Map<String, Integer> headers, String name) {
        Integer index = headers.get(name);
        if (index == null || index > cols.size()) {
            return null;
        }

        var text = cols.get(index).text().trim();
        if (text.isEmpty()) {
            text = null;
        }

        return text;
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

    static class Session {
        private String action;
        private String version;
        private long timestamp;
        private int maxRequestCount = 50;
        private final AtomicInteger requestCount = new AtomicInteger(0);
        private Map<String, String> cookies;
        private boolean reset;

        public String getAction() {
            return action;
        }

        public String getVersion() {
            return version;
        }

        public void incrementRequestCount() {
            requestCount.incrementAndGet();
        }

        public boolean hasExpired() {
            long now = System.currentTimeMillis();
            return reset || (requestCount.get() % maxRequestCount) == 0 || TimeUnit.MILLISECONDS.toMinutes(now - timestamp) > 9;
        }

        public void refresh(String action, String version, Map<String, String> cookies) {
            this.action = action;
            this.version = version;
            this.cookies = cookies;
            timestamp = System.currentTimeMillis();
            reset = false;
            var r = new SecureRandom();
            maxRequestCount = r.nextInt(100-75) + 75;
        }

        public void reset() {
            reset = true;
        }

        public Map<String, String> getCookies() {
            return cookies;
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

    private static SSLSocketFactory socketFactory() {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            final X509TrustManager origTrustManager = (X509TrustManager)trustManagers[0];
            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return origTrustManager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustManager.checkClientTrusted(certs, authType);
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            // Trust expired certificates
                            try {
                                origTrustManager.checkServerTrusted(certs, authType);
                            } catch (CertificateException e) {
                                if (e.getCause() instanceof CertPathValidatorException) {
                                    Throwable t = e.getCause();
                                    CertPathValidatorException.Reason reason = ((CertPathValidatorException) t).getReason();
                                    if (CertPathValidatorException.BasicReason.EXPIRED.equals(reason)) {
                                        return;
                                    }
                                }
                                throw e;
                            }
                        }
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, wrappedTrustManagers, null);
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            return (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
    }
}
