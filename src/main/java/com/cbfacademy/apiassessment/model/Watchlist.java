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
    private double pointsChange;
    private boolean isSold;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateSold;
    private BigDecimal cumulativeDailyProfit;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

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
        this.pointsChange = (double) json.get("pointsChange");
        // this.open = (double) json.get("open");
        // this.close = (double) json.get("close");
        // this.intradayHigh = (double) json.get("intradayHigh");
    }

    public Watchlist(BigDecimal currentPrice, double open, double close, double intradayHigh, double intradayLow,
                ObjectId id, UUID uuid, String stockName, String symbol, String currency, LocalDate datePurchased,
                Integer wantsVolStock, Integer ownsVolStock, BigDecimal purchasePrice, BigDecimal profit,
                double pointsChange, BigDecimal cumulativeDailyProfit,
                boolean isDeleted, LocalDateTime deletedAt) {
            super(currentPrice, open, close, intradayHigh, intradayLow);
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
            this.pointsChange = pointsChange;
            this.cumulativeDailyProfit = cumulativeDailyProfit;
            this.isDeleted = isDeleted;
            this.deletedAt = deletedAt;
        }

        // watchlist constructor for updating watchlist 
        public Watchlist(BigDecimal currentPrice, double open, double close, double intradayHigh, double intradayLow,
            String symbol, String currency, Integer wantsVolStock, Integer ownsVolStock, double pointsChange,
            boolean isSold, LocalDate dateSold, BigDecimal cumulativeDailyProfit) {
        super(currentPrice, open, close, intradayHigh, intradayLow);
        this.symbol = symbol;
        this.currency = currency;
        this.wantsVolStock = wantsVolStock;
        this.ownsVolStock = ownsVolStock;
        this.pointsChange = pointsChange;
        this.isSold = isSold;
        this.dateSold = dateSold;
        this.cumulativeDailyProfit = cumulativeDailyProfit;
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
        calculateProfit();
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
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
        calculateProfit();
    }
   
    public BigDecimal getProfit() {
        return profit;
    }
    
    public void setProfit(BigDecimal profit){
        calculateProfit();
        // BigDecimal currentPrice = getCurrentPrice();
        // BigDecimal purchasePrice = getPurchasePrice();
        // BigDecimal ownsVolStock = BigDecimal.valueOf(getOwnsVolStock());
        // this.profit = currentPrice.subtract(purchasePrice).multiply(ownsVolStock);
        // this.profit = (getCurrentPrice() - getPurchasePrice()) * getOwnsVolStock();
    }

    // calculates profit based on user inputs
    protected void calculateProfit() {
        if (getCurrentPrice() != null && this.purchasePrice != null && this.ownsVolStock != null) {
            BigDecimal currentPrice = getCurrentPrice();
            BigDecimal purchasePrice = this.purchasePrice;
            BigDecimal ownsVolStock = BigDecimal.valueOf(this.ownsVolStock);
            this.profit = currentPrice.subtract(purchasePrice).multiply(ownsVolStock);
        } else {
            // Handle null values, set profit to 0
            this.profit = BigDecimal.ZERO; 
        }
    }
    public double getPointsChange() {
        return pointsChange;
    }

    // logic for setting points change automatically based on user input
    public void setPointsChange(double pointsChange) {
        this.pointsChange = getPrevClose() - getOpen();
    }

    // calculates points change based on input user values
    protected void calculatePointsChange() {
        this.pointsChange = getPrevClose() - getOpen(); 
    }

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

    public BigDecimal getCumulativeDailyProfit() {
        return cumulativeDailyProfit;
    }

    public void setCumulativeDailyProfit(BigDecimal cumulativeDailyProfit) {
        this.cumulativeDailyProfit = cumulativeDailyProfit;
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
                + ", pointsChange=" + pointsChange + ", isSold=" + isSold + ", dateSold=" + dateSold
                + ", cumulativeDailyProfit=" + cumulativeDailyProfit + ", isDeleted=" + isDeleted + ", deletedAt="
                + deletedAt + "]";
    }
}