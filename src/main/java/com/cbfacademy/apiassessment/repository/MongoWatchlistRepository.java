package com.cbfacademy.apiassessment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

@Repository
public interface MongoWatchlistRepository extends MongoRepository<Watchlist, UUID> {
        // public Watchlist updateWatchlistFromExternalAPI(String symbol);

        List<Watchlist> findByStockName(String stockName);

        Optional<Watchlist> findById(ObjectId id);

        // void softDeleteWatchlistEntry(UUID uuid) throws WatchlistDataAccessException;
        void deleteByUuid(UUID uuid) throws WatchlistDataAccessException ;

}
