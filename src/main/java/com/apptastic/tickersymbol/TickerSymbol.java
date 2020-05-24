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

import java.util.Objects;


/**
 * Class representing ticker symbol.
 */
public class TickerSymbol {
    private String symbol;
    private String name;
    private String currency;
    private String isin;
    private String mic;
    private String description;
    private Source source;

    /**
     * Default constructor.
     */
    public TickerSymbol() {

    }

    /**
     * Constructor.
     * @param symbol symbol
     * @param name name
     * @param currency currency
     * @param isin isin
     * @param mic MIC
     * @param description description
     * @param source source
     */
    public TickerSymbol(String symbol, String name, String currency, String isin, String mic, String description, Source source) {
        this.symbol = symbol;
        this.name = name;
        this.currency = currency;
        this.isin = isin;
        this.mic = mic;
        this.description = description;
        this.source = source;
    }

    /**
     * Copy constructor.
     * @param o ticker to copy
     */
    public TickerSymbol(TickerSymbol o) {
        symbol = o.symbol;
        name = o.name;
        currency = o.currency;
        isin = o.isin;
        mic = o.mic;
        description = o.description;
        source = o.source;
    }

    /**
     * Symbol name or short name for this tickers.
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Symbol or short name for this tickers.
     * @param symbol symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Name or long name for this ticker.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name or long name for this ticker.
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Currency code in ISO 4217 format for this ticker.
     * @return currency code
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Currency code in ISO 4217 format for this ticker.
     * @param currency currency code
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * International Securities Identification Number (ISIN) for this ticker.
     * @return ISIN code
     */
    public String getIsin() {
        return isin;
    }

    /**
     * International Securities Identification Number (ISIN) for this ticker.
     * @param isin ISIN code
     */
    public void setIsin(String isin) {
        this.isin = isin;
    }

    /**
     * Market Identifier Code (MIC) for this ticker. Can ether be an operational MIC or market segment MIC.
     * @return MIC
     */
    public String getMic() {
        return mic;
    }

    /**
     * Market Identifier Code (MIC) for this ticker. Can ether be an operational MIC or market segment MIC.
     * @param mic MIC
     */
    public void setMic(String mic) {
        this.mic = mic;
    }

    /**
     * Description of this ticker.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description of this ticker.
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The source where the ticker information is fetched from.
     * @return source
     */
    public Source getSource() {
        return source;
    }

    /**
     * The source where the ticker information is fetched from.
     * @param source source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * Comparison method.
     * @param o other ticker to check if equals
     * @return Returns true if this ticker equals to the given ticker. Otherwise returns false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TickerSymbol)) return false;

        TickerSymbol ticker = (TickerSymbol) o;

        return Objects.equals(getSymbol(), ticker.getSymbol()) &&
                Objects.equals(getName(), ticker.getName()) &&
                Objects.equals(getCurrency(), ticker.getCurrency()) &&
                Objects.equals(getIsin(), ticker.getIsin()) &&
                Objects.equals(getMic(), ticker.getMic()) &&
                Objects.equals(getDescription(), ticker.getDescription()) &&
                Objects.equals(getSource(), ticker.getSource());
    }

    /**
     * Calculates hash code for this ticker.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSymbol(), getName(), getCurrency(), getIsin(), getMic(), getDescription(), getSource());
    }
}
