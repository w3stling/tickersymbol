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
package com.apptastic.tickersymbol.provider;

import com.apptastic.tickersymbol.Source;
import com.apptastic.tickersymbol.TickerSymbol;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Ticker provider implementation that fetches ticker information from Nordic Growth Market (NGM).
 * NGM is a small swedish market place.
 */
public class NordicGrowthMarket extends AbstractHttpsConnection implements TickerSymbolProvider {
    private static final String URL = "http://mdweb.ngm.se/MDWebFront/quotes/service";
    private static final String HTTP_POST_BODY = "7|0|6|http://turing.ngm.se/MDWebFront/quotes/|F30BB935BF060E376C44B6B85A44C503|se.ngm.mdweb.front.client.rpc.SearchRPCService|getCompletionResult|com.google.gwt.user.client.ui.SuggestOracle$Request/3707347745|{\"query\":\"%1$s\", \"segment\":[\"NMTF:MST\",\"XNGM:AIFS\",\"XNGM:EQST\",\"NMTF:MOS\",\"NMTF:EDEX\",\"XNGM:DNCE\",\"XNGM:DNKO\",\"XNGM:DDEX\",\"XNGM:DSKO\",\"XNGM:DDCE\",\"XNGM:DFCE\",\"NMTF:EFKO\",\"NMTF:ETPF\",\"NMTF:EDKO\",\"NMTF:ESCE\",\"NMTF:ESKO\",\"NMTF:EDPV\",\"NMTF:ETPN\",\"NMTF:ENCE\",\"XNGM:DSPV\",\"NMTF:EDCE\",\"NMTF:ENKO\",\"NMTF:ESPV\",\"NMTF:EFEX\",\"NMTF:ENPV\",\"XNGM:DFEX\",\"NMTF:ESEX\",\"XNGM:DDKO\",\"NMTF:EFCE\",\"NMTF:ETPS\",\"XNGM:DDPV\",\"XNGM:DNEX\",\"XNGM:DSEX\",\"XNGM:DFPV\",\"XNGM:DSCE\",\"NMTF:EFPV\",\"XNGM:DFKO\",\"XNGM:DNPV\",\"NMTF:ENEX\",\"NMTF:ETPD\",\"NMTF:IPMD\",\"NMTF:IPMF\",\"NMTF:IPMN\",\"XNGM:DSBO\",\"NMTF:IPMS\",\"XNGM:DDMP\",\"XNGM:DNBO\",\"NMTF:IPMDMP\",\"XNGM:DNMP\",\"NMTF:IPMSMP\",\"XNGM:DSSP\",\"XNGM:DDSP\",\"NMTF:IPMDSP\",\"NMTF:IPMNMP\",\"XNGM:DSMP\",\"XNGM:DFBO\",\"NMTF:IPMNSP\",\"XNGM:DFSP\",\"NMTF:IPMFMP\",\"XNGM:DNSP\",\"NMTF:IPMSSP\",\"XNGM:DFMP\",\"NMTF:IPMFSP\",\"NMTF:MDSM\",\"XNGM:MDSS\",\"NMTF:CBSM\",\"XNGM:CBOS\",\"XNGM:DEBS\"], \"timeStamp\":\"%2$d\", \"putCall\":\"\", \"category\":[], \"subCategory\":[], \"issuer\":[], \"arranger\":[], \"instrumentTypes\":[], \"fromEndDate\":\"\", \"toEndDate\":\"\", \"firstTradedDate\":\"0\"}|1|2|3|4|1|5|5|15|6|";

    /**
     * Search ticker by name.
     * @param name name.
     * @return stream of tickers
     */
    @Override
    public List<TickerSymbol> searchByName(String name) {
        return Collections.emptyList();
    }

    /**
     * Search ticker by ISIN code.
     * @param isin ISIN code.
     * @return stream of tickers
     * @throws IOException IO exception
     */
    public List<TickerSymbol> searchByIsin(String isin) throws IOException {
        String postBody =  String.format(HTTP_POST_BODY, isin, System.currentTimeMillis());

        try (BufferedReader reader = sendRequest(URL, postBody.getBytes(), "UTF-8")) {
            return handleResponse(reader, isin);
        }
    }

    private List<TickerSymbol> handleResponse(BufferedReader reader, String isin) throws IOException {
        List<TickerSymbol> tickers = new ArrayList<>();
        TickerSymbol ticker = new TickerSymbol();
        ticker.setIsin(isin);
        ticker.setCurrency("SEK");
        ticker.setMic("XNGM");
        ticker.setSource(Source.NORDIC_GROWTH_MARKET);
        ticker.setDescription("");

        String line = reader.readLine();
        String[] column = line.split(",");
        int nofColumns = column.length;

        if (nofColumns - 3 > 0) {
            String shortName = column[nofColumns - 3];
            shortName = shortName.substring(1, shortName.length() - 2);
            int index = shortName.indexOf(' ');

            if (index != -1)
                shortName = shortName.substring(0, index);

            ticker.setSymbol(shortName.trim());
        }

        if (nofColumns - 4 > 0) {
            String longName = column[nofColumns - 4];
            int index = longName.indexOf('-');

            if (index != -1) {
                longName = longName.substring(index + 1, longName.length() - 1);
                ticker.setName(longName.trim());
            }
        }

        if (ticker.getSymbol() != null && ticker.getName() != null)
            tickers.add(ticker);

        return tickers;
    }

    @Override
    protected void setRequestHeaders(HttpRequest.Builder requestBuilder) {
        super.setRequestHeaders(requestBuilder);

        requestBuilder.header("Accept", "*/*");
        requestBuilder.header("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,sv;q=0.7");
        requestBuilder.header("Content-Type", "text/x-gwt-rpc; charset=UTF-8");
        requestBuilder.header("X-GWT-Module-Base", "http://turing.ngm.se/MDWebFront/quotes/");
        requestBuilder.header("X-GWT-Permutation", "165556F18402C15B9C4A62A43EBB19D1");
    }

}
