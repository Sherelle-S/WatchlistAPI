package com.cbfacademy.apiassessment.controller.tempHold;
// package com.cbfacademy.apiassessment.model;

// import com.cbfacademy.apiassessment.externalApi.AlphaVantageConfig;
// import java.math.BigDecimal;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// @Component
// public class ModelComponents implements ModelComponentInterface{
// // public class ModelComponents {
//   private static final Logger log = LoggerFactory.getLogger(
//     ModelComponents.class
//   );

// //   @Autowired
// //   private MarketData marketData;
// //    @Autowired
//   private Watchlist watchlist;

//    @Autowired
//   public ModelComponents(Watchlist watchlist) {
//     this.watchlist = watchlist;
//   }

//   public ModelComponents(){};

// //   public void calculateProfit() {
// //     if (
// //       watchlist.getCurrentPrice() != null &&
// //       watchlist.getPurchasePrice() != null &&
// //       watchlist.getOwnsVolStock() != null
// //     ) {
// //       BigDecimal currentPrice = watchlist.getCurrentPrice();
// //       BigDecimal purchasePrice = watchlist.getPurchasePrice();
// //       BigDecimal ownsVolStock = BigDecimal.valueOf(watchlist.getOwnsVolStock());
// //       watchlist.setProfit(
// //         currentPrice.subtract(purchasePrice).multiply(ownsVolStock)
// //       );
// //     } else {
// //       // Handle null values, set profit to 0
// //       watchlist.setProfit(BigDecimal.ZERO);
// //     }
// //   }

// //   public BigDecimal calculateCumulativeProfit() {
// //     BigDecimal currentProfit = watchlist.getProfit();
// //     if (currentProfit != null && watchlist.getCumulativeProfit() != null) {
// //         watchlist.setCumulativeProfit(watchlist.getCumulativeProfit().add(currentProfit));
// //     } else if (currentProfit != null) {
// //         watchlist.setCumulativeProfit(currentProfit);
// //     }
// //     return watchlist.getCumulativeProfit();
// // }
// }
