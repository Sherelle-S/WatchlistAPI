package com.cbfacademy.apiassessment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.controller.WatchlistController;
import com.cbfacademy.apiassessment.exceptions.ExternalAPIResponseUnavailable;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
import com.cbfacademy.apiassessment.model.Watchlist;
@Service
public class MongoListService {

    private static final Logger log = LoggerFactory.getLogger(MongoListService.class);
    @Autowired
    private AlphaVantageConfig alphaVantageConfig;
    @Autowired
    private WatchlistRepository repository;    
    @Autowired
    private WatchlistRepository watchlistRepository;

    public MongoListService(WatchlistRepository repository, AlphaVantageConfig alphaVantageConfig) {
        this.repository = repository;
        this.alphaVantageConfig = alphaVantageConfig;
    }



public void saveWatchlist(Watchlist watchlist) {
    watchlist.setId(null); // Ensure id is not set
    watchlistRepository.save(watchlist);
}

    public void softDeleteWatchlistEntry(UUID uuid) {
        Watchlist watchlist = repository.findById(uuid).orElse(null);
        if (watchlist != null) {
            watchlist.setDeleted(true);
            watchlist.setDeletedAt(LocalDateTime.now());
            repository.save(watchlist);
        }
        // Handle if the document doesn't exist or other errors
    }
    
    public Watchlist updateViaExtApi(Watchlist apiWatchlist, String symbol ){

        try {
            Watchlist updatedWatchlist = alphaVantageConfig.useAlphaVantigeAPI(symbol);

            if (updatedWatchlist != null) {
                apiWatchlist.setOpen(updatedWatchlist.getOpen());
                apiWatchlist.setPrevClose(updatedWatchlist.getPrevClose());
                apiWatchlist.setIntradayHigh(updatedWatchlist.getIntradayHigh());
            } else {
                throw new ExternalAPIResponseUnavailable("unable to update external API response.");
            }
            // Watchlist watchlist = // Fetch the Watchlist from repository using the symbol or any identifier
            // apiWatchlist.setOpen(0);
            // apiWatchlist.setClose(0);
            // apiWatchlist.setIntradayHigh(0);
            return apiWatchlist;
        } catch (Exception e) {
            // Handle exceptions or log errors
            log.error("unable to retrieve data from external API", e);
            return null;
        }
    
        
    }
}
