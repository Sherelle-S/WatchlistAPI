package com.cbfacademy.apiassessment.repository;

import com.cbfacademy.apiassessment.model.MarketData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends MongoRepository<MarketData, String> {
  // Additional custom methods if needed
}
