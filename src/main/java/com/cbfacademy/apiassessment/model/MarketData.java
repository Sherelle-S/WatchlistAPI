package com.cbfacademy.apiassessment.model;

import java.math.BigDecimal;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

// Store watchlist inside of mongoDB watchlist collection
@Document(collection = "Watchlist")
@Component
public class MarketData {

    private BigDecimal currentPrice; 
    private double open;
    private double prevClose;
    private double intradayHigh;
    private double intradayLow;
    private ModelComponents modelComponents;

    public MarketData() {
    }

    public MarketData(BigDecimal currentPrice, double open, double close, double intradayHigh, double intradayLow) {
        this.currentPrice = currentPrice; //if we use adjusted close price as current price
        this.open = open;
        this.prevClose = close;
        this.intradayHigh = intradayHigh;
        this.intradayLow = intradayLow;
    }

    public MarketData(JSONObject json){
        this.open = (double) json.get("open");
        this.currentPrice = (BigDecimal) json.get("currentPrice");
        this.prevClose = (double) json.get("prevClose");
        this.intradayHigh = (double) json.get("intradayHigh");
        this.intradayLow = (double) json.get("intradayLow");
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    // sets current price and calls calculate profit 
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice; //if we use adjusted close price as current price
        modelComponents.calculateProfit();
    }

    public double getOpen() {
        return open;
    }

    // sets stockMarket open price and calls calculate points change 
    public void setOpen(double open) {
        this.open = open;
    }
    public double getPrevClose() {
        return prevClose;
    }

    // sets stockMarket closing price from user input and calculates points change
    public void setPrevClose(double close) {
        this.prevClose = close;
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
        return "MarketData [currentPrice=" + currentPrice + ", open=" + open + ", close=" + prevClose + ", intradayHigh="
                + intradayHigh + ", intradayLow=" + intradayLow + ", modelComponents=" + modelComponents + "]";
    }
    
}
