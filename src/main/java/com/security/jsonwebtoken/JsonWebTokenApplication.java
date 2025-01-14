package com.security.jsonwebtoken;

import com.security.jsonwebtoken.config.VerifyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(VerifyProperties.class)
public class JsonWebTokenApplication {
	public static void main(String[] args) {
		SpringApplication.run(JsonWebTokenApplication.class, args);
	}
}
