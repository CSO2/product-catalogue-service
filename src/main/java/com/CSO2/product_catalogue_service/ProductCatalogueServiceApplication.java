package com.CSO2.product_catalogue_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ProductCatalogueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogueServiceApplication.class, args);
	}

}