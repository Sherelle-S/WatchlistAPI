package com.cbfacademy.apiassessment.crudActions.appendingActions.updateOneEntry;

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

@Component
public class UpdatePutEntry {
    
     private static final Logger log = LoggerFactory.getLogger(UpdatePutEntry.class);
 ;
  
     private NewWatchlistConstructor newWatchlist;
    
    @Autowired
    public UpdatePutEntry(NewWatchlistConstructor newWatchlist) {
        this.newWatchlist = newWatchlist;
    }

    // updates one watchlist entry located by UUID if uuid cannot be found we throw exception
    public void updateEntryViaUuid(List<Watchlist> existingWatchlist, UUID uuid, Watchlist newEntry, MarketData marketData){
        log.info("updatePutEntry is running");
        log.info("Locating watchlist item with UUID: {}, uuid");
        boolean found = false;
        for(Watchlist watchlistEntry : existingWatchlist){
            if(watchlistEntry.getUuid().equals(uuid)){
                newWatchlist.updateOneItem(watchlistEntry, newEntry, marketData);
                log.info("Item with UUID {} has been updated in watchlist.", uuid);
                found = true;
                break;
            } 
        }
        if(!found){
            throw new ItemNotFoundException("The item with the uuid you are looking for cannot be found.");
        }
    }
}
