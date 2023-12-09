package com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

    // handles serializing java watchlist objects and writing them to the jsonRepo file. 
@Component
public class WriteToJsonFile {

    private static final Logger log = LoggerFactory.getLogger(WriteToJsonFile.class);

    public ResponseEntity<?> writeToJson(String jsonRepo, ObjectMapper mapper, List<Watchlist> existingWatchlist) throws StreamWriteException, IOException {
        try {
            log.info("existingWatchlist at Write to json: {}", existingWatchlist);
            mapper.registerModule(new JavaTimeModule());
            mapper.writeValue(new File(jsonRepo), existingWatchlist);
            log.info("watchlist object has been update and written back to jsonRepo.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StreamWriteException e) {
            log.error("Stream writer has thrown and exceptions while updating and writing new entry", e.getMessage());
            return new ResponseEntity<>("StreamWriting exception caused while writing to json file.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            log.error("IOException triggered while updating and writing new watchlist.", e.getMessage());
            return new ResponseEntity<>("IOException caused while writing to json file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
