package com.cbfacademy.apiassessment.crudActions.appendingActions.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//  reads jsonRepo and adds the the data into a temporary holder, existingWatchlist
@Component
public class ReadExistingWatchlist {

    private static final Logger log = LoggerFactory.getLogger(ReadExistingWatchlist.class);

    public List<Watchlist> readExistingWatchlist(String jsonRepo, ObjectMapper mapper) throws IOException {

        try {
            mapper.registerModule(new JavaTimeModule());
            log.info("jsonRepo has been read");
            
            var thing = mapper.readValue(new File(jsonRepo), new TypeReference<List<Watchlist>>() {});
            log.info("Watchlist object returned from read Existing list at WriteToJson: {}", thing);
            return thing;
        } catch (JsonProcessingException e) {
            log.error("Failed to read json file.", e.getMessage());
            throw new WatchlistDataAccessException("Processing json request failed.", e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("json File could not be found.", e.getMessage());
            throw new ItemNotFoundException("jsonRepo could not be located.", e.getMessage());
        }   catch (IOException e) {
            log.error("IOException ocurred while trying to read jsonRepo.", e.getMessage());
            throw new IOException("Json repo could not be read.");
        }
    }
}