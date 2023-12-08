/**
 * The class `NewWatchlistConstructor` is a component in a Java application that contains a method for
 * updating a single item in a watchlist.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
import com.cbfacademy.apiassessment.model.Watchlist;

import io.github.mainstringargs.alphavantagescraper.properties.AlphaVantageAPIKey;

@Component
public class NewWatchlistConstructor {
  
    @Autowired
    private AlphaVantageConfig alphaVantageConfig;
    private static final Logger log = LoggerFactory.getLogger(NewWatchlistConstructor.class);

    // Updates One existing entry, to be used with the PUT operations.
    // @Autowired
    /**
     * The function updates the values of an existing Watchlist item with the values from a new
     * Watchlist item and returns the updated item.
     * 
     * @param existingEntry The existing entry in the watchlist that needs to be updated.
     * @param newEntry The newEntry parameter is an object of the Watchlist class that contains the
     * updated information for a specific item in the watchlist.
     * @param apiWatchlist The `apiWatchlist` parameter is an instance of the `Watchlist` class that
     * represents the watchlist data obtained from an API.
     * @return The method is returning the updated Watchlist object, which is represented by the
     * variable "newEntry".
     */
    public Watchlist updateOneItem(Watchlist existingEntry, Watchlist newEntry, Watchlist apiWatchlist){
        apiWatchlist.setOpen(newEntry.getOpen());
        apiWatchlist.setPrevClose(newEntry.getPrevClose());
        apiWatchlist.setIntradayHigh(newEntry.getIntradayHigh());
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
        log.info("object update one item has been created.");
        return newEntry;
    }
}
