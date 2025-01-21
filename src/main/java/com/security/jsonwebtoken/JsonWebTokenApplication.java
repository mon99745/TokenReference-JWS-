package com.security.jsonwebtoken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JsonWebTokenApplication {
	public static void main(String[] args) {
		SpringApplication.run(JsonWebTokenApplication.class, args);
	}
}
