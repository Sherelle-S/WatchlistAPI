/**
 * The WatchlistServiceImpl class is a service layer in a Java application that handles the business
 * logic for managing a watchlist of stocks.
 */
package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.controller.WatchlistController;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.CreateFirstItem;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.RunCreatingActions;
import com.cbfacademy.apiassessment.crudActions.appendingActions.deleteEntries.RunDeleteEntry;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.RunGetWatchlist;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort.BinarySearch;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort.SortWatchlistByName;
import com.cbfacademy.apiassessment.crudActions.appendingActions.updateOneEntry.RunUpdatingMethods;
import com.cbfacademy.apiassessment.exceptions.InvalidInputException;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.exceptions.WatchlistDataAccessException;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// service layer, holds business logic to controller methods
@Service
@Primary
public class WatchlistServiceImpl implements WatchlistService {

  String jsonRepo = "src\\main\\resources\\JsonWatchlist.json";

  // String jsonRepo = "JsonWatchlist.json";

  @Autowired
  private BinarySearch binarySearch;

  @Autowired
  private CreateFirstItem createFirstItem;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private RunCreatingActions runCreateItem;

  @Autowired
  private RunDeleteEntry deleteEntry;

  @Autowired
  private RunGetWatchlist readList;

  @Autowired
  private RunUpdatingMethods runUpdatingMethods;

  @Autowired
  private SortWatchlistByName sortByName;

  // private UpdatePutEntry updateOneEntry;

  // The `WatchlistServiceImpl` constructor is used to initialize the dependencies required by the
  // `WatchlistServiceImpl` class. It takes in several parameters representing different dependencies
  // and assigns them to the corresponding instance variables of the class. These dependencies are
  // then used by the methods of the class to perform various operations related to managing a
  // watchlist of stocks.
  public WatchlistServiceImpl(
    BinarySearch binarySearch,
    CreateFirstItem createFirstItem,
    RunDeleteEntry deleteEntry,
    ObjectMapper mapper,
    RunCreatingActions runCreateItem,
    RunUpdatingMethods runUpdatingMethods,
    SortWatchlistByName sortByName,
    RunGetWatchlist readList
  ) {
    this.binarySearch = binarySearch;
    this.createFirstItem = createFirstItem;
    this.deleteEntry = deleteEntry;
    this.mapper = mapper.registerModule(new JavaTimeModule());
    this.mapper = mapper;
    this.runCreateItem = runCreateItem;
    this.runUpdatingMethods = runUpdatingMethods;
    this.readList = readList;
    this.sortByName = sortByName;
  }

  // The line `private static final Logger log = LoggerFactory.getLogger(WatchlistController.class);` is
  // creating a logger object named `log` using the `LoggerFactory.getLogger()` method from the
  // `org.slf4j` library. The logger is associated with the `WatchlistController` class, which means that
  // any log messages generated by this logger will be tagged with the name of the `WatchlistController`
  // class.
  private static final Logger log = LoggerFactory.getLogger(
    WatchlistController.class
  );

  // return response entity for creating watchlist, abstracts and isolates access to readExistingWatchlist

  /**
   * The function getCurrentWatchlist() returns the current watchlist by reading it from a JSON
   * repository.
   *
   * @return The method is returning a List of Watchlist objects.
   */
  public List<Watchlist> getCurrentWatchlist() throws IOException {
    List<Watchlist> currentWatchlist = readList.getWatchlist(jsonRepo, mapper);
    return currentWatchlist;
  }

  // checks if watchlist is in its available? If it is not it creates a watchlist for data to be sent to. If it is available and empty it creates a first entry and writes to json. if there is a watchlist already in use with data in it, a different set of methods is used to handle deserializing appending serializing and writing to JSON.
  /**
   * This function creates a new watchlist by either creating a new file and adding the watchlist items,
   * or appending the watchlist items to an existing file.
   *
   * @param watchlist A list of Watchlist objects that need to be created or appended to the existing
   * file.
   * @return The method is returning a ResponseEntity<Void>.
   */
  @Override
  public ResponseEntity<Void> create(
    List<Watchlist> watchlist,
    MarketData marketData
  ) throws WatchlistDataAccessException {
    try {
      File file = new File(jsonRepo);
      if (!file.exists()) {
        file.createNewFile();
        createFirstItem.CreateFirstEntry(watchlist, jsonRepo, marketData);
        return new ResponseEntity<>(HttpStatus.CREATED);
      } else if (file.exists() && file.length() == 0) {
        createFirstItem.CreateFirstEntry(watchlist, jsonRepo, marketData);
        return new ResponseEntity<>(HttpStatus.CREATED);
      } else {
        runCreateItem.appendNewItems(watchlist, jsonRepo, marketData);
        return new ResponseEntity<>(HttpStatus.CREATED);
      }
    } catch (WatchlistDataAccessException e) {
      log.error("Problem ocurred while processing watchlist object", e);
      throw new WatchlistDataAccessException();
    } catch (IOException e) {
      log.error("Error processing file: " + e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // return response entity and watchlist for read requests, comes from a method responsible for retrieving watchlist data for all activities that require a returned deserialized watchlist.
  /**
   * The function reads a watchlist and returns a ResponseEntity containing the watchlist if it is not
   * empty, or a ResponseEntity with no content if the watchlist is empty.
   *
   * @return The method is returning a ResponseEntity object that contains a list of Watchlist
   * objects.
   */
  @Override
  public ResponseEntity<List<Watchlist>> readWatchlist() {
    try {
      List<Watchlist> retrieveWatchlist = getCurrentWatchlist();
      if (retrieveWatchlist.size() <= 0) {
        return (ResponseEntity<List<Watchlist>>) ResponseEntity.noContent();
      } else {
        return ResponseEntity.ok(retrieveWatchlist);
      }
    } catch (IOException e) {
      log.error(
        "Get Watchlist has Triggered IOException in watchlist Service Implementation",
        e.getMessage()
      );
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  // The below code is implementing a method called "sortedWatchlist" that returns a sorted list of
  // watchlist objects. It uses a quicksort algorithm to sort the watchlist by name. The method takes
  // the current watchlist, a JSON repository, and a mapper as parameters. If an IOException occurs
  // during the sorting process, an error message is logged and a response with the status code
  // "EXPECTATION_FAILED" is returned. Otherwise, the sorted watchlist is returned in a response with
  // the status code "OK".
  // logic for returning sorted watchlist passes watchlist object through a quicksort algorithm.
  @Override
  public ResponseEntity<List<Watchlist>> sortedWatchlist()
    throws WatchlistDataAccessException {
    try {
      List<Watchlist> quickSortWatchlist = sortByName.sortedWatchlist(
        getCurrentWatchlist(),
        jsonRepo,
        mapper
      );
      return ResponseEntity.ok(quickSortWatchlist);
    } catch (IOException e) {
      log.error(
        "IOException while attempting to sort watchlist by name.",
        e.getMessage()
      );
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  // The below code is implementing a searchByName method in a service class. It takes a stockName as
  // input and performs a binary search on the watchlist to find any matches. If a match is found, it
  // returns a ResponseEntity with the search result. If the stockName is not found in the watchlist,
  // it returns a not found response. If the stockName is an invalid input, it returns a no content
  // response. If an exception occurs while accessing the JSON data, it returns an internal server
  // error response.
  //passes quicksort watchlist though a binary search algorithm
  @Override
  public ResponseEntity<List<Watchlist>> searchByName(String stockName)
    throws InvalidInputException {
    try {
      log.info("service impl name is " + stockName);
      List<Watchlist> searchResult = binarySearch.binarySearchWatchlist(
        getCurrentWatchlist(),
        stockName
      );
      return ResponseEntity.ok(searchResult);
    } catch (ItemNotFoundException e) {
      log.error(stockName + " not found in existing watchlist", e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (InvalidInputException e) {
      log.error(stockName + " is an invalid input", e.getMessage());
      return ResponseEntity.noContent().build();
    } catch (IOException e) {
      log.error("Exception occurred while accessing json data", e.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }

  // Update Entry is implementing a method called `updateEntry` in a Java class. This method is
  // responsible for updating an entry in a watchlist.
  // passes data over to run updating methods algo responsible for controlling the methods that update one entry. It returns https status a s a result
  @Override
  public ResponseEntity<Void> updateEntry(
    UUID uuid,
    Watchlist newEntry,
    MarketData marketData
  ) {
    try {
      List<Watchlist> existingWatchlist = getCurrentWatchlist();
      try {
        runUpdatingMethods.runUpdatingMethods(
          existingWatchlist,
          jsonRepo,
          newEntry,
          uuid,
          newEntry
        );
      } catch (ParseException e) {
        log.error(
          "Exception ocurred while parsing json data in watchlistServiceImplementation to update PUT request",
          e.getMessage()
        );
        e.printStackTrace();
      }
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (ItemNotFoundException e) {
      log.error(
        "Could not find item in watchlist with corresponding uuid in updateEntry method watchlist service implementation.",
        e.getMessage()
      );
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IOException e) {
      log.error(
        "IOException has been triggered while updating entry in watchlist service implementation.",
        e.getMessage()
      );
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // The method that is responsible for deleting a watchlist entry
  // based on its UUID. It receives the UUID as a parameter and then calls the `runDeleteItem` method
  // from the `deleteEntry` object to perform the deletion.
  // passes data over to run updating methods algo responsible for controlling the methods that delete one entry based on its uuid. It returns https status a s a result
  @Override
  public ResponseEntity<List<Watchlist>> deleteWatchlistEntry(UUID uuid)
    throws IOException {
    List<Watchlist> existingWatchlist = getCurrentWatchlist();
    log.info("delete watchlist has been called.");
    log.info("watchlist at deleteEntry in service {}", existingWatchlist);
    try {
      deleteEntry.runDeleteItem(existingWatchlist, jsonRepo, mapper, uuid);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (InvalidInputException e) {
      log.error("Invalid input received", e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ItemNotFoundException e) {
      log.error("Unable to locate requested item", e.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IOException e) {
      log.error(
        "IOException has taken place in watchlist Service implementation while attempting to run method deleteEntry",
        e.getMessage()
      );
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  // @Override
  // public Watchlist updateWatchlistFromExternalAPI(String symbol) {
  //     try {
  //         Watchlist watchlist = // Fetch the Watchlist from repository using the symbol or any identifier
  //         watchlist.setOpen(/* value from API */);
  //         watchlist.setClose(/* value from API */);
  //         watchlist.setIntradayHigh(/* value from API */);
  //         return watchlist;
  //     } catch (Exception e) {
  //         // Handle exceptions or log errors
  //         return null;
  //     }
  // }
}
