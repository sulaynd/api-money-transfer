package com.loulysoft.moneytransfer.ratings;

import org.springframework.boot.SpringApplication;

public class TestRatingsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RatingsServiceApplication::main).with(ContainersConfig.class).run(args);
	}

}
