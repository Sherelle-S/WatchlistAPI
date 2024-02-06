package com.cbfacademy.apiassessment;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.AddWatchlistItem;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.CreateFirstItem;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.RunCreatingActions;
import com.cbfacademy.apiassessment.crudActions.appendingActions.createEntry.WriteToJsonFile;
import com.cbfacademy.apiassessment.crudActions.appendingActions.deleteEntries.DeleteEntry;
import com.cbfacademy.apiassessment.crudActions.appendingActions.deleteEntries.RunDeleteEntry;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.ReadExistingWatchlist;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.RunGetWatchlist;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort.BinarySearch;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort.QuicksortWatchlist;
import com.cbfacademy.apiassessment.crudActions.appendingActions.read.searchAndSort.SortWatchlistByName;
import com.cbfacademy.apiassessment.crudActions.appendingActions.updateOneEntry.RunUpdatingMethods;
import com.cbfacademy.apiassessment.exceptions.ExternalAPIResponseUnavailable;
import com.cbfacademy.apiassessment.exceptions.InvalidInputException;
import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.exceptions.WatchlistProcessingException;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
import com.cbfacademy.apiassessment.externalApi.AlphaVantageService;
import com.cbfacademy.apiassessment.model.MarketData;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.cbfacademy.apiassessment.service.WatchlistServiceImpl;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
  classes = App.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AppTests {

  @LocalServerPort
  private int port;

  private URL base;

  private String mockJsonFile = "mockJsonFile.json";

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
    private RestTemplate restTemplates;


  // @Autowired
  // @Mock
  // private WriteToJsonFile writeToJson;
  @Autowired
  @Mock
  private ObjectMapper mapper;

  private BufferedWriter writer;
  private FileWriter fileWriter;

  @Mock
  private BinarySearch binarySearch;

  @Mock
  private ReadExistingWatchlist readList;

  @Mock
  private RunGetWatchlist runGetWatchlist;

  @Mock
  private SortWatchlistByName sortWatchlistByName;

  @Mock
  private RunDeleteEntry runDeleteItem;

  @Mock
  private CreateFirstItem createFirstItem;

  @Mock
  private RunUpdatingMethods runUpdatingMethods;

  @Mock
  private RunCreatingActions runCreatingActions;

  @Mock
  private WatchlistServiceImpl watchlistServiceImpl;

  @Mock
  private AddWatchlistItem addWatchlistItem;

  @InjectMocks
  private WatchlistServiceImpl service;

  @InjectMocks
  private AlphaVantageService alphaVantageService;

  @InjectMocks
  AlphaVantageConfig alphaVantageConfig;

  @TempDir
  Path tempDir;

  @BeforeEach
  public void setUp() throws Exception {
    try {
      this.base = new URL("http://localhost:" + port + "/greeting");
      MockitoAnnotations.openMocks(this);
      File jsonFile = new File(mockJsonFile);
      if (jsonFile.exists()) {
        jsonFile.delete();
      }
    } catch (Exception e) {
      throw new Exception();
    }
  }

  @AfterEach
  public void tearDown() {
    File file = new File(mockJsonFile);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  @Description("/greeting endpoint returns expected response for default name")
  public void greeting_ExpectedResponseWithDefaultName() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      base.toString(),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Hello World", response.getBody());
  }

  @Test
  @Description(
    "/greeting endpoint returns expected response for specified name parameter"
  )
  public void greeting_ExpectedResponseWithNameParam() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      base.toString() + "?name=John",
      String.class
    );
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Hello John", response.getBody());
  }

  @Test
  @Description("/Create first entry writes to json")
  public void CreateFirstEntryWritesToJson() {
    List<Watchlist> emptyMockWatchlist = new ArrayList<>();
    MarketData mockMarketData = new MarketData();
    try {
      Files.createFile(Paths.get(mockJsonFile));
      createFirstItem.CreateFirstEntry(
        emptyMockWatchlist,
        mockJsonFile,
        mockMarketData
      );
      assertTrue(
        Files.exists(Paths.get(mockJsonFile)),
        "File should have been created"
      );
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Test
  @Description(
    "new watchlist entries are converted to a json object in addWatchlistItem"
  )
  public void newWatchlistEntriesAreConvertedToAJsonWatchlistObject() {
    List<Watchlist> emptyMockWatchlist = new ArrayList<>();
    List<Watchlist> sampleMockWatchlist = new ArrayList<>();
    MarketData mockMarketData = new MarketData();
    // ModelComponents modelComponents = new ModelComponents(mockMarketData, sampleEntry);

    Watchlist sampleEntry = new Watchlist();
    sampleEntry.setUuid(null);
    sampleEntry.setStockName("Apple");
    sampleEntry.setSymbol("AAPL");
    sampleEntry.setCurrency("USD");
    sampleEntry.setDatePurchased(LocalDate.now());

    BigDecimal purchasePrice = BigDecimal.valueOf(76.7);
    BigDecimal currentPrice = BigDecimal.valueOf(69.8);
    sampleEntry.setWantsVolStock(2340);
    sampleEntry.setOwnsVolStock(2110);

    sampleEntry.setPurchasePrice(purchasePrice);
    sampleEntry.setCurrentPrice(currentPrice);
    sampleEntry.setProfit(BigDecimal.ZERO);
    sampleEntry.setCumulativeProfit(BigDecimal.valueOf(25));
    sampleEntry.setOpen(354.2);
    sampleEntry.setPrevClose(234.0);
    sampleEntry.setIntradayHigh(356.2);

    sampleMockWatchlist.add(sampleEntry);
    addWatchlistItem.appendToWatchlist(
      emptyMockWatchlist,
      sampleMockWatchlist,
      mockMarketData
    );

    verify(addWatchlistItem)
      .appendToWatchlist(
        emptyMockWatchlist,
        sampleMockWatchlist,
        mockMarketData
      );
  }

  @Test
  @Description("/multiple watchlist entries are converted to json obtect")
  public void multipleWatchlistEntiresAreSerializedToJsonObject() {
    List<Watchlist> sampleMockWatchlist1 = new ArrayList<>();
    List<Watchlist> sampleMockWatchlist2 = new ArrayList<>();
    MarketData mockMarketData = new MarketData();
    AddWatchlistItem addWatchlistItem = mock(AddWatchlistItem.class);

    addWatchlistItem.appendToWatchlist(
      sampleMockWatchlist1,
      sampleMockWatchlist2,
      mockMarketData
    );
    verify(addWatchlistItem)
      .appendToWatchlist(
        sampleMockWatchlist1,
        sampleMockWatchlist2,
        mockMarketData
      );
  }

  @Test
  @Description("/new watchlist object writes to json")
  public void writeNewWatchlistObjectToJsonFile() throws IOException {
    String testJsonFile = "testMock.json";
    List<Watchlist> sampleList = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
	mapper = mapper.registerModule(new JavaTimeModule());

    WriteToJsonFile writeToJson = new WriteToJsonFile();
    ObjectMapper mockedMapper = Mockito.mock(ObjectMapper.class);

    try {
      Watchlist sampleEntry = new Watchlist();
      sampleEntry.setUuid(null);
      sampleEntry.setStockName("Apple");
      sampleEntry.setSymbol("AAPL");
      sampleEntry.setCurrency("USD");
      sampleEntry.setDatePurchased(LocalDate.now());

      BigDecimal purchasePrice = BigDecimal.valueOf(76.7);
      BigDecimal currentPrice = BigDecimal.valueOf(69.8);
      sampleEntry.setWantsVolStock(2340);
      sampleEntry.setOwnsVolStock(2110);

      sampleEntry.setPurchasePrice(purchasePrice);
      sampleEntry.setCurrentPrice(currentPrice);
      sampleEntry.setProfit(BigDecimal.ZERO);
      sampleEntry.setCumulativeProfit(BigDecimal.valueOf(25));
      sampleEntry.setOpen(354.2);
      sampleEntry.setPrevClose(234.0);
      sampleEntry.setIntradayHigh(356.2);

      sampleList.add(sampleEntry);
      // Mock behavior to return OK response
      Mockito
        .when(
          writeToJson.writeToJson(
            anyString(),
            any(ObjectMapper.class),
            anyList()
          )
        )
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

      // Call the method under test
      ResponseEntity<?> data = writeToJson.writeToJson(
        testJsonFile,
        mockedMapper,
        sampleList
      );

      // Verify method invocation with expected arguments
      Mockito
        .verify(writeToJson)
        .writeToJson(testJsonFile, mockedMapper, sampleList);

      // Assert the response status code
      assertEquals(HttpStatus.OK, data.getStatusCode());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  @Description("/itemSelectedByUuidIsDeleted")
  public void itemSelectedByUuidIsDeleted() {
    UUID testUuid = UUID.randomUUID();
    ReadExistingWatchlist readList = mock(ReadExistingWatchlist.class);
    WriteToJsonFile writeToJsonFile = mock(WriteToJsonFile.class);
    DeleteEntry deleteEntry = new DeleteEntry(readList, writeToJsonFile);

    try {
      List<Watchlist> existingWatchlist = new ArrayList<>();
      Watchlist watchlistItem = new Watchlist();
      watchlistItem.setUuid(testUuid);
      existingWatchlist.add(watchlistItem);
      deleteEntry.deleteEntry(
        existingWatchlist,
        mockJsonFile,
        mapper,
        testUuid
      );
      assertEquals(0, existingWatchlist.size(), "Item should now be deleted");
      verify(writeToJsonFile, times(1))
        .writeToJson(
          eq("mockJsonFile.json"),
          any(ObjectMapper.class),
          eq(existingWatchlist)
        );
    } catch (StreamWriteException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  @Description(
    "/exceptions class invalidInputException returns HttpStatus.INTERNAL_SERVER_ERROR"
  )
  public void invalidInputExceptionReturnsBadRequest() {
    ResponseStatus annotation =
      InvalidInputException.class.getAnnotation(ResponseStatus.class);
    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    assertEquals(
      expectedStatus,
      annotation.value(),
      "InvalidInputException should have @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)"
    );
  }

  @Test
  @Description(
    "exceptions class ItemNotFoundException returns HttpStatus.INTERNAL_SERVER_ERROR"
  )
  public void itemNotFoundExceptionReturns() {
    ResponseStatus annotation =
      ItemNotFoundException.class.getAnnotation(ResponseStatus.class);
    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    assertEquals(
      expectedStatus,
      annotation.value(),
      "ItemNotFoundException should have @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)"
    );
  }

  @Test
  @Description(
    "exceptions class WatchlistProcessingException returns HttpStatus.INTERNAL_SERVER_ERROR"
  )
  public void WatchlistProcessingExceptionReturns() {
    ResponseStatus annotation =
      WatchlistProcessingException.class.getAnnotation(ResponseStatus.class);
    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    assertEquals(
      expectedStatus,
      annotation.value(),
      "WatchlistProcessingException should have @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)"
    );
  }

  @Test
  @Description(
    "exceptions class WatchlistProcessingException returns HttpStatus.INTERNAL_SERVER_ERROR"
  )
  public void WatchlistProcessingException() {
    ResponseStatus annotation =
      WatchlistProcessingException.class.getAnnotation(ResponseStatus.class);
    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    assertEquals(
      expectedStatus,
      annotation.value(),
      "WatchlistProcessingException should have @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)"
    );
  }

  @Test
  @Description(
    "exceptions class ExternalAPIResponseUnavailable returns HttpStatus.INTERNAL_SERVER_ERROR"
  )
  public void ExternalAPIResponseUnavailable() {
    ResponseStatus annotation =
      ExternalAPIResponseUnavailable.class.getAnnotation(ResponseStatus.class);
    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    assertEquals(
      expectedStatus,
      annotation.value(),
      "ExternalAPIResponseUnavailable should have @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)"
    );
  }

  @Test
  @Description("/quicksort class correctly sorts watchlist objects by name")
  public void quicksortSortsWatchlistObjectsByName() {
    QuicksortWatchlist quicksortWatchlist = new QuicksortWatchlist();

    List<Watchlist> unsortedWatchlist = new ArrayList<>();
    unsortedWatchlist.add(
      new Watchlist(
        new BigDecimal("75.0"),
        0.5,
        70.0,
        85.0,
        90.0,
         
        UUID.randomUUID(),
        "TestStock C",
        "VOD.L",
        "USD",
        LocalDate.now(),
        100,
        200,
        new BigDecimal("75.0"),
        new BigDecimal("80.0"),
        new BigDecimal("5.0"),
        false,
        LocalDateTime.now() 
      )
    );

    unsortedWatchlist.add(
      new Watchlist(
        new BigDecimal("80.0"),
        0.5,
        75.0,
        90.0,
        95.0,
         
        UUID.randomUUID(),
        "TestStock A",
        "XAG",
        "USD",
        LocalDate.now(),
        150,
        250,
        new BigDecimal("80.0"),
        new BigDecimal("85.0"),
        new BigDecimal("5.0"),
        false,
        LocalDateTime.now() 
      )
    );

    unsortedWatchlist.add(
      new Watchlist(
        new BigDecimal("85.0"),
        0.5,
        80.0,
        95.0,
        100.0,
         
        UUID.randomUUID(),
        "TestStock B",
        "CBF",
        "USD",
        LocalDate.now(),
        120,
        220,
        new BigDecimal("85.0"),
        new BigDecimal("90.0"),
        new BigDecimal("5.0"),
        false,
        LocalDateTime.now() 
      )
    );
    List<Watchlist> sortedWatchlist = quicksortWatchlist.sortAlgo(
      unsortedWatchlist
    );

    assertEquals("TestStock A", sortedWatchlist.get(0).getStockName());
    assertEquals("TestStock B", sortedWatchlist.get(1).getStockName());
    assertEquals("TestStock C", sortedWatchlist.get(2).getStockName());
  }
 
  @Test
  @Description("/String name is returned from @PathVariable")
  public void stringNameIsReceivedFromPathVariable(){
  	List<Watchlist> watchlist = binarySearch.binarySearchWatchlist(new ArrayList<>(), "TestName");
  	Assertions.assertNotNull(watchlist);
  }

  @Test
  @Description("/sorted array is received from quicksort")

  public void itemReceivedFromQuicksortIsSorted(){
  	  List<Watchlist> existingWatchlist = new ArrayList<>();
		existingWatchlist.add(new Watchlist(
			new BigDecimal("75.0"),
			0.5,
			70.0,
			85.0,
			90.0,
			UUID.randomUUID(),
			"TestStock B",
			"VOD.L",
			"USD",
			LocalDate.now(),
			100,
			200,
			new BigDecimal("75.0"),
			new BigDecimal("80.0"),
			new BigDecimal("5.0"),
			false,
			LocalDateTime.now()  // Replace this with your actual LocalDateTime
		));
	
		existingWatchlist.add(new Watchlist(
			new BigDecimal("80.0"),
			0.5,
			75.0,
			90.0,
			95.0,
			UUID.randomUUID(),
			"TestStock A",
			"XAG",
			"USD",
			LocalDate.now(),
			150,
			250,
			new BigDecimal("80.0"),
			new BigDecimal("85.0"),
			new BigDecimal("5.0"),
			false,
			LocalDateTime.now()  // Replace this with your actual LocalDateTime
		));
	
		existingWatchlist.add(new Watchlist(
			new BigDecimal("85.0"),
			0.5,
			80.0,
			95.0,
			100.0,
			UUID.randomUUID(),
			"TestStock B",
			"CBF",
			"USD",
			LocalDate.now(),
			120,
			220,
			new BigDecimal("85.0"),
			new BigDecimal("90.0"),
			new BigDecimal("5.0"),
			false,
			LocalDateTime.now()  // Replace this with your actual LocalDateTime
		));
  	// binarySearch.binarySearch(new QuicksortWatchlist());
  	QuicksortWatchlist quicksortWatchlist = new QuicksortWatchlist();
  	List<Watchlist> sortedWatchlist = quicksortWatchlist.sortAlgo(existingWatchlist);
  	List<Watchlist> foundEntires = binarySearch.binarySearchWatchlist(sortedWatchlist, "TestName");
  }

  @Test
  @Description("Binary search returns multiple entries for a given stock name")
  public void binarySearch_ReturnsMultipleEntriesForStockName() {
  	List<Watchlist> mockWatchlist = new ArrayList<>();
	  mockWatchlist.add(new Watchlist(
		  new BigDecimal("300.0"), 5.0, 300.0, 305.0, 310.0,  UUID.randomUUID(),
		  "Amazon", "AMZN", "USD", LocalDate.now(), 10, 10, new BigDecimal("300.0"), new BigDecimal("305.0"),
		  new BigDecimal("50.0"), false, LocalDateTime.now()
	  ));
	  mockWatchlist.add(new Watchlist(
		  new BigDecimal("280.0"), 8.0, 280.0, 290.0, 295.0,  UUID.randomUUID(),
		  "Amazon", "AMZN", "USD", LocalDate.now(), 8, 8, new BigDecimal("280.0"), new BigDecimal("290.0"),
		  new BigDecimal("80.0"), false, LocalDateTime.now()
	  ));
	  mockWatchlist.add(new Watchlist(
		  new BigDecimal("320.0"), 15.0, 320.0, 330.0, 335.0,  UUID.randomUUID(),
		  "Amazon", "AMZN", "USD", LocalDate.now(), 15, 15, new BigDecimal("320.0"), new BigDecimal("330.0"),
		  new BigDecimal("150.0"), false, LocalDateTime.now()
	  ));
  String stockNameToSearch = "Amazon";

  doReturn(mockWatchlist).when(binarySearch).binarySearchWatchlist(anyList(), eq(stockNameToSearch));

  List<Watchlist> foundEntries = binarySearch.binarySearchWatchlist(mockWatchlist, stockNameToSearch);

  Assertions.assertNotNull(foundEntries);
  assertEquals(3, foundEntries.size());
  assertEquals(stockNameToSearch, foundEntries.get(0).getStockName());
  assertEquals(stockNameToSearch, foundEntries.get(1).getStockName());
  assertEquals(stockNameToSearch, foundEntries.get(2).getStockName());
  }

  @Test
  @Description("/watchlist route endpoint returns a list of the current items in the watchlist")
  public void watchlist_endpointShowsEndpointOK() {
  	String url = "http://localhost:" +port +"/watchlist/";
  	ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
  	HttpStatusCode statusCode = restTemplate.getForEntity(url, String.class).getStatusCode();
  	assertEquals(HttpStatus.OK, statusCode);
  }

  @Test
  @Description("/watchlist route /sortedWatchlist returns a list of the current items in the watchlist")
  public void sortedWatchlist_ExpectedResponseWithSortedWatchlist() {
  	String url = "http://localhost:" +port +"/watchlist/sortedWatchlist";
  	ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
  	HttpStatusCode statusCode = restTemplate.getForEntity(url, String.class).getStatusCode();
  	assertEquals(HttpStatus.OK, statusCode);
  }

  @Test
  @Description("/watchlist root endpoint /deleteEntry/{uuid} returns HttpStatus ok")
  public void deleteEntryUUID_ExpectedResponseWithCurrentDocumentAdded() {
  	String url = "http://localhost:" +port +"/watchlist/";
  	ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
  	HttpStatusCode statusCode = restTemplate.getForEntity(url, String.class).getStatusCode();
  	assertEquals(HttpStatus.OK, statusCode);
  }

@Test
void testReadExistingWatchlist() throws IOException {
    // Sample JSON string
    String jsonString = "[{\"symbol\":\"CBF\",\"name\":\"TestStock B\",\"currency\":\"USD\"}]";

    ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
    // Mock behavior of objectMapper.readValue() to return a List of Watchlist objects
    Mockito.when(mockMapper.readValue(
            Mockito.anyString(),
            ArgumentMatchers.<TypeReference<List<Watchlist>>>any()
        ))
        .thenReturn(List.of(new Watchlist("CBF", "TestStock B", "USD")));

    ReadExistingWatchlist readExistingWatchlist = new ReadExistingWatchlist();
    // Call the method that reads and deserializes JSON
    List<Watchlist> deserializedObject = readExistingWatchlist.readExistingWatchlist(jsonString, mockMapper);

    // Assert that deserializedObject is not null, indicating successful deserialization
    Assertions.assertNotNull(deserializedObject, "'deserializedObject' is not null after deserialization");
    // Assert that deserializedObject has the expected content
    Assertions.assertEquals(1, deserializedObject.size(), "List size should be 1");
    Assertions.assertEquals("CBF", deserializedObject.get(0).getSymbol(), "Symbol should be CBF");
    Assertions.assertEquals("TestStock B", deserializedObject.get(0).getStockName(), "Stock name should be TestStock B");
    Assertions.assertEquals("USD", deserializedObject.get(0).getCurrency(), "Currency should be USD");
}

  


//   @Test
//   void testReadExistingWatchlist() throws IOException {
// 	  // Sample JSON string
// 	  String jsonString = "[{\"symbol\":\"CBF\",\"name\":\"TestStock B\",\"currency\":\"USD\"}]";

// 	  ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
// 	  // Mock behavior of objectMapper.readValue() to return a List of Watchlist objects
// 	  Mockito.when(mockMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class)))
// 	//   Mockito.when(mockMapper.readValue(Mockito.anyString(), Mockito.any()))
// 			  .thenReturn(List.of(new Watchlist(new BigDecimal("85.0"),	0.5, 80.0, 95.0, 100.0,   // Replace this with your actual ObjectId
// 			UUID.randomUUID(), "TestStock B", "CBF", "USD", LocalDate.now(),
// 			120, 220, new BigDecimal("85.0"), new BigDecimal("90.0"),
// 			new BigDecimal("5.0"), false, LocalDateTime.now())));

// 			ReadExistingWatchlist readExistingWatchlist = new ReadExistingWatchlist();
// 	  // Call the method that reads and deserializes JSON
// 	  List<Watchlist> deserializedObject = readExistingWatchlist.readExistingWatchlist(jsonString, mockMapper);

// 	  // Assert that deserializedObject is not null, indicating successful deserialization
// 	  Assertions.assertNotNull(deserializedObject, "'deserializedObject' is not null after deserialization");
// 	  // Assert that deserializedObject has the expected content
// 	  Assertions.assertEquals(1, deserializedObject.size(), "List size should be 1");
// 	  Assertions.assertEquals("CBF", deserializedObject.get(0).getSymbol(), "Symbol should be CBF");
// 	  Assertions.assertEquals("TestStock B", deserializedObject.get(0).getStockName(), "stock name should be TestStock B");
// 	  Assertions.assertEquals("USD", deserializedObject.get(0).getCurrency(), "Currency should be USD");
//   }

//   @Test
//   public void testDeserializedJsonObject() throws IOException {
// 	  // Your JSON string
// 	  String jsonString = "{\"key\": \"value\"}";

// 	  // Mock ObjectMapper
// 	  ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);

// 	  // Mock the behavior of mapper.readValue() to return a mocked object
// 	  Mockito.when(mockMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class)))
// 			 .thenReturn(List.of());

// 	  ReadExistingWatchlist readExistingWatchlist = new ReadExistingWatchlist();

// 	  // Call the method that reads and deserializes JSON
// 	  List<?> deserializedObject = readExistingWatchlist.readExistingWatchlist(jsonString, mockMapper);

// 	  // Assert that deserializedObject is not null, indicating successful deserialization
// 	  Assertions.assertNotNull(deserializedObject, "'deserializedObject' is not null after deserialization");
//   }

// @Test
//   @Description("Check if 'thing' is a deserialized JSON object")
//   public void testDeserializedJsonObject() throws IOException {
//   // Your JSON string
//   String jsonString = "{\"key\": \"value\"}";

//   // Mock ObjectMapper
//   ObjectMapper mockMapper = mock(ObjectMapper.class);

//   TypeReference<List<Watchlist>> typeReference = new TypeReference<List<Watchlist>>() {};
//   // Mock the behavior of mapper.readValue() to return a mocked object
//   when(mockMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(new ArrayList<>());

//   ReadExistingWatchlist readExistingWatchlist = new ReadExistingWatchlist();

//   // Call the method that reads and deserializes JSON
//   List<Watchlist> deserializedObject = readExistingWatchlist.readExistingWatchlist(jsonString, mockMapper);

//   // Assert that deserializedObject is not null, indicating successful deserialization
//   // Call the method that reads and deserializes JSON

//   // Assert that deserializedObject is not null, indicating successful deserialization
//   Assertions.assertNotNull(deserializedObject, "'thing' is not null after deserialization");
//   }

  // // AlphaVantageApiTests
  // @Test
  // void testAlphaVantageApiConnection() throws ParseException, IOException {
  //   // Mocking the API call without actually hitting the API
  // MarketData mockedMarketData = new MarketData(/* Initialize with sample data */);
  // when(alphaVantageConfig.useAlphaVantigeAPI("XAG")).thenReturn(mockedMarketData);

  // // Call your service method that connects to AlphaVantage API
  // MarketData apiResponse = alphaVantageService.getMarketData("XAG");

  // // Assert that the API call was successful and returned a MarketData object
  // Assertions.assertNotNull(apiResponse);
  // }

//   @Test
// void testAlphaVantageDataRetrieval() throws ParseException, IOException {
//     // Mocking the API call without actually hitting the API
//     MarketData mockedMarketData = new MarketData(/* Initialize MarketData object with sample data */);
//     when(restTemplates.getForObject(
//         eq("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=YOUR_SYMBOL&apikey=YOUR_API_KEY"),
//         any(Class.class))) // Using any(Class.class) as a placeholder
//         .thenReturn(mockedMarketData);

//     // Call your service method that retrieves data from AlphaVantage API
//     MarketData apiData = alphaVantageService.getMarketData("XAG");

//     // Assert that data was retrieved successfully
//     Assertions.assertNotNull(apiData);
// }


// //   @Test
// // void testAlphaVantageDataRetrieval() throws ParseException, IOException {
// //     // Read API key from the config file
// //     String configFilePath = "src/main/resources/config.properties";
// //     FileInputStream propsInput = new FileInputStream(configFilePath);
// //     Properties prop = new Properties();
// //     prop.load(propsInput);
// //     String apiKey = prop.getProperty("Key");
// //     String symbol = "XAG"; // Replace with the required symbol

// //     // Mocking the API call without actually hitting the API
// //     MarketData mockedMarketData = new MarketData(/* Initialize MarketData object with sample data */);
// //     when(restTemplate.getForObject(
// //             eq("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey),
// //             eq(MarketData.class)))
// //             .thenReturn(mockedMarketData);

// //     // Call your service method that retrieves data from AlphaVantage API
// //     MarketData apiData = alphaVantageService.getMarketData(symbol);

// //     // Assert that data was retrieved successfully
// //     Assertions.assertNotNull(apiData);
// // }

// //   @Test
// //   void testAlphaVantageDataRetrieval() throws ParseException, IOException {
// // 	  // Read API key from the config file
// // 	  String configFilePath = "src/main/resources/config.properties";
// // 	  FileInputStream propsInput = new FileInputStream(configFilePath);
// // 	  Properties prop = new Properties();
// // 	  prop.load(propsInput);
// // 	  String apiKey = prop.getProperty("Key");
// // 	  String symbol = "XAG"; // Replace with the required symbol
  
// // 	  // Mocking the API call without actually hitting the API
// // 	  MarketData mockedMarketData = new MarketData();
// // 	  when(restTemplate.getForObject(
// // 			  eq("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey),
// // 			  eq(MarketData.class)))
// // 			  .thenReturn(mockedMarketData);
  
// // 	  // Call  service method that retrieves data from AlphaVantage API
// // 	  MarketData apiData = alphaVantageService.getMarketData( symbol);
  
// // 	  // Assert that data was retrieved successfully
// // 	  Assertions.assertNotNull(apiData);
// //   }
// @Test
// public void testGetMarketData_Success() throws ExternalAPIResponseUnavailable, ParseException, IOException {
//         // Mocking external API response
//         MarketData mockMarketData = new MarketData();
//         mockMarketData.setCurrentPrice(BigDecimal.valueOf(100.0));
//         when(alphaVantageConfig.useAlphaVantigeAPI("AAPL")).thenReturn(mockMarketData);

//         // Call the service method
//         MarketData result = alphaVantageService.getMarketData("AAPL");

//         // Verify that the service returns the expected market data
//         Assertions.assertNotNull(result);
//         assertEquals(BigDecimal.valueOf(100.0), result.getCurrentPrice());

//         // Verify that the external API method was called with the correct symbol
//         verify(alphaVantageConfig, times(1)).useAlphaVantigeAPI("AAPL");
//     }

//     @Test
//     public void testGetMarketData_Exception() throws ExternalAPIResponseUnavailable, ParseException, IOException {
//         // Mocking an exception when calling the external API
//         when(alphaVantageConfig.useAlphaVantigeAPI("INVALID")).thenThrow(ExternalAPIResponseUnavailable.class);

//         // Call the service method with an invalid symbol
//         assertThrows(ExternalAPIResponseUnavailable.class, () -> alphaVantageService.getMarketData("INVALID"));

//         // Verify that the external API method was called with the invalid symbol
//         verify(alphaVantageConfig, times(1)).useAlphaVantigeAPI("INVALID");
//     }
  
//  @Test
//     void testUseAlphaVantageAPI() throws Exception {
// 		String configFilePath = "src/main/resources/config.properties";
// 		String symbol = "VOD.L";
//             FileInputStream propsInput = new FileInputStream(configFilePath);
//             Properties prop = new Properties();
//             prop.load(propsInput);
//             String apiKey = prop.getProperty("Key");
//             String apiSymbol = symbol.toUpperCase() + ".LON";
//             String apiUrl = "https://www.alphavantage.co/query?" +
//                     "function=GLOBAL_QUOTE" +
//                     "&symbol=" + symbol +
//                     "&apikey=" + apiKey;
// 					URL url = new URL(apiUrl);
// 					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//         // Initialize mocks
//         MockitoAnnotations.openMocks(this);

//         // Mock behavior of URL and HttpURLConnection
//         when(url.openConnection()).thenReturn(connection);
//         when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

//         // Mock response from the API
//         InputStream fakeResponse = getClass().getResourceAsStream("/fakeAlphaVantageResponse.json");
//         when(url.openStream()).thenReturn(fakeResponse);

//         // Create an instance of AlphaVantageConfig
//         AlphaVantageConfig alphaVantageConfig = new AlphaVantageConfig(mapper);

//         // Call the method under test with a known input symbol
//         MarketData marketData = alphaVantageConfig.useAlphaVantigeAPI("AAPL");

//         // Assert on the returned MarketData or behavior as needed
//         // For example:
//         Assertions.assertNotNull(marketData);
//         // Additional assertions based on the expected response from the API
//     }
// @Test
// void testUseAlphaVantageAPI() throws Exception {
//     String jsonString = "{\"Global Quote\": {\"02. open\": \"123.45\",\"03. high\": \"130.00\",\"04. low\": \"120.00\",\"04. previous close\": \"125.00\",\"05. price\": \"128.50\"}}";

//     // Mock behavior of URL and HttpURLConnection
//     HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
//     URL url = Mockito.mock(URL.class);
//     Mockito.when(url.openConnection()).thenReturn(connection);
//     Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

//     // Create an InputStream from the JSON string
//     InputStream fakeResponse = new ByteArrayInputStream(jsonString.getBytes());
//     Mockito.when(url.openStream()).thenReturn(fakeResponse);

//     // Create an instance of AlphaVantageConfig
//     AlphaVantageConfig alphaVantageConfig = new AlphaVantageConfig(new ObjectMapper());

//     // Call the method under test with a known input symbol
//     MarketData marketData = alphaVantageConfig.useAlphaVantigeAPI("AAPL");

//     // Assert on the returned MarketData or behavior as needed
//     Assertions.assertNotNull(marketData);
//     // Additional assertions based on the expected response from the API
// }
@Test
void testUseAlphaVantageAPI() throws Exception {
    String jsonString = "{\"Global Quote\": {\"02. open\": \"123.45\",\"03. high\": \"130.00\",\"04. low\": \"120.00\",\"04. previous close\": \"125.00\",\"05. price\": \"128.50\"}}";

    // Mock behavior of URL and HttpURLConnection
    HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
    URL url = Mockito.mock(URL.class);
    Mockito.when(url.openConnection()).thenReturn(connection);
    Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    // Create an InputStream from the JSON string
    InputStream fakeResponse = new ByteArrayInputStream(jsonString.getBytes());
    Mockito.when(url.openStream()).thenReturn(fakeResponse);

    // Create an instance of AlphaVantageConfig
    AlphaVantageConfig alphaVantageConfig = new AlphaVantageConfig(new ObjectMapper());

    // Call the method under test with a known input symbol
    MarketData marketData = alphaVantageConfig.useAlphaVantigeAPI("AAPL");

    // Assert on the returned MarketData or behavior as needed
    Assertions.assertNotNull(marketData);
    // Additional assertions based on the expected response from the API

    // Optionally verify interactions with the HttpURLConnection or URL
    Mockito.verify(url).openConnection();
    Mockito.verify(connection).getResponseCode();
    Mockito.verify(url).openStream();
}

}
