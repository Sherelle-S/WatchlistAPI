package com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.ReadExistingWatchlist;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Responsible to controlling the components that append a new entry to the json file it reads old watchlist from json, converts it into a new watchlist object, appends an new entry and returns it back to the json file with new entry added.
@Component 
public class RunCreatingActions {

    private static final Logger log = LoggerFactory.getLogger(RunCreatingActions.class);

    @Autowired
    private AddWatchlistItem addEntry;
    private ObjectMapper mapper;
    private WriteToJsonFile writeToJson;
    private ReadExistingWatchlist readList;

    
       public RunCreatingActions(AddWatchlistItem addEntry, ObjectMapper mapper, ReadExistingWatchlist readList, WriteToJsonFile writeToJson) {
        this.addEntry = addEntry;
        this.mapper = mapper;
        this.mapper = mapper.registerModule(new JavaTimeModule());
        this.writeToJson = writeToJson;
        this.readList = readList;
    }

    
    /** 
     * @return ResponseEntity<?>
     */
    // public ResponseEntity<?> appendNewItems(List<Watchlist> watchlist, List<Watchlist> existingWatchlist, String jsonRepo) throws IOException{
        public ResponseEntity<?> appendNewItems(List<Watchlist> watchlist, String jsonRepo, MarketData marketData) throws IOException{
        try {
            List<Watchlist> existingWatchlist = readList.readExistingWatchlist(jsonRepo, mapper);
            List<Watchlist> updatedWatchlist = addEntry.appendToWatchlist(watchlist, existingWatchlist, marketData);
            writeToJson.writeToJson(jsonRepo, mapper, updatedWatchlist);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JacksonException e) {
            log.error("Exception while trying to process json request with jackson", e.getMessage());
            return new ResponseEntity<>("Error processing JSON.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            log.error("Exception occurred while running Appending components to watchlist");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
