package com.example.demo.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 검증 설정
 */
@Data
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifyProperties {

	/**
	 * 키 페어 경로
	 */
	@Value("${keyPair.path}")
	protected String path = "./files/";

	/**
	 * 키 페어 생성 알고리즘
	 */
	@Value("${keyPair.algorithm}")
	protected String algorithm = "RSA";

	/**
	 * 키 페어 크기
	 */
	@Value("${keyPair.keySize}")
	protected int keySize = 2048;

	/**
	 * 설정 정보
	 */
	@Getter
	@Setter
	private static VerifyProperties instance = new VerifyProperties();

}
