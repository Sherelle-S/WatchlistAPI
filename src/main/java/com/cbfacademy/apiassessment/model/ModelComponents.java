package com.cbfacademy.apiassessment.model;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;

public class ModelComponents {

private static final Logger log = LoggerFactory.getLogger(ModelComponents.class);
@Autowired
private MarketData marketData;
    private Watchlist watchlist;

    public ModelComponents(MarketData marketData, Watchlist watchlist) {
        this.marketData = marketData;
        this.watchlist = watchlist;
    }

    protected void calculateProfit() {
        if (watchlist.getCurrentPrice() != null && watchlist.getPurchasePrice() != null && watchlist.getOwnsVolStock() != null) {
            BigDecimal currentPrice = watchlist.getCurrentPrice();
            BigDecimal purchasePrice = watchlist.getPurchasePrice();
            BigDecimal ownsVolStock = BigDecimal.valueOf(watchlist.getOwnsVolStock());
            watchlist.setProfit(currentPrice.subtract(purchasePrice).multiply(ownsVolStock));
        } else {
            // Handle null values, set profit to 0
            watchlist.setProfit(BigDecimal.ZERO); 
        }
    }
    
    protected BigDecimal calculateCumulativeProfit(){
        if (watchlist.getProfit() != null) {
            watchlist.setCumulativeProfit(watchlist.getCumulativeProfit().add(watchlist.getProfit()) ); // Add the daily profit/loss to cumulative profit
        }
    return calculateCumulativeProfit();
    }

}
