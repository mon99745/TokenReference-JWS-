package com.security.jsonwebtoken.service;

import com.security.jsonwebtoken.config.RsaKeyGenerator;
import com.security.jsonwebtoken.config.VerifyProperties;
import com.security.jsonwebtoken.model.Token;
import com.security.jsonwebtoken.util.ByteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignDocService {
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final TokenSerivce tokenSerivce;
	protected final VerifyProperties verifyProperties;

//	TODO : createSignDocument
//	public JSONObject createSignDocument(String claim) throws IOException, NoSuchPaddingException,
//			IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException,
//			InvalidKeyException {
//		Token.Header headerInfo = Token.Header.builder()
//				.alg(verifyProperties.getAlg())
//				.build();
//
//		Token.Payload payloadInfo = Token.Payload.builder()
//				.credentialSubject(new JSONObject(claim))
//				.build();
//
//		// Header 생성
//		String header = tokenSerivce.createHeader();
//		log.info("header = " + header);
//
//		// Payload 생성
//		String payload = tokenSerivce.createPayload(new JSONObject(claim));
//		log.info("payload = " + payload);
//
//		// Signature 생성
//		String publicKey = Base58.encode(rsaKeyGenerator.getPublicKey().getEncoded());
//		String signature = tokenSerivce.createSignature(payload, publicKey);
//		log.info("signature = " + signature);
//
//		String jws = tokenSerivce.combineToken(header, payload, signature);
//		log.info("jws = " + jws);
//
//		// TODO : SignDocument 모델 생성 후 Reflection 사용으로 필드 값을 모델에 대입하도록
//		JSONObject createSignDoc = new JSONObject();
//		createSignDoc.put("typ", headerInfo.getTyp());
//		createSignDoc.put("alg", headerInfo.getAlg());
//		createSignDoc.put("credentialSubject", payloadInfo.getCredentialSubject());
//		createSignDoc.put("publicKey", publicKey);
//		createSignDoc.put("jws", jws);
//
//		return createSignDoc;
//	}

//	TODO : verifyDocument
//	public ResponseEntity<Object> verifyDocument(JSONObject document, String privateKey) throws NoSuchAlgorithmException, IOException,
//			NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException,
//			InvalidKeyException {
//		String credentialSubject = document.optString("credentialSubject");
//		if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
//			throw new RuntimeException("credentialSubject is null or empty");
//		}
//
//		byte[] claimByteData = ByteUtil.stringToBytes(credentialSubject);
//
//		// Token Object Parsing
//		Token tokenObject = tokenSerivce.parseToken(document.getString("jws"));
//
//		// Signature
//
//		String signature = rsaKeyGenerator.decryptPrvRSA(tokenObject.getSignature(), privateKey);
//		byte[] signatureByteData = Base58.decode(signature);
//
//		// 해시 검증을 통해 위변조 검증
//		if (Arrays.equals(claimByteData, signatureByteData)) {
//			String successMessage = "검증 성공하였습니다.";
//			return new ResponseEntity<>(successMessage, HttpStatus.OK);
//		} else {
//			String errorMessage = "검증 실패하였습니다.";
//			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//		}
//	}
}