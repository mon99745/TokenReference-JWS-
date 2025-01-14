package com.security.jsonwebtoken.controller;

import com.security.jsonwebtoken.message.CreateTokenResponse;
import com.security.jsonwebtoken.message.ExtractClaimResponse;
import com.security.jsonwebtoken.message.VerifyTokenResponse;
import com.security.jsonwebtoken.service.KeyPairService;
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
	protected final KeyPairService keyPairService;
	protected final TokenSerivce tokenSerivce;


	/**
	 * 토큰 발행
	 *
	 * @param claim Claim to include in JWT
	 * @return
	 */
	@PostMapping("createToken")
	@Operation(summary = "1. 토큰(JWT) 발행")
	public CreateTokenResponse createToken(@RequestBody Map<String, String> claim){
		log.info("Request Claims : ", claim);

		return tokenSerivce.createJwt(claim);
	}

	/**
	 * 토큰 검증
	 *
	 * @param request Request with token
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	@PostMapping("verifyToken")
	@Operation(summary = "2. 토큰(JWT) 검증")
	public VerifyTokenResponse verifyToken(@RequestBody Map<String, String> request)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		log.info("Request JWT : " + request.get("jwt"));

		String privateKey = keyPairService.getPrivateKey();
		return tokenSerivce.verifyToken(request.get("jwt"), privateKey);
	}

	/**
	 * 토큰에서 클레임 추출
	 *
	 * @param request Request with token
	 * @return
	 */
	@PostMapping("extractClaim")
	@Operation(summary = "3. 토큰(JWT)에서 클레임 추출")
	public ExtractClaimResponse extractClaim(@RequestBody Map<String, String> request) {
		log.info("Request JWT : " + request.get("jwt"));

		return tokenSerivce.extractCredentialSubject(request.get("jwt"));
	}
}