package com.loulysoft.moneytransfer.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// Role : https://coderanch.com/t/773812/frameworks/JPA-Hibernate-Failed-Lazily-Initialize
@SpringBootApplication
@ConfigurationPropertiesScan
public class AccountingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountingServiceApplication.class, args);
    }
}
