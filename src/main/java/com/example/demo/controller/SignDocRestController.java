package com.example.demo.controller;

import com.example.demo.config.RsaKeyGenerator;
import com.example.demo.model.KeyPair;
import com.example.demo.model.Token;
import com.example.demo.service.TokenSerivce;
import com.example.demo.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.json.JSONException;
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

@Api(tags = SignDocRestController.TAG)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(SignDocRestController.PATH)
public class SignDocRestController {
	public static final String TAG = "Signature Document Manager API";
	public static final String PATH = "/api/v1";

	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final TokenSerivce tokenSerivce;

	/**
	 * 서명 문서 발행
	 *
	 * @param claim
	 * @return
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws JSONException
	 */
	@PostMapping("createSignDocument")
	@Operation(summary = "1. 서명 문서 발행")
	public String createSignDocument(KeyPair keyPair, @RequestBody String claim) throws IOException,
			NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException,
			BadPaddingException, InvalidKeyException, JSONException {

		// TODO: 공개키로 암호화/ 개인키로 복호화 (부인방지)
		if (keyPair.getPublicKey() == null || keyPair.getPrivateKey() == null) {
			keyPair.setPublicKey(Base58.encode(rsaKeyGenerator.getPublicKey().getEncoded()));
			keyPair.setPrivateKey(Base58.encode(rsaKeyGenerator.getPrivateKey().getEncoded()));
		}

		Token.Header headerInfo = Token.Header.builder()
				.typ("JWT")
				.alg("SHA256").build();

		Token.Payload payloadInfo = Token.Payload.builder()
				.credentialSubject(new JSONObject(claim))
				.build();

		// Header 생성
		String header = tokenSerivce.createHeader(headerInfo);
		log.info("header = " + header);

		// Payload 생성
		String payload = tokenSerivce.createPayload(payloadInfo);
		log.info("payload = " + payload);

		// Signature 생성
		String signature = tokenSerivce.createSignature(payload, keyPair);
		log.info("signature = " + signature);

		String jws = tokenSerivce.createJws(header, payload, signature);
		log.info("jws = " + jws);

		// TODO : SignDocument 모델 생성 후 Reflection 사용으로 필드 값을 모델에 대입하도록
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("typ", headerInfo.getTyp());
		jsonObject.put("alg", headerInfo.getAlg());
		jsonObject.put("credentialSubject", payloadInfo.getCredentialSubject());
		jsonObject.put("publicKey", keyPair.getPublicKey());
		jsonObject.put("jws", jws);


		return JsonUtil.toPrettyString(jsonObject.toString());
	}

	/**
	 * 서명 문서 검증
	 *
	 * @param document
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws JSONException
	 */
	@PostMapping("verifySignDocument")
	@Operation(summary = "2. 서명 문서 검증")
	public ResponseEntity<Object> verifySignDocument(@RequestBody String document) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, IOException,
			BadPaddingException, InvalidKeyException, JSONException {
		JSONObject doc = new JSONObject(document);
		return tokenSerivce.verifyDocument(doc);
	}
}