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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenSerivce {
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final VerifyProperties verifyProperties;

//	public Claims setClaims(Map<String, String> request) {
//		/**
//		 * TODO : Add default claims
//		 */
//		Claims.RegisteredClaims registeredClaims = Claims.RegisteredClaims.builder()
//				.issuer(request.get("uniqueId"))
//				.jwtId(request.get("num"))
//				.build();
//
//		Claims.PublicClaims publicClaims = Claims.PublicClaims.builder()
//				.username(request.get("name"))
//				.build();
//
//		Claims.PrivateClaims privateClaims = Claims.PrivateClaims.builder()
//				.build();
//
//		return Claims.builder()
//				.registeredClaims(registeredClaims)
//				.publicClaims(publicClaims)
//				.privateClaims(privateClaims)
//				.build();
//	}
//	public CreateTokenResponse createJwt(Map<String, String> claim) {
//		return this.createJwt(setClaims(claim));
//	}
	public CreateTokenResponse createJwt(Map<String, String> claim) {
		try {
			if (claim == null || claim.isEmpty()) {
				throw new IllegalArgumentException("Claim is empty");
			}

			Token.Header headerInfo = Token.Header.builder()
					.typ(verifyProperties.getTyp())
					.alg(verifyProperties.getAlg())
					.build();

			Token.Payload payloadInfo = Token.Payload.builder()
					.credentialSubject(new JSONObject(claim))
					.build();

			// Header 생성
			String header = createHeader(headerInfo);
			log.info("header = " + header);

			// Payload 생성
			String payload = createPayload(payloadInfo);
			log.info("payload = " + payload);

			// Signature 생성
			String publicKey = Base58.encode(rsaKeyGenerator.getPublicKey().getEncoded());
			String signature = createSignatureForJwt(header, payload, publicKey);
			log.info("signature = " + signature);

			String jwt = combineToken(header, payload, signature);
			log.info("jwt = " + jwt);
			return CreateTokenResponse.builder()
					.resultMsg("Success")
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.claim(claim)
					.jwt(jwt)
					.build();

		} catch (IllegalArgumentException e) {
			log.error("JWT 생성 중 예외 발생: ", e);
			return CreateTokenResponse.builder()
					.resultMsg("Fail: " + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.claim(claim)
					.build();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException |
				NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
				InvalidKeyException e) {
			log.error("JWT 생성 중 예외 발생: ", e);
			return CreateTokenResponse.builder()
					.resultMsg("Fail" + e.getMessage())
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.claim(claim)
					.build();
		}
	}

	public VerifyTokenResponse verifyToken(String token, String privateKey) {
		try {
			if (token == null || token.isEmpty()) {
				throw new IllegalArgumentException("JWT is empty");
			}

			// Token Object Parsing
			Token tokenObject = parseToken(token);

			// Decrypt Signature
			String signature = rsaKeyGenerator.decryptPrvRSA(tokenObject.getSignature(), privateKey);

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
					.resultMsg("Fail:" + "JWT 검증 실패 :" + e.getMessage())
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
			String credentialSubject = ByteUtil.bytesToUtfString(decodedBytes);
			if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
				throw new RuntimeException("Extracted credentialSubject is null or empty");
			}
			return ExtractClaimResponse.builder()
					.resultMsg("Success")
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.claim(objectMapper.readValue(credentialSubject, Map.class))
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


	public String createHeader(Token.Header header) throws IOException {
		String typ = header.getTyp();
		String alg = header.getAlg();
		if (Objects.isNull(typ) || typ.isEmpty()
				|| Objects.isNull(alg) || alg.isEmpty()) {
			throw new RuntimeException("Header Info is null or empty");
		}

		byte[] byteHeaderData = ByteUtil.stringToBytes(typ + alg);
		String encHeader = Base58.encode(byteHeaderData);

		return encHeader;
	}

	public String createPayload(Token.Payload payload) throws IOException {
		String credentialSubject = String.valueOf(payload.getCredentialSubject());
		if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
			throw new RuntimeException("credentialSubject is null or empty");
		}
		byte[] bytePayloadData = ByteUtil.stringToBytes(credentialSubject);
		String encPayload = Base58.encode(bytePayloadData);

		return encPayload;
	}

	public String createSignature(String payload, String publicKey)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		String signature = rsaKeyGenerator.encryptPubRSA(payload, publicKey);

		return signature;
	}

	public String createSignatureForJwt(String header, String payload, String publicKey)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		String strData = header + payload;
		String signature = rsaKeyGenerator.encryptPubRSA(strData, publicKey);

		return signature;
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