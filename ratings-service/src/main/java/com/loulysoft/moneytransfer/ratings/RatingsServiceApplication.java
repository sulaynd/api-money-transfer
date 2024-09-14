package com.loulysoft.moneytransfer.ratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RatingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatingsServiceApplication.class, args);
    }
}
