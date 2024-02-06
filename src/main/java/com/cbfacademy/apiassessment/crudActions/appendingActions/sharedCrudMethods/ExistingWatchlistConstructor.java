/**
 * The ExistingWatchlistConstructor class is responsible for updating existing entries in a watchlist
 * with new data.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.exceptions.ExternalAPIResponseUnavailable;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageService;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

@Component
public class ExistingWatchlistConstructor {

    // update multiple entries for when we add a new item to the watchlist in a POST request
    // @Autowired
    
    // The code you provided is a method called `updateExistingEntry` in the
    // `ExistingWatchlistConstructor` class. This method is responsible for updating the existing entry
    // in a watchlist with new data.
    private static final Logger log = LoggerFactory.getLogger(ExistingWatchlistConstructor.class);

    @Autowired
    private AlphaVantageService alphaService;
    @Autowired
    Watchlist watchlist;
    @Autowired
    MarketData marketData;

    public Watchlist updateExistingEntry(Watchlist existingEntry, Watchlist newEntry, MarketData marketData){
               
        existingEntry.setStockName(newEntry.getStockName());
        existingEntry.setSymbol(newEntry.getSymbol());
        existingEntry.setOwnsVolStock(newEntry.getOwnsVolStock());
        existingEntry.setCurrency(newEntry.getCurrency());
        existingEntry.setWantsVolStock(newEntry.getWantsVolStock());
        existingEntry.setPurchasePrice(newEntry.getPurchasePrice());
        existingEntry.setDatePurchased(newEntry.getDatePurchased());
        existingEntry.setProfit(newEntry.getProfit());
        existingEntry.setCumulativeProfit(newEntry.getCumulativeProfit());
        //add data from AlphaVantageAPI
        // The code block is checking if the `marketData` object is not null. If it is not null, it
        // means that new data has been retrieved from the Alpha Vantage API. In that case, the code
        // updates the `existingEntry` object with the new data by setting the current price, open
        // price, previous close price, and intraday high price.
        if(marketData != null){
          
            try {
                alphaService.getMarketData(watchlist.getSymbol());
                 existingEntry.setCurrentPrice(marketData.getCurrentPrice());
            existingEntry.setOpen(marketData.getOpen());
            existingEntry.setPrevClose(marketData.getPrevClose());
            existingEntry.setIntradayHigh(marketData.getIntradayHigh());
            log.info("object with existing data has been created.");
            } catch (ExternalAPIResponseUnavailable e) {
                log.error("Unable to retrieve data from AlphaVantageAPI.", e.getMessage());
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
                log.error("Problem ocurred while attempting to retrieve data from AlphaVanage API", e.getMessage());
            }
        
        } else {
            throw new ExternalAPIResponseUnavailable("unable to retrieve new data from Alpha Vantage API");
        }
        
       
        return existingEntry;
    }
}
