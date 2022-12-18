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

import java.util.Objects;

/**
 * Class representing ticker symbol.
 */
public class TickerSymbol {
    private String symbol;
    private String description;
    private String type;
    private String country;
    private String exchange;
    private String exchangeCountry;
    private String category1;
    private String category2;
    private String category3;
    private String sedol;

    /**
     * Default constructor.
     */
    public TickerSymbol() {

    }

    /**
     * Get symbol name or short name for this tickers.
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Set symbol or short name for this tickers.
     * @param symbol symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Get description of this ticker.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description of this ticker.
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get type of instrument.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Set type of instrument.
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get country.
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country.
     * @param country country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get exchange.
     * @return exchange
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * Set exchange.
     * @param exchange exchange
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    /**
     * Get exchange country.
     * @return exchangeCountry
     */
    public String getExchangeCountry() {
        return exchangeCountry;
    }

    /**
     * Set exchange country.
     * @param exchangeCountry exchange country
     */
    public void setExchangeCountry(String exchangeCountry) {
        this.exchangeCountry = exchangeCountry;
    }

    /**
     * Get category 1.
     * @return category1
     */
    public String getCategory1() {
        return category1;
    }

    /**
     * Set category 1.
     * @param category1 category1
     */
    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    /**
     * Get category 2.
     * @return category2
     */
    public String getCategory2() {
        return category2;
    }

    /**
     * Set category 2.
     * @param category2 category2
     */
    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    /**
     * Get category 3.
     * @return category3
     */
    public String getCategory3() {
        return category3;
    }

    /**
     * Set category 3.
     * @param category3 category3
     */
    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    /**
     * Get SEDOL.
     * @return SEDOL
     */
    public String getSedol() {
        return sedol;
    }

    /**
     * Set SEDOL.
     * @param sedol sedol
     */
    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    /**
     * Comparison method.
     * @param o other ticker to check if equals
     * @return Returns true if this ticker equals to the given ticker. Otherwise returns false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickerSymbol that = (TickerSymbol) o;
        return Objects.equals(getSymbol(), that.getSymbol()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getType(), that.getType()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getExchange(), that.getExchange()) && Objects.equals(getExchangeCountry(), that.getExchangeCountry()) && Objects.equals(getCategory1(), that.getCategory1()) && Objects.equals(getCategory2(), that.getCategory2()) && Objects.equals(getCategory3(), that.getCategory3()) && Objects.equals(getSedol(), that.getSedol());
    }

    /**
     * Calculates hash code for this ticker.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSymbol(), getDescription(), getType(), getCountry(), getExchange(), getExchangeCountry(), getCategory1(), getCategory2(), getCategory3(), getSedol());
    }
}
