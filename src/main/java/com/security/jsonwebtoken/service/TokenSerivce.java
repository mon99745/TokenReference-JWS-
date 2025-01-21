package com.security.jsonwebtoken.service;

import com.security.jsonwebtoken.config.RsaKeyGenerator;
import com.security.jsonwebtoken.config.VerifyProperties;
import com.security.jsonwebtoken.message.CreateTokenResponse;
import com.security.jsonwebtoken.message.ExtractClaimResponse;
import com.security.jsonwebtoken.message.VerifyTokenResponse;
import com.security.jsonwebtoken.model.Claims;
import com.security.jsonwebtoken.model.Token;
import com.security.jsonwebtoken.util.ByteUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jsonwebtoken.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenSerivce {
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final VerifyProperties verifyProperties;
	protected final KeyPairService keyPairService;

	/**
	 * Re-entry after Claim initialization
	 *
	 * @param requestClaim to include in JWT
	 * @return
	 */
	public CreateTokenResponse createJwt(Map<String, String> requestClaim) {
		return this.createJwt(setClaims(requestClaim));
	}

	/**
	 * Json Web Token 생성
	 *
	 * @param claims to include in JWT
 	 * @return
	 */
	public CreateTokenResponse createJwt(Claims claims) {
		try {
			if (claims == null) {
				throw new IllegalArgumentException("Claim is empty");
			}

			/** Header 생성 */
			String header = createHeader();
			log.info("header = {}, header byte = {}", header, header.getBytes().length);

			/** Payload 생성 */
			List<String> payload = createPayload(claims);
			log.info("payload = {}", payload);
			payload.forEach(pl ->
					log.info("Byte size: {}", pl.getBytes(StandardCharsets.UTF_8).length));

			/** VerifyCode 생성 */
			String verifyCode = setVerifyCode(header, payload);
			log.info("verifyCode = {}, verifyCode byte = {}", verifyCode, verifyCode.getBytes().length);

			/** Signature 생성 */
			String privateKey = keyPairService.getPrivateKey();
			String signature = createSignature(verifyCode, privateKey);
			log.info("signature = {}, signature byte = {}", signature, signature.getBytes().length);

			/** Json Web Token 생성 */
			String jwt = combineToken(header, String.join("", payload), signature);
			log.info("jwt = {}, jwt byte = {}", jwt, jwt.getBytes().length);

			return CreateTokenResponse.builder()
					.resultMsg("Success")
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.claim(claims.getPublicClaims())
					.jwt(jwt)
					.build();

		} catch (IllegalArgumentException e) {
			log.error("JWT 생성 중 예외 발생: ", e);
			return CreateTokenResponse.builder()
					.resultMsg("Fail: " + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.claim(claims.getPublicClaims())
					.build();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException |
				 NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
				 InvalidKeyException e) {
			log.error("JWT 생성 중 예외 발생: ", e);
			return CreateTokenResponse.builder()
					.resultMsg("Fail: " + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.claim(claims.getPublicClaims())
					.build();
		}
	}

	public VerifyTokenResponse verifyToken(String token)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String publicKey = keyPairService.getPublicKey();
		try {
			if (token == null || token.isEmpty()) {
				throw new IllegalArgumentException("JWT is empty");
			}

			// Token Object Parsing
			Token tokenObject = parseToken(token);

			// Decrypt Signature
			String signature = rsaKeyGenerator.decryptPubRSA(tokenObject.getSignature(), publicKey);

			// 해시 비교를 통해 위변조 검증
			if (signature.equals(tokenObject.getHeader() + tokenObject.getPayload())) {
				return VerifyTokenResponse.builder()
						.resultMsg("Success")
						.resultCode(String.valueOf(HttpStatus.OK.value()))
						.jwt(token)
						.build();
			} else {
				return VerifyTokenResponse.builder()
						.resultMsg("Fail: " + "토큰이 위변조 되었습니다.")
						.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.jwt(token)
						.build();
			}
		} catch (IllegalArgumentException e) {
			log.error("JWT 검증 중 예외 발생: ", e);
			return VerifyTokenResponse.builder()
					.resultMsg("Fail: " + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.jwt(token)
					.build();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException |
				 NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
				 InvalidKeyException e) {
			log.error("JWT 검증 중 예외 발생: ", e);
			return VerifyTokenResponse.builder()
					.resultMsg("Fail: " + "JWT 검증 실패 :" + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.jwt(token)
					.build();
		}
	}

	public ExtractClaimResponse extractCredentialSubject(String token) {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException("JWT is empty");
		}

		// Token Object Parsing
		Token tokenObject = parseToken(token);
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			byte[] decodedBytes = Base58.decode(tokenObject.getPayload());
			String claims = ByteUtil.bytesToUtfString(decodedBytes);
			if (Objects.isNull(claims) || claims.isEmpty()) {
				throw new RuntimeException("Extracted credentialSubject is null or empty");
			}
			return ExtractClaimResponse.builder()
					.resultMsg("Success")
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.claim(objectMapper.readValue(claims, Claims.class))
					.jwt(token)
					.build();
		} catch (IllegalArgumentException e) {
			log.error("Claim 추출 실패 : ", e);
			return ExtractClaimResponse.builder()
					.resultMsg("Fail: " + "Claim 추출 실패 : " + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.build();
		} catch (IOException e) {
			log.error("Claim 추출 실패 : ", e);
			return ExtractClaimResponse.builder()
					.resultMsg("Fail: " + "Claim 추출 실패 :" + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.build();
		}
	}

	public Claims setClaims(Map<String, String> Authentication) {
		/**
		 * TODO : Add default claims
		 */
		Claims.RegisteredClaims registeredClaims = Claims.RegisteredClaims.builder()
				.build();

		Claims.PublicClaims publicClaims = Claims.PublicClaims.builder()
				.build();

		Claims.PrivateClaims privateClaims = Claims.PrivateClaims.builder()
				.build();

		return Claims.builder()
				.registeredClaims(registeredClaims)
				.publicClaims(publicClaims)
				.privateClaims(privateClaims)
				.build();
	}

	public String createHeader() throws IOException {
		String typ = verifyProperties.getTyp();
		String alg = verifyProperties.getAlg();

		if (Objects.isNull(typ) || typ.isEmpty()
				|| Objects.isNull(alg) || alg.isEmpty()) {
			throw new RuntimeException("Header Info is null or empty");
		}

		// TODO: setHeader 정의 후 오버라이딩이 필요.
		byte[] byteHeaderData = ByteUtil.stringToBytes(typ + alg);
		String encHeader = Base58.encode(byteHeaderData);

		return encHeader;
	}

	public List<String> createPayload(Claims claims) throws IOException {
		List<String> strClaims = new ArrayList<>();
		strClaims.add(String.valueOf(claims.getRegisteredClaims()));
		strClaims.add(String.valueOf(claims.getPublicClaims()));
		strClaims.add(String.valueOf(claims.getPrivateClaims()));

		if (Objects.isNull(strClaims) || strClaims.isEmpty()) {
			throw new RuntimeException("credentialSubject is null or empty");
		}

		List<String> payload = new ArrayList<>();
		for (String claim : strClaims) {
			byte[] bytePayloadData = ByteUtil.stringToBytes(claim);
			payload.add(Base58.encode(bytePayloadData));
		}

		return payload;
	}

	public String setVerifyCode(String header, List<String> payload) {
		StringBuilder strPayload = new StringBuilder();
		for (String pl : payload) {
			strPayload.append(pl);
		}
		return HashUtil.sha256(header + strPayload);
	}

	public String createSignature(String verifyCode, String privateKey)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {

		return rsaKeyGenerator.encryptPrvRSA(verifyCode, privateKey);
	}

	public String combineToken(String header, String payload, String signature) {
		return header + "." + payload + "." + signature;
	}

	public Token parseToken(String token) {
		String[] splitArray = token.split("\\.");
		String header = null;
		String payload = null;
		String signature = null;

		for (int i = 0; i < splitArray.length; i++) {
			if (i == 0) {
				header = splitArray[i];
			} else if (i == 1) {
				payload = splitArray[i];
			} else if (i == 2) {
				signature = splitArray[i];
			}
		}

		return new Token(header, payload, signature);
	}
}