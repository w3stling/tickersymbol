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

import com.apptasticsoftware.lei.CusipValidator;
import com.apptasticsoftware.lei.IsinCodeValidator;
import com.apptasticsoftware.lei.SedolValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
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

    public List<TickerSymbol> searchByIdentifier(String identifier) {
        if (!(IsinCodeValidator.isValid(identifier) || CusipValidator.isValid(identifier) || SedolValidator.isValid(identifier))) {
            return Collections.emptyList();
        }

        List<TickerSymbol> list = searchByIdentifiers(identifier);
        if (!list.isEmpty()) {
            return list;
        }

        if (IsinCodeValidator.isValid(identifier)) {
            list = searchByIsin(identifier);
            if (!list.isEmpty()) {
                return list;
            }
        }

        if (CusipValidator.isValid(identifier)) {
            list = searchByCusip(identifier);
            if (!list.isEmpty()) {
                return list;
            }
        }

        if (SedolValidator.isValid(identifier)) {
            list = searchBySedol(identifier);
            if (!list.isEmpty()) {
                return list;
            }
        }

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
            Connection.Response searchForm = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("accept-encoding", "gzip, deflate")
                    .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,sv;q=0.7")
                    .followRedirects(true)
                    .execute();

            Document responseDocument = searchForm.parse();
            Element action = responseDocument.select("input[name=action]").first();
            String actionValue = action != null ? action.attr("value") : "";
            Element version = responseDocument.select("input[name=version]").first();
            String versionValue = version != null ? version.attr("value") : "";

            var response = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("accept-encoding", "gzip, deflate")
                    .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,sv;q=0.7")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("origin", "https://stockmarketmba.com")
                    .header("referer", url)
                    .header("x-requested-with", "XMLHttpRequest")
                    .method(Connection.Method.POST)
                    .data("action", actionValue)
                    .data("version", versionValue)
                    .data("search", identifier)
                    .cookies(searchForm.cookies())
                    .followRedirects(true)
                    .execute();

            var document = response.parse();
            Elements table = document.select("table[id=searchtable]");
            if (table.isEmpty()) {
                table = document.select("table[id=results]");
            }
            Elements rows = table.get(0).select("tr");

            HashMap<String, Integer> headers = new HashMap<>();
            Element headerRow = rows.get(0);
            Elements headerCols = headerRow.select("th");

            for (int i = 0; i < headerCols.size(); ++i) {
                var header = headerCols.get(i).text().toLowerCase().trim();
                headers.put(header, i);
            }

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                TickerSymbol ticker = new TickerSymbol();
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
}
