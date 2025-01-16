package com.security.jsonwebtoken.common;

import com.security.jsonwebtoken.controller.TokenRestController;
import com.security.jsonwebtoken.controller.KeyPairRestController;
import com.security.jsonwebtoken.controller.SignDocRestController;
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
				.tags(
						new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),new Tag(KeyPairRestController.TAG, "키쌍(Key-Pair) 관리 API", 100),
						new Tag(TokenRestController.TAG, "토큰(JWT) 관리 API", 200)
//						new Tag(SignDocRestController.TAG, "서명 문서(Signature Document) 관리 API", 300)
						)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.security.jsonwebtoken.controller"))
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