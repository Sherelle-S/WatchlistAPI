/**
 * The `RunUpdatingMethods` class is responsible for running all the actions required to update one
 * entry in a watchlist, including reading the existing watchlist, updating the entry, and writing back
 * to the JSON file.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.updateOneEntry;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.RunCreatingActions;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.WriteToJsonFile;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.ReadExistingWatchlist;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.repository.MongoListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class RunUpdatingMethods {
    
    private static final Logger log = LoggerFactory.getLogger(RunCreatingActions.class);

    @Autowired
    private MongoListService mongoListService;
    // private AddWatchlistItem addEntry;
    private ObjectMapper mapper;
    private ReadExistingWatchlist readList;
    private WriteToJsonFile writeToJson;
    private UpdatePutEntry updateOneEntry;

    // handles the methods that run all actions responsible for making a POST request together in one place
    public RunUpdatingMethods( ObjectMapper mapper, ReadExistingWatchlist readList,
            WriteToJsonFile writeToJson, UpdatePutEntry updateOneEntry) {
        this.mapper = mapper;
        // this must be registered at the same time as mapper to prevent exceptions.
        this.mapper = mapper.registerModule(new JavaTimeModule());
        this.readList = readList;
        this.writeToJson = writeToJson;
        this.updateOneEntry = updateOneEntry;
    }

    // runs all the components for updating one entry of watchlist readsWatchlist updates one entry and writes back to json
    public ResponseEntity<Void> runUpdatingMethods(List<Watchlist> watchlist, String jsonRepo, Watchlist newEntry, UUID uuid, MarketData marketData) throws ParseException{
        try {
            readList.readExistingWatchlist(jsonRepo, mapper);
            log.info("reader is working in run updating method");
            updateOneEntry.updateEntryViaUuid(watchlist, uuid, newEntry, marketData);
            mongoListService.updateWatchlist(newEntry);
            writeToJson.writeToJson(jsonRepo, mapper, watchlist);
            log.info("Watchlist entry has successfully been updated.");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ItemNotFoundException e) {
            log.error("The item that you are looking for could not be located", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e ) {
            log.error("An error has ocurred while trying to update watchlist with your request.", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
