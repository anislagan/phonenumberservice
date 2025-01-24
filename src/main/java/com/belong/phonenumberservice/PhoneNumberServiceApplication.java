package com.belong.phonenumberservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = "com.belong.phonenumberservice.model")
public class PhoneNumberServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneNumberServiceApplication.class, args);
	}

	// Future alternative search can be added here e.g. database, elasticsearch etc,
//	@Bean
//	public SearchServiceFactory searchServiceFactory(
//			Optional<FlatFilePhoneNumberService> flatFileService) {
//		return new SearchServiceFactory(
//				flatFileService
//		);
//	}
}