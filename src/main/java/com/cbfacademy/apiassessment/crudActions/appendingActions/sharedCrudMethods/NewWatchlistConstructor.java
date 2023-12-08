/**
 * The class `NewWatchlistConstructor` is a component in a Java application that provides methods for
 * updating a `Watchlist` object.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

import io.github.mainstringargs.alphavantagescraper.properties.AlphaVantageAPIKey;

@Component
public class NewWatchlistConstructor {
  
   // The code snippet `@Autowired private AlphaVantageConfig alphaVantageConfig;` is using the
   // `@Autowired` annotation to inject an instance of the `AlphaVantageConfig` class into the
   // `NewWatchlistConstructor` class. This allows the `NewWatchlistConstructor` class to access and
   // use the methods and properties of the `AlphaVantageConfig` class.
    @Autowired
    private AlphaVantageConfig alphaVantageConfig;
    private static final Logger log = LoggerFactory.getLogger(NewWatchlistConstructor.class);

    // Updates One existing entry, to be used with the PUT operations.
    // @Autowired
   // The `updateOneItem` method in the `NewWatchlistConstructor` class is a method that updates the
   // properties of an existing `Watchlist` object with the values from a new `Watchlist` object.
    public Watchlist updateOneItem(Watchlist existingEntry, Watchlist newEntry, MarketData marketData){
        existingEntry.setSymbol(newEntry.getSymbol());
        existingEntry.setCurrency(newEntry.getCurrency());
        existingEntry.setOwnsVolStock(newEntry.getOwnsVolStock());
        existingEntry.setWantsVolStock(newEntry.getWantsVolStock());
        existingEntry.setCurrentPrice(newEntry.getCurrentPrice());
        existingEntry.setProfit(newEntry.getProfit());
        existingEntry.setCumulativeProfit(newEntry.getCumulativeProfit());
        existingEntry.setOpen(newEntry.getOpen());
        existingEntry.setPrevClose(newEntry.getPrevClose());
        existingEntry.setIntradayHigh(newEntry.getIntradayHigh());
        log.info("object update one item has been created.");
        return newEntry;
    }
}
