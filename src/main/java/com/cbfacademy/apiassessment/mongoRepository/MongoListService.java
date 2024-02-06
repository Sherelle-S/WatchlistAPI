package com.cbfacademy.apiassessment.mongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.exceptions.ExternalAPIResponseUnavailable;
import com.cbfacademy.apiassessment.exceptions.InvalidInputException;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageService;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;

@Service
public class MongoListService implements MongoWatchlistRepository{

    private static final Logger log = LoggerFactory.getLogger(MongoListService.class);
    @Autowired
    private AlphaVantageConfig alphaVantageConfig;
    @Autowired
    private MongoWatchlistRepository repository;
    // @Autowired
    // private AlphaVantageService alphaService;
    

    public MongoListService(AlphaVantageConfig alphaVantageConfig) {
        this.alphaVantageConfig = alphaVantageConfig;
    }




/**
 * The function saves a watchlist object to a repository, ensuring that the id is not set.
 * 
 * @param watchlist The watchlist parameter is an object of the Watchlist class.
 */
public void saveWatchlist(List<Watchlist> watchlist) {
    // watchlist.setId(null); // Ensure id is not set
    repository.saveAll(watchlist);
}

// @Override
// public void createWatchlist (Watchlist watchlist, MarketData marketData) throws InvalidInputException {
//     if (watchlist.getUuid() != null) {
//         Optional<Watchlist> newWatchlistOptional = repository.findById(watchlist.getUuid());

//         if (newWatchlistOptional.isPresent()) {
//             Watchlist newWatchlist = newWatchlistOptional.get();
            
//             // Update fields that need to be changed
//             newWatchlist.setSymbol(watchlist.getSymbol());
//             newWatchlist.setCurrency(watchlist.getCurrency());
//             newWatchlist.setOwnsVolStock(watchlist.getOwnsVolStock());
//             newWatchlist.setWantsVolStock(watchlist.getWantsVolStock());
//             newWatchlist.setCurrentPrice(watchlist.getCurrentPrice());
//             newWatchlist.setProfit(watchlist.getProfit());
//             newWatchlist.setCumulativeProfit(watchlist.getCumulativeProfit());
                     
//             try {
//                 alphaService.getMarketData(watchlist.getSymbol());
//                  newWatchlist.setCurrentPrice(marketData.getCurrentPrice());
//             newWatchlist.setOpen(marketData.getOpen());
//             newWatchlist.setPrevClose(marketData.getPrevClose());
//             newWatchlist.setIntradayHigh(marketData.getIntradayHigh());
//             log.info("object with existing data has been created.");
//             } catch (ExternalAPIResponseUnavailable e) {
//                 log.error("Unable to retrieve data from AlphaVantageAPI.", e.getMessage());
//                 e.printStackTrace();
//             } catch (org.json.simple.parser.ParseException e) {
//                 e.printStackTrace();
//                 log.error("Problem ocurred while attempting to retrieve data from AlphaVanage API", e.getMessage());
//             }
            
//             repository.save(newWatchlist);
//         } else {
//             log.error("Watchlist item with ID {} not found", watchlist.getUuid());
//             throw new ItemNotFoundException("Entry with the id that you are trying to update cannot be located.");
//         }
//     } else {
//         log.error("Invalid Watchlist ID provided");
//         throw new InvalidInputException("You have entered an invalid input.");
//     }
// }

public void updateWatchlist(Watchlist watchlist) throws InvalidInputException {
    if (watchlist.getId() != null) {
        Optional<Watchlist> existingWatchlistOptional = repository.findById(watchlist.getUuid());

        if (existingWatchlistOptional.isPresent()) {
            Watchlist existingWatchlist = existingWatchlistOptional.get();
            
            // Update fields that need to be changed
            existingWatchlist.setSymbol(watchlist.getSymbol());
            existingWatchlist.setCurrency(watchlist.getCurrency());
            existingWatchlist.setOwnsVolStock(watchlist.getOwnsVolStock());
            existingWatchlist.setWantsVolStock(watchlist.getWantsVolStock());
            existingWatchlist.setCurrentPrice(watchlist.getCurrentPrice());
            existingWatchlist.setProfit(watchlist.getProfit());
            existingWatchlist.setCumulativeProfit(watchlist.getCumulativeProfit());
            existingWatchlist.setOpen(watchlist.getOpen());
            existingWatchlist.setPrevClose(watchlist.getPrevClose());
            existingWatchlist.setIntradayHigh(watchlist.getIntradayHigh());
            
            repository.save(existingWatchlist);
        } else {
            log.error("Watchlist item with ID {} not found", watchlist.getId());
            throw new ItemNotFoundException("Entry with the id that you are trying to update cannot be located.");
        }
    } else {
        log.error("Invalid Watchlist ID provided");
        throw new InvalidInputException("You have entered an invalid input.");
    }
}


    public void deleteByUuid(UUID uuid) throws WatchlistDataAccessException {
        Watchlist watchlist = repository.findById(uuid).orElse(null);
        if (watchlist != null) {
            watchlist.setDeleted(true);
            watchlist.setDeletedAt(LocalDateTime.now());
            repository.save(watchlist);
        }else {
            throw new WatchlistDataAccessException("Watchlist entry not available.");
        }
        // Handle if the document doesn't exist or other errors
    }
    
    public MarketData updateViaExtApi(MarketData apiWatchlist, String symbol ){

        try {
            MarketData updatedWatchlist = alphaVantageConfig.useAlphaVantigeAPI(symbol);

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

    @Override
    public <S extends Watchlist> S insert(S entity) {
        return repository.save(entity);
    }

    @Override
    public <S extends Watchlist> List<S> insert(Iterable<S> entities) {
        return repository.saveAll(entities);
    }
    
    @Override
    public <S extends Watchlist> List<S> findAll(Example<S> example) {
        return repository.findAll(example);
    }
    
    @Override
    public <S extends Watchlist> List<S> findAll(Example<S> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    @Override
    public <S extends Watchlist> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }
    
    @Override
    public List<Watchlist> findAll() {
        return repository.findAll();
    }
    
    @Override
    public List<Watchlist> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public <S extends Watchlist> S save(S entity) {
       return repository.save(entity);
    }
    
    @Override
    public Optional<Watchlist> findById(ObjectId id) {
        return repository.findById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
    
    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
    
    @Override
    public void delete(Watchlist entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> ids) {
        repository.deleteAllById(ids);
    }
    
    @Override
    public void deleteAll(Iterable<? extends Watchlist> entities) {
        repository.deleteAll(entities);
    }
    
    @Override
    public void deleteAll() {
       repository.deleteAll();
    }
    
    @Override
    public List<Watchlist> findAll(Sort sort) {
       return repository.findAll(sort);
    }
    
    @Override
    public Page<Watchlist> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public <S extends Watchlist> Optional<S> findOne(Example<S> example) {
        return repository.findOne(example);
    }

    @Override
    public <S extends Watchlist> Page<S> findAll(Example<S> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }
    
    @Override
    public <S extends Watchlist> long count(Example<S> example) {
        return repository.count(example);
    }
    
    @Override
    public <S extends Watchlist> boolean exists(Example<S> example) {
        return repository.exists(example);
    }
    
    @Override
    public <S extends Watchlist, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return repository.findBy(example, queryFunction);
    }
    
    @Override
    public List<Watchlist> findByStockName(String stockName) {
        return repository.findByStockName(stockName);
    }
    
    @Override
    public Optional<Watchlist> findById(UUID id) {
        return repository.findById(id);
    }
    
}
