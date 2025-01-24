package com.belong.phonenumberservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.belong.phonenumberservice.model")
public class PhoneNumberServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneNumberServiceApplication.class, args);
	}
}