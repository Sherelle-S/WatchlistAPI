package com.cbfacademy.apiassessment.repository;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

@Repository
public interface WatchlistRepository extends MongoRepository<Watchlist, UUID> {
        // public Watchlist updateWatchlistFromExternalAPI(String symbol);

        List<Watchlist> findByStockName(String stockName);

//     // ResponseEntity<Void> create(List<Watchlist> watchlist) throws WatchlistDataAccessException;

//     // ResponseEntity<List<Watchlist>> readWatchlist() throws WatchlistDataAccessException;

//     // ResponseEntity<List<Watchlist>> sortedWatchlist() throws WatchlistDataAccessException;

//     // ResponseEntity<List<Watchlist>> searchByName(String name) throws InvalidInputException;

//     // ResponseEntity<Void> updateEntry(UUID uuid, Watchlist newEntry);

//     // ResponseEntity<List<Watchlist>> deleteWatchlistEntry(UUID uuid) throws IOException;
    
// @Autowired
// private MongoTemplate mongoTemplate;

// public void saveWatchlist(Watchlist watchlist) {
//     mongoTemplate.save(watchlist);
//     // Don't manually set UUID or ID here
// }

// MarketData marketData = new MarketData();
// // Set properties for marketData
// marketDataRepository.save(marketData);

// Watchlist watchlist = new Watchlist();
// // Set properties for watchlist
// watchlistRepository.save(watchlist);
}
