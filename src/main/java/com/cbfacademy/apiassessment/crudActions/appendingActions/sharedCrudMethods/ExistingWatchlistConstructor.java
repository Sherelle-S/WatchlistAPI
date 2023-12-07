package com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.model.Watchlist;

@Component
public class ExistingWatchlistConstructor {

    // update multiple entries for when we add a new item to the watchlist in a POST request
    // @Autowired
    private static final Logger log = LoggerFactory.getLogger(ExistingWatchlistConstructor.class);
    public Watchlist updateExistingEntry(Watchlist existingEntry, Watchlist newEntry){
        existingEntry.setStockName(newEntry.getStockName());
        existingEntry.setSymbol(newEntry.getSymbol());
        existingEntry.setOwnsVolStock(newEntry.getOwnsVolStock());
        existingEntry.setCurrency(newEntry.getCurrency());
        existingEntry.setWantsVolStock(newEntry.getWantsVolStock());
        existingEntry.setCurrentPrice(newEntry.getCurrentPrice());
        existingEntry.setPurchasePrice(newEntry.getPurchasePrice());
        existingEntry.setProfit(newEntry.getProfit());
        existingEntry.setPointsChange(newEntry.getPointsChange());
        existingEntry.setOpen(newEntry.getOpen());
        existingEntry.setPrevClose(newEntry.getPrevClose());
        existingEntry.setIntradayHigh(newEntry.getIntradayHigh());
        log.info("object with existing data has been created.");
        return existingEntry;
    }
}
