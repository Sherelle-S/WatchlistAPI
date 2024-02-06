package com.cbfacademy.apiassessment.crudActions.appendingActions.deleteEntries;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.WriteToJsonFile;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.ReadExistingWatchlist;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.databind.ObjectMapper;

 // checks if item has the same UUID as the one in question and removes it from database.
@Component
public class DeleteEntry {

    private static final Logger log = LoggerFactory.getLogger(DeleteEntry.class);

    @Autowired
    public ReadExistingWatchlist readList;
    @Autowired
    public WriteToJsonFile writeToJson;

    public DeleteEntry(ReadExistingWatchlist readList, WriteToJsonFile writeToJson) {
        this.readList = readList;
        this.writeToJson = writeToJson;
    }

   
        protected void deleteWatchlistItem(List<Watchlist> existingWatchlist, UUID uuid){
            Iterator<Watchlist> iterator = existingWatchlist.iterator();
            while(iterator.hasNext()){
                Watchlist watchlistEntry = iterator.next();
                if(watchlistEntry.getUuid().equals(uuid)){
                    iterator.remove();
                }
            }
            log.info("item with the UUID of " + uuid + " has been located.");
        }
  
        // deletes watchlist entry and update json file.
    public List<Watchlist> deleteEntry(List<Watchlist> existingWatchlist, String jsonRepo, ObjectMapper mapper, UUID uuid){
        try {
            deleteWatchlistItem(existingWatchlist, uuid);
            writeToJson.writeToJson(jsonRepo, mapper, existingWatchlist);
        } catch (ItemNotFoundException e) {
            log.error("Jackson Exception has occurred in while trying to delete watchlist entry.", e.getMessage());
        } catch (IOException e) {
            log.error("IOException has ocurred while trying to delete entry", e.getMessage());
        }
        return existingWatchlist;
    }
}
