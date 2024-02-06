// package com.cbfacademy.apiassessment.mongoRepository;

// // import java.util.Collections;

// // import org.bson.UuidRepresentation;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
// // import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
// // import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;

// @Configuration
// public class MongoConfig extends AbstractMongoClientConfiguration {

//     @Override
//     public MongoCustomConversions customConversions() {
//         return new MongoCustomConversions(Collections.emptyList());
//     }

//     @Override
//     protected String getDatabaseName() {
//         return "Watchlist";
//     }

//     // @Override
//     @Bean
//     public MongoCustomConversions mongoCustomConversions() {
//         UUIDCodecProvider codecProvider = new UUIDCodecProvider(UuidRepresentation.STANDARD);
//         return new MongoCustomConversions(Collections.singletonList(new MongoConverterConfigurationAdapter(codecProvider)));
//     }
// }