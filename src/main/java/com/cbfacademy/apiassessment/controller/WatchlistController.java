package com.cbfacademy.apiassessment.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.mongoRepository.MongoWatchlistRepository;
import com.cbfacademy.apiassessment.service.WatchlistService;

// contains the controllers for CRUD request, maps them to the correct endpoint and gets Http responses.
/**
 * The WatchlistController class is a Java class that handles requests related to the
 * watchlist feature and allows cross-origin requests from http://localhost:3000.
 */
@RestController
@RequestMapping("/watchlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WatchlistController {
    
    // The line `private static final Logger log = LoggerFactory.getLogger(WatchlistController.class);`
    // is creating a logger object named `log` for the `WatchlistController` class. This logger is used
    // to log messages and events during the execution of the code. It is part of the logging framework
    // provided by SLF4J (Simple Logging Facade for Java) and is used to record information about the
    // application's behavior, errors, and other relevant events.
    private static final Logger log = LoggerFactory.getLogger(WatchlistController.class);
    
   // The `@Autowired` annotation is used in Spring to automatically wire (inject) dependencies into a
   // class. In this case, it is injecting instances of `WatchlistService` and
   // `MongoWatchlistRepository` into the `WatchlistController` class.
    @Autowired
    private WatchlistService service;
    @Autowired
    @Qualifier("mongoWatchlistRepository")
    private MongoWatchlistRepository repo;

   // The code `public WatchlistController(WatchlistService service) { this.service = service; }` is a
   // constructor for the `WatchlistController` class. It takes an instance of `WatchlistService` as a
   // parameter and assigns it to the `service` field of the `WatchlistController` class. This allows
   // the `WatchlistController` class to have access to the methods and functionality provided by the
   // `WatchlistService` class.
    public WatchlistController(WatchlistService service) {
        this.service = service;
    }

   // The code `@GetMapping(value = "/")` is a Spring annotation that maps the method to the root
   // endpoint ("/") of the API.
    // shows all watchlist data 
    @GetMapping(value = "/")
    public ResponseEntity<List<Watchlist>> readWatchlist() throws WatchlistDataAccessException, ParseException {
        repo.findAll();
        return service.readWatchlist();
    }

    // returns watchlist data sorted by name
  /**
   * This function retrieves a sorted list of watchlist items from the repository and returns it as a
   * response entity.
   * 
   * @return The method is returning a ResponseEntity object that contains a list of Watchlist objects.
   */
    @GetMapping(value = "/sortedWatchlist")
    public ResponseEntity<List<Watchlist>> sortedWatchlist() throws WatchlistDataAccessException, ParseException {
        repo.findAll();
        return service.sortedWatchlist();
    }

    //search watchlist by stockName
  /**
   * This Java function is a controller method that searches for watchlist items by stock name and
   * returns a list of matching items.
   * 
   * @param stockName The stock name that is being searched for in the watchlist.
   * @return The method is returning a ResponseEntity object containing a List of Watchlist objects.
   */
    @GetMapping(value = "/searchName/{stockName}")
    public ResponseEntity<List<Watchlist>> searchByName(@PathVariable String stockName) throws InvalidInputException{
        log.info("controller name is" + stockName );
        // List<Watchlist> result = repo.findByStockName(stockName);
        return service.searchByName(stockName);
    }

    // Creates new watchlist item on route 
   /**
    * This function handles the creation of new entries in a watchlist, but needs to implement logic to
    * prevent duplicate entries.
    * 
    * @param watchlist A list of Watchlist objects that will be saved to the repository.
    * @return The method is returning a ResponseEntity with a Void type.
    */
    @PostMapping(value = "/addEntry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <Void> create(@RequestBody List<Watchlist> watchlist, MarketData marketData) throws WatchlistDataAccessException{
        
        repo.saveAll(watchlist);
        return service.create(watchlist, marketData);      
        // create some logic that means if client already has stock of item of x name in watchlist, they cannot add another item of that stock they must instead update.
    }

 // The code you provided is a method in the `WatchlistController` class that handles the PUT request
 // for updating a watchlist entry.
    // maps to put routing searching by uuid 
    @PutMapping(value = "/updateEntry/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <Void> updateEntry(@PathVariable UUID uuid, @RequestBody Watchlist newEntry){
        repo.save(newEntry);
        return service.updateEntry(uuid, newEntry, newEntry);
    }

 // The code you provided is a method in the `WatchlistController` class that handles the DELETE
 // request for deleting a watchlist entry.
    // maps to delete routing
    @DeleteMapping(value = "/deleteEntry/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Watchlist>> deleteWatchlistEntry(@PathVariable UUID uuid) throws IOException{
        log.info("Delete process has begun");
        repo.deleteByUuid(uuid);
        return service.deleteWatchlistEntry(uuid);
    }

  /**
   * The above function is a Java method that retrieves all the records from a watchlist table in a
   * database and returns them as a list.
   * 
   * @return The getAllWatchlist method is returning a List of Watchlist objects.
   */
    // @GetMapping("/watchlistM")
    // public List<Watchlist> getAllWatchlist(){
    //     return repo.findAll();
    // }

  /**
   * This function adds a new entry to the watchlist by saving it in the repository.
   * 
   * @param watchlist The parameter "watchlist" is an object of the class "Watchlist". It is annotated
   * with "@RequestBody" which means that the object will be deserialized from the request body. This
   * allows you to send a JSON representation of a Watchlist object in the request body, and it will be
   * automatically
   * @return The method is returning a Watchlist object.
   */
    // @PostMapping("/addEntryM")
    // public Watchlist addWatchlist(@RequestBody Watchlist watchlist){
    //     return repo.save(watchlist);
    // }
}
    
