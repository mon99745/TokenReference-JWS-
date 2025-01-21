package com.security.jsonwebtoken.controller;

import com.security.jsonwebtoken.message.CreateTokenResponse;
import com.security.jsonwebtoken.message.ExtractClaimResponse;
import com.security.jsonwebtoken.message.VerifyTokenResponse;
import com.security.jsonwebtoken.service.TokenSerivce;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Api(tags = TokenRestController.TAG)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(TokenRestController.PATH)
public class TokenRestController {
	public static final String TAG = "JWT Manager API";
	public static final String PATH = "/api/v1";
	private static final String JWT_FIELD_NAME = "jwt";
	protected final TokenSerivce tokenSerivce;


	/**
	 * 1. 토큰 발행
	 *
	 * @param requestClaim to include in JWT
	 * @return CreateTokenResponse
	 */
	@PostMapping("createToken")
	@Operation(summary = "1. 토큰(JWT) 발행")
	public CreateTokenResponse createToken(@RequestBody Map<String, String> requestClaim) {
		log.info("Request Claim : ", requestClaim);

		return tokenSerivce.createJwt(requestClaim);
	}

	/**
	 * 2. 토큰 검증
	 *
	 * @param request Request with JWT
	 * @return VerifyTokenResponse
	 */
	@PostMapping("verifyToken")
	@Operation(summary = "2. 토큰(JWT) 검증")
	public VerifyTokenResponse verifyToken(@RequestBody Map<String, String> request) {
		log.info("Request JWT : " + request.get(JWT_FIELD_NAME));

		return tokenSerivce.verifyJwt(request.get(JWT_FIELD_NAME));
	}

	/**
	 * 토큰에서 클레임 추출
	 *
	 * @param request Request with JWT
	 * @return
	 */
	@PostMapping("extractClaim")
	@Operation(summary = "3. 토큰(JWT)에서 클레임 추출")
	public ExtractClaimResponse extractClaim(@RequestBody Map<String, String> request) {
		log.info("Request JWT : " + request.get(JWT_FIELD_NAME));

		return tokenSerivce.extractCredentialSubject(request.get(JWT_FIELD_NAME));
	}
}