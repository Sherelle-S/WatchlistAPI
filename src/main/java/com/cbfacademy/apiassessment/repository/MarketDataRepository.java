package com.cbfacademy.apiassessment.repository;

import com.cbfacademy.apiassessment.model.MarketData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarketDataRepository extends MongoRepository<MarketData, String> {
  // Additional custom methods if needed
}
