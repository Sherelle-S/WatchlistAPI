/**
 * The Watchlist class is a model that represents the structure of a watchlist for market data,
 * including information such as stock name, symbol, currency, purchase details, profit, and deletion
 * status.
 */
package com.cbfacademy.apiassessment.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

// model that shows the structure for the watchlist
@Document(collection = "Watchlist")
@Component
public class Watchlist extends MarketData{

    @Id
    private ObjectId id;
    private UUID uuid;
    private String stockName;
    private String symbol;
    private String currency;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate datePurchased;
    private Integer wantsVolStock;
    private Integer ownsVolStock;
    private BigDecimal purchasePrice;
    private BigDecimal profit;
    private boolean isSold;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateSold;
    private BigDecimal cumulativeProfit;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    @Field("watchlistModelComponents")
    private ModelComponents modelComponents;
    // private AIAdvice aIAdvice; this will ru a prompt through an AI device that shows this stock data through several iterations and ask it for advice on buying, selling or holding stock for profit.

    // empty watchlist
    public Watchlist() {
    }

    // generating uuid with this constructor and implementing the logic so that it is generated if uuid is null
    // Initializes watchlist object from json data
        public Watchlist(JSONObject json){
            Object uuidObj = json.get("uuid");
            if (uuidObj instanceof String) {
                this.uuid = UUID.fromString((String) uuidObj);
            } else {
                this.uuid = UUID.randomUUID();
            }
        this.stockName = (String) json.get("stockName");
        this.symbol = (String) json.get("symbol");
        this.currency = (String) json.get("currency");
        String datePurchasedStr = (String) json.get("datePurchased");
        this.datePurchased = LocalDate.parse(datePurchasedStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.ownsVolStock = (Integer) json.get("ownsVolStock");
        this.wantsVolStock = (Integer) json.get("wantsVolStock");
        this.profit = (BigDecimal) json.get("profit");
    }
// The code you provided is a constructor for the Watchlist class. It initializes a Watchlist object
// with the given parameters.
// The code you provided is a constructor for the Watchlist class. It initializes a Watchlist object
// with the given parameters.

    public Watchlist(BigDecimal currentPrice, double open, double prevClose, double intradayHigh, double intradayLow,
                ObjectId id, UUID uuid, String stockName, String symbol, String currency, LocalDate datePurchased,
                Integer wantsVolStock, Integer ownsVolStock, BigDecimal purchasePrice, BigDecimal profit,
             BigDecimal cumulativeProfit,
                boolean isDeleted, LocalDateTime deletedAt, ModelComponents modelComponents) {
            super(currentPrice, open, prevClose, intradayHigh, intradayLow);
            this.id = id;
            this.uuid = (uuid == null) ? UUID.randomUUID() : uuid;
            this.stockName = stockName;
            this.symbol = symbol;
            this.currency = currency;
            this.datePurchased = datePurchased;
            this.wantsVolStock = wantsVolStock;
            this.ownsVolStock = ownsVolStock;
            this.purchasePrice = purchasePrice;
            this.profit = profit;
            this.cumulativeProfit = cumulativeProfit;
            this.isDeleted = isDeleted;
            this.deletedAt = deletedAt;
            this.modelComponents = modelComponents;
        }

       // The code you provided is a constructor for the Watchlist class that is used for updating an
       // existing watchlist object. It takes in various parameters such as the current price, open
       // price, previous close price, intraday high price, intraday low price, symbol, currency,
       // volume of stocks wanted, volume of stocks owned, whether the stock is sold or not, date of
       // sale, cumulative profit, and model components.
        // watchlist constructor for updating watchlist 
        public Watchlist(BigDecimal currentPrice, double open, double prevClose, double intradayHigh, double intradayLow,
            String symbol, String currency, Integer wantsVolStock, Integer ownsVolStock,
            boolean isSold, LocalDate dateSold, BigDecimal cumulativeProfit, ModelComponents modelComponents) {
        super(currentPrice, open, prevClose, intradayHigh, intradayLow);
        this.symbol = symbol;
        this.currency = currency;
        this.wantsVolStock = wantsVolStock;
        this.ownsVolStock = ownsVolStock;
        this.isSold = isSold;
        this.dateSold = dateSold;
        this.cumulativeProfit = cumulativeProfit;
        this.modelComponents = modelComponents;
    }


    public ObjectId getId() {
        return id;
    }

   
    public void setId(ObjectId id) {
        this.id = id;
    }

    // getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDate datePurchased) {
        this.datePurchased = datePurchased;
    }

    public Integer getOwnsVolStock() {
        return ownsVolStock;
    }

    public void setOwnsVolStock(Integer has) {
        this.ownsVolStock = has;
        modelComponents.calculateProfit();
    }

    public Integer getWantsVolStock() {
        return wantsVolStock;
    }

    public void setWantsVolStock(Integer wants) {
        this.wantsVolStock = wants;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    
    // sets purchase price and calls calculate profit 
   /**
    * The function sets the purchase price and calculates the profit using the modelComponents object.
    * 
    * @param purchasePrice The purchase price of an item.
    */
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
        modelComponents.calculateProfit();
    }
   
    public BigDecimal getProfit() {
        return profit;
    }
    
    public void setProfit(BigDecimal profit){
        modelComponents.calculateProfit();
    }

    // calculates profit based on user inputs
       
     public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }

    public LocalDate getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDate dateSold) {
        this.dateSold = dateSold;
    }

    public BigDecimal getCumulativeProfit() {
        return cumulativeProfit;
    }

    /**
     * The function sets the cumulative profit and calculates it using the model components.
     * 
     * @param cumulativeProfit The cumulative profit is a BigDecimal value that represents the total
     * profit earned over a period of time.
     */
    public void setCumulativeProfit(BigDecimal cumulativeProfit) {
        modelComponents.calculateCumulativeProfit();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Watchlist [id=" + id + ", uuid=" + uuid + ", stockName=" + stockName + ", symbol=" + symbol
                + ", currency=" + currency + ", datePurchased=" + datePurchased + ", wantsVolStock=" + wantsVolStock
                + ", ownsVolStock=" + ownsVolStock + ", purchasePrice=" + purchasePrice + ", profit=" + profit
                + ", isSold=" + isSold + ", dateSold=" + dateSold + ", cumulativeProfit=" + cumulativeProfit
                + ", isDeleted=" + isDeleted + ", deletedAt=" + deletedAt + ", modelComponents=" + modelComponents
                + "]";
    }

  
}