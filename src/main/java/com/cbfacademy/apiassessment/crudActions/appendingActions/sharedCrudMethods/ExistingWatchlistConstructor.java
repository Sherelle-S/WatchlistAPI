/**
 * The ExistingWatchlistConstructor class is responsible for updating existing entries in a watchlist
 * with new data.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    public Watchlist updateExistingEntry(Watchlist existingEntry, Watchlist newEntry, MarketData marketData){
        existingEntry.setStockName(newEntry.getStockName());
        existingEntry.setSymbol(newEntry.getSymbol());
        existingEntry.setOwnsVolStock(newEntry.getOwnsVolStock());
        existingEntry.setCurrency(newEntry.getCurrency());
        existingEntry.setWantsVolStock(newEntry.getWantsVolStock());
        existingEntry.setCurrentPrice(newEntry.getCurrentPrice());
        existingEntry.setPurchasePrice(newEntry.getPurchasePrice());
        existingEntry.setProfit(newEntry.getProfit());
        existingEntry.setOpen(newEntry.getOpen());
        existingEntry.setPrevClose(newEntry.getPrevClose());
        existingEntry.setIntradayHigh(newEntry.getIntradayHigh());

        //add data from AlphaVantageAPI
        existingEntry.setCurrentPrice(marketData.getCurrentPrice());
        existingEntry.setOpen(marketData.getOpen());
        existingEntry.setPrevClose(marketData.getPrevClose());
        existingEntry.setIntradayHigh(marketData.getIntradayHigh());
        log.info("object with existing data has been created.");
        return existingEntry;
    }
}
