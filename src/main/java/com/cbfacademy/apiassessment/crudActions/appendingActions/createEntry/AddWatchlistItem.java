/**
 * The AddWatchlistItem class appends new entries to an existing watchlist object and updates the
 * existing entries if they already exist.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cbfacademy.apiassessment.crudActions.appendingActions.sharedCrudMethods.ExistingWatchlistConstructor;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

// File appends data to existing temporary watchlist object, adding the new entry. it then writes the temporary watchlist with all data including the new entry to json if 'WriteToJsonFile' is called.
@Component
public class AddWatchlistItem {

    @Autowired
    private ExistingWatchlistConstructor updateExistingEntry;

    public AddWatchlistItem(ExistingWatchlistConstructor updateExistingEntry) {
        this.updateExistingEntry = updateExistingEntry;
    }
    private static final Logger log = LoggerFactory.getLogger(AddWatchlistItem.class);
    // we iterate through each item of our watchlist checking if we do not already have that uuid we add it to existingWatchlist
    public List<Watchlist> appendToWatchlist(List<Watchlist> watchlist, List<Watchlist> existingWatchlist, MarketData marketData) {
        log.info("watchlist when append to watchlist starts: {}", watchlist);
        for(Watchlist newEntry : watchlist) {
            UUID newEntryUuid = newEntry.getUuid();
            boolean entryExists = false;

            for(Watchlist existingEntry : existingWatchlist){
                UUID existingUuid = existingEntry.getUuid();
                if(existingUuid != null && existingUuid.equals(newEntryUuid)){
                    entryExists = true;
                    updateExistingEntry.updateExistingEntry(existingEntry, newEntry, marketData);
                    log.info("existingWatchlist at if existing uuid addWatchlist: {}", existingWatchlist);
                    log.info("watchlist at existing uuid: {}", watchlist);
                    break;
                }
            }

            if(!entryExists){
                UUID entryUUID = UUID.randomUUID();
                newEntry.setUuid(entryUUID);
                existingWatchlist.add(newEntry);
                log.info("existingWatchlist at !entryExists addWatchlistItem: {}", existingWatchlist);
            }
        }
        log.info("Append new entry to watchlist object.");
        return existingWatchlist;
    }    
}