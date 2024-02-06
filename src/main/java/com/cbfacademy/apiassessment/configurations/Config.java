package com.cbfacademy.apiassessment.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.web.client.RestTemplate;

import com.mongodb.client.MongoClients;

@Configuration
public class Config {
    
     @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory (MongoClients.create(), "Watchlist"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
}
