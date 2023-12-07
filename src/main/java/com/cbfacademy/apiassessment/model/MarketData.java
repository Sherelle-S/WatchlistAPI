package com.cbfacademy.apiassessment.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

// Store watchlist inside of mongoDB watchlist collection
@Document(collection = "Watchlist")
@Component
public class MarketData {

    private BigDecimal currentPrice; 
    private double open;
    private double close;
    private double intradayHigh;
    private double intradayLow;
    private Watchlist watchlist;

    public MarketData() {
    }

    public MarketData(BigDecimal currentPrice, double open, double close, double intradayHigh, double intradayLow) {
        this.currentPrice = currentPrice;
        this.open = open;
        this.close = close;
        this.intradayHigh = intradayHigh;
        this.intradayLow = intradayLow;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getOpen() {
        return open;
    }

    // sets stockMarket open price and calls calculate points change 
    public void setOpen(double open) {
        this.open = open;
        watchlist.calculatePointsChange();
    }
    public double getClose() {
        return close;
    }

    // sets stockMarket closing price from user input and calculates points change
    public void setClose(double close) {
        this.close = close;
        watchlist.calculatePointsChange();
    }

    public double getIntradayHigh() {
        return intradayHigh;
    }

    public void setIntradayHigh(double intradayHigh) {
        this.intradayHigh = intradayHigh;
    }

    public double getIntradayLow() {
        return intradayLow;
    }

    public void setIntradayLow(double intradayLow) {
        this.intradayLow = intradayLow;
    }

    @Override
    public String toString() {
        return "MarketData [currentPrice=" + currentPrice + ", open=" + open + ", close=" + close + ", intradayHigh="
                + intradayHigh + "]";
    }
}
