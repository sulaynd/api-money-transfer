package com.loulysoft.moneytransfer.accounting;

import org.springframework.boot.SpringApplication;

public class TestAccountingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AccountingServiceApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
