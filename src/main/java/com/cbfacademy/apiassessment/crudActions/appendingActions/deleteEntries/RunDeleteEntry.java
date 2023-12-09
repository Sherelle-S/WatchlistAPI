package com.cbfacademy.apiassessment.crudActions.appendingActions.deleteEntries;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.repository.MongoListService;
import com.fasterxml.jackson.databind.ObjectMapper;

// runs combined components necessary to delete an entry to watchlist
@Service
public class RunDeleteEntry {
    
    private static final Logger log = LoggerFactory.getLogger(RunDeleteEntry.class);

    @Autowired
    private DeleteEntry deleteEntry;
    @Autowired
    MongoListService mongoListService;

    @Autowired
    public RunDeleteEntry(DeleteEntry deleteEntry) {
        this.deleteEntry = deleteEntry;
    }

    public List<Watchlist> runDeleteItem(List<Watchlist> existingWatchlist, String jsonRepo, ObjectMapper mapper, UUID uuid) throws IOException{
        try {
            log.info("Existing list has been read");
            mongoListService.deleteByUuid(uuid);
            deleteEntry.deleteEntry(existingWatchlist, jsonRepo, mapper, uuid);
            log.info("Item by uuid " + uuid + " has been located");
            log.info("new watchlist is now {}", existingWatchlist);
            return existingWatchlist;
        } catch (ItemNotFoundException e) {
            log.error("UUID that you are trying to delete cannot be found,");
            throw new IOException("Failed to delete item in json file");
        }

    }
}
