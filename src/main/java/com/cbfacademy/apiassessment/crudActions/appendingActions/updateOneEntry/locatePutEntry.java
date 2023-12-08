package com.cbfacademy.apiassessment.crudActions.appendingActions.updateOneEntry;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods.NewWatchlistConstructor;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// updates an item in the watchlist after finding its uuid. uses helper method updateExistingEntry.updateOneItem to update the specific entry once it is found.
@Component
public class locatePutEntry {

    private static final Logger log = LoggerFactory.getLogger(locatePutEntry.class);

    @Autowired
    private ObjectMapper mapper;
    // private ReadExistingWatchlist readList;
    private NewWatchlistConstructor newWatchlist;
    
    public locatePutEntry(ObjectMapper mapper, NewWatchlistConstructor newWatchlist) {
        this.mapper = mapper;
        this.mapper = mapper.registerModule(new JavaTimeModule());
        this.newWatchlist = newWatchlist;
    }

    public void locateEntry(List<Watchlist> existingWatchlist, UUID uuid, Watchlist newEntry, MarketData marketData) throws IOException {
        try {
            for(Watchlist watchlistEntry : existingWatchlist) {
            if(watchlistEntry.getUuid().equals(uuid)){
                newWatchlist.updateOneItem(watchlistEntry, newEntry, marketData);
                log.info("Item with uuid" + uuid + "has been updated in watchlist: {}", uuid);
                break;
            } 
        }
        } catch (ItemNotFoundException e) {
            log.info("Item not located in current watchlist");
                throw new ItemNotFoundException("the item you were looking for cannot be located");
        }
        
    }
}