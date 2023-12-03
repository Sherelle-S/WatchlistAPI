package com.cbfacademy.apiassessment.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbfacademy.apiassessment.exceptions.InvalidInputException;
import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.repository.MongoWatchlistRepository;
import com.cbfacademy.apiassessment.service.WatchlistService;

// contains the controllers for CRUD request, maps them to the correct endpoint and gets Http responses.
@RestController
@RequestMapping("/watchlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WatchlistController {
    
    
    private static final Logger log = LoggerFactory.getLogger(WatchlistController.class);
    
    @Autowired
    private WatchlistService service;
    @Autowired
    private MongoWatchlistRepository repo;

    public WatchlistController(WatchlistService service) {
        this.service = service;
    }

    // shows all watchlist data 
    @GetMapping(value = "/")
    public ResponseEntity<List<Watchlist>> readWatchlist() throws WatchlistDataAccessException, ParseException {
        repo.findAll();
        return service.readWatchlist();
    }

    // returns watchlist data sorted by name
    @GetMapping(value = "/sortedWatchlist")
    public ResponseEntity<List<Watchlist>> sortedWatchlist() throws WatchlistDataAccessException, ParseException {
        repo.findAll();
        return service.sortedWatchlist();
    }

    //search watchlist by stockName
    @GetMapping(value = "/searchName/{name}")
    public ResponseEntity<List<Watchlist>> searchByName(@PathVariable String stockName) throws InvalidInputException{
        log.info("controller name is" + stockName );
        List<Watchlist> result = repo.findByStockName(stockName);
        return service.searchByName(stockName);
    }

    // Creates new watchlist item on route 
    @PostMapping(value = "/addEntry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <Void> create(@RequestBody List<Watchlist> watchlist) throws WatchlistDataAccessException{
        repo.saveAll(watchlist);
        return service.create(watchlist);      
        // create some logic that means if client already has stock of item of x name in watchlist, they cannot add another item of that stock they must instead update.
    }

    // maps to put routing searching by uuid 
    @PutMapping(value = "/updateEntry/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <Void> updateEntry(@PathVariable UUID uuid, @RequestBody Watchlist newEntry){
        repo.save(newEntry);
        return service.updateEntry(uuid, newEntry);
    }

    // maps to delete routing
    @DeleteMapping(value = "/deleteEntry/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Watchlist>> deleteWatchlistEntry(@PathVariable UUID uuid) throws IOException{
        log.info("Delete process has begun");
        repo.deleteById(uuid);
        return service.deleteWatchlistEntry(uuid);
    }

    @GetMapping("/watchlistM")
    public List<Watchlist> getAllWatchlist(){
        return repo.findAll();
    }

    @PostMapping("/addEntryM")
    public Watchlist addWatchlist(@RequestBody Watchlist watchlist){
        return repo.save(watchlist);
    }
}
    
