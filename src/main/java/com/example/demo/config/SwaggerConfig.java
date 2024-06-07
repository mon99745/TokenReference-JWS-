package com.example.demo.config;

import com.example.demo.controller.JwsRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Slf4j
@Configuration
@EnableOpenApi
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
				.useDefaultResponseMessages(true) // Swagger 에서 제공해주는 기본 응답 코드
				.apiInfo(apiInfo())
				.tags(new Tag(JwsRestController.TAG, "JWS 관리하는 API", 100))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
				.build();

	}

	public ApiInfo apiInfo() {
		log.info("apiInfo");
		return new ApiInfoBuilder()
				.title("SpringBoot Rest API Documentation")
				.description("springboot rest api practice.")
				.version("0.1")
				.build();
	}
}