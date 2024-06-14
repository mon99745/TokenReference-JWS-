package com.example.demo.controller;

import com.example.demo.service.KeyPairService;
import com.example.demo.service.TokenSerivce;
import com.example.demo.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
	 * @param claim
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
	@PostMapping("createToken")
	@Operation(summary = "1. 토큰(JWT) 발행")
	public String createToken(@RequestBody String claim) throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException {
		String JsonWebToken = tokenSerivce.createJwt(claim);
		return JsonWebToken;
	}

	/**
	 * 토큰 검증
	 *
	 * @param token
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
	@PostMapping("verifyToken")
	@Operation(summary = "2. 토큰(JWT) 검증")
	public ResponseEntity<Object> verifyToken(@RequestBody String token) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException {
		String privateKey = keyPairService.getPrivateKey();
		ResponseEntity<Object> verifyResult = tokenSerivce.verifyToken(token, privateKey);
		return verifyResult;
	}

	/**
	 * 토큰에서 클레임 추출
	 *
	 * @param token
	 * @return
	 * @throws IOException
	 */
	@PostMapping("extractClaim")
	@Operation(summary = "3. 토큰(JWT)에서 클레임 추출")
	public String extractClaim(@RequestBody String token) throws IOException {
		JSONObject claim = tokenSerivce.extractCredentialSubject(token);
		return JsonUtil.toPrettyString(claim.toString());
	}
}