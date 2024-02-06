/**
 * The CreateFirstItem class is responsible for creating the first entry in a JSON file by appending
 * components to a watchlist.
 */
package com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.exceptions.WatchlistProcessingException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.mongoRepository.MongoListService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// class creates the first item written to the json file. The first item written to json is done via a different method as it needs to create and initialize and empty Watchlist Arraylist so that it can be reused by other methods later.
@Component
public class CreateFirstItem {
    
    private static final Logger log = LoggerFactory.getLogger(RunCreatingActions.class);
    
    @Autowired
    private AddWatchlistItem addWatchlistItem;
     @Autowired
    private MongoListService mongoListService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private WriteToJsonFile writeToJson;

    @Autowired
    public CreateFirstItem(AddWatchlistItem addWatchlistItem, ObjectMapper mapper, WriteToJsonFile writeToJson) {
        this.addWatchlistItem = addWatchlistItem;
        // registerModule(new JavaTimeModule()) must always be registered at the same time is object mapper or code will be broken due to problem with Java 8 
        this.mapper = mapper;
        this.mapper = mapper.registerModule(new JavaTimeModule());
        this.writeToJson = writeToJson;
    }
// logic for creating entry to json file when watchlist is empty
   /**
    * This function creates the first entry in a watchlist by appending a market data item, saving it
    * to a MongoDB database, and writing it to a JSON file.
    * 
    * @param watchlist A list of Watchlist objects. This is the existing watchlist that you want to add
    * a new entry to.
    * @param jsonRepo The parameter "jsonRepo" is a String that represents the file path or location
    * where the JSON file will be saved or written to.
    * @param marketData The marketData parameter is an object of type MarketData. It likely contains
    * information about a specific market or financial data that is being used in the watchlist.
    */
    public void CreateFirstEntry(List<Watchlist> watchlist, String jsonRepo, MarketData marketData) throws IOException{
        try {
            List<Watchlist> newWatchlistEntry = new ArrayList<>();
            newWatchlistEntry = addWatchlistItem.appendToWatchlist(watchlist, newWatchlistEntry, marketData);
            mongoListService.saveWatchlist(newWatchlistEntry);;
            writeToJson.writeToJson(jsonRepo, mapper, newWatchlistEntry);
        } catch (JacksonException e) {
            log.error("Exception while trying to process json request with jackson", e.getMessage());
            throw new WatchlistProcessingException("Exception ocurred while trying to parse json file.", e.getMessage());
        } catch (IOException e) {
            log.error("Exception occurred while running Appending components to watchlist");
            throw new WatchlistDataAccessException("IOException ocurred while running appendWatchlist method.", e.getMessage());
        }
    }
}
