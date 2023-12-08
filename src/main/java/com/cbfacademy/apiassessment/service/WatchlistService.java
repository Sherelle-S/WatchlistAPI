package com.cbfacademy.apiassessment.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.exceptions.InvalidInputException;
import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

// interface that tells watchlist service what methods it needs to implement but not how to implement them.
public interface WatchlistService {

   // These are method signatures of the methods that need to be implemented in the `WatchlistService`
   // interface.
    ResponseEntity<Void> create(List<Watchlist> watchlist, MarketData marketData) throws WatchlistDataAccessException;

    ResponseEntity<List<Watchlist>> readWatchlist() throws WatchlistDataAccessException;

    ResponseEntity<List<Watchlist>> sortedWatchlist() throws WatchlistDataAccessException;

    ResponseEntity<List<Watchlist>> searchByName(String stockName) throws InvalidInputException;

    ResponseEntity<Void> updateEntry(UUID uuid, Watchlist newEntry);

    ResponseEntity<List<Watchlist>> deleteWatchlistEntry(UUID uuid) throws IOException;


} 
