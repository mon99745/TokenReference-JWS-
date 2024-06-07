package com.example.demo.controller;

import com.example.demo.config.VerifyProperties;
import com.example.demo.model.Request;
import com.example.demo.util.ByteUtil;
import com.example.demo.config.RsaKeyGenerator;
import com.example.demo.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.bitcoinj.core.Base58;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Api(tags = JwtRestController.TAG)
@RestController
public class JwtRestController {

	public static final String TAG = "JWS Manager API";
	protected final VerifyProperties verifyProperties;
	protected final RsaKeyGenerator rsaKeyGenerator;

	public JwtRestController(VerifyProperties verifyProperties) {
		this.verifyProperties = verifyProperties;
		this.rsaKeyGenerator = new RsaKeyGenerator(verifyProperties);
	}

	@GetMapping("createKeyPair")
	@Operation(summary = "키 페어 생성")
	public Map<String, Object> createKeyPair() {
		Map<String, Object> keyMap = rsaKeyGenerator.createKey();
		Map<String, Object> strKeymap = new HashMap<>();

		PublicKey publicKey = (PublicKey) keyMap.get("PublicKey");
		PrivateKey privateKey = (PrivateKey) keyMap.get("PrivateKey");

		String strPublicKey = Base58.encode(publicKey.getEncoded());
		String strPrivateKey = Base58.encode(privateKey.getEncoded());

		strKeymap.put("publicKey", strPublicKey);
		strKeymap.put("privateKey", strPrivateKey);
		return strKeymap;
	}

	/**
	 * JWS 토큰 발행
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
	@PostMapping("createReqMsg")
	@Operation(summary = "1. 토큰과 함께 요청문 발행")
	public String createReqMsg(Request keyPair, @RequestBody String claim) throws IOException, NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException,
			InvalidKeyException, JSONException {

		String header = "";
		String payload = "";
		String signature = "";

		if (keyPair.getPublicKey() == null || keyPair.getPrivateKey() == null) {
			keyPair.setPublicKey(Base58.encode(rsaKeyGenerator.getPublicKey().getEncoded()));
			keyPair.setPrivateKey(Base58.encode(rsaKeyGenerator.getPrivateKey().getEncoded()));
		}

		JSONObject jsonObject = new JSONObject();
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		jsonObject.put("type", "JWS");
		jsonObject.put("alg", "SHA256");
		jsonObject.put("credentialSubject", new JSONObject(claim));
		jsonObject.put("publicKey", keyPair.getPublicKey());
		// Header
		byte[] byteHeaderData = ByteUtil.stringToBytes(
				jsonObject.get("type").toString() + jsonObject.get("alg").toString());
		header = Base58.encode(byteHeaderData);
		System.out.println("header = " + header);

		// Payload
		byte[] bytePayloadData = ByteUtil.stringToBytes(jsonObject.get("credentialSubject").toString());
		byte[] hashData = digest.digest(bytePayloadData);

		// 바이트를 16진수 문자열로 변환
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashData) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		byte[] claimHexData = ByteUtil.stringToBytes(hexString.toString());
		payload = Base58.encode(claimHexData);
		System.out.println("payload = " + payload);

		// Signature
		signature = rsaKeyGenerator.encryptPrvRSA(payload, keyPair.getPrivateKey());
		System.out.println("signature = " + signature);

		String jws = header + "." + payload + "." + signature;
		System.out.println("jws = " + jws);

		jsonObject.put("jws", jws);

		return JsonUtil.toPrettyString(jsonObject.toString());
	}

	/**
	 * JWS 토큰 검증
	 *
	 * @param reqMsg
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
	@PostMapping("verifyReqMsg")
	@Operation(summary = "2. 토큰을 통해 요청문 검증")
	public ResponseEntity<Object> verifyReqMsg(@RequestBody String reqMsg) throws NoSuchPaddingException, IllegalBlockSizeException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException, BadPaddingException, InvalidKeyException, JSONException {

		String header = "";
		String payload = "";
		String signature = "";

		JSONObject jsonObject = new JSONObject(reqMsg);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		byte[] byteData = ByteUtil.stringToBytes(jsonObject.get("credentialSubject").toString());
		byte[] hashData = digest.digest(byteData);

		// 바이트를 16진수 문자열로 변환
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashData) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		byte[] claimHexData = ByteUtil.stringToBytes(hexString.toString());

		String[] splitArray = jsonObject.getString("jws").split("\\.");
		for (int i = 0; i < splitArray.length; i++) {
			if (i == 0) {
				header = splitArray[i];
			} else if (i == 1) {
				payload = splitArray[i];
			} else if (i == 2) {
				signature = splitArray[i];
			}
		}

		// Signature
		String publickey = jsonObject.getString("publicKey");
//		signature = rsaKeyGenerator.decryptPubRSA(signature);
		signature = rsaKeyGenerator.decryptPubRSA(signature, publickey);
		byte[] signatureHexData = Base58.decode(signature);

		// 해시 검증을 통해 위변조 검증
		if (Arrays.equals(claimHexData, signatureHexData)) {
			String successMessage = "검증 성공하였습니다.";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} else {
			String errorMessage = "검증 실패하였습니다.";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * PK base58 암/복호화
	 *
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
//	@GetMapping("base58/key")
	public String base58() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("value = " + ByteUtil.objectToBytes(rsaKeyGenerator.getPublicKey()));

		byte[] publicKey = ByteUtil.objectToBytes(rsaKeyGenerator.getPublicKey());
		System.out.println("publicKey = " + publicKey);

		// Base58 인코딩
		String encodedData = Base58.encode(ByteUtil.objectToBytes(publicKey));

		System.out.println("Original Data: " + publicKey);
		System.out.println("Base58 Encoded: " + encodedData);

		// Base58 디코딩
		byte[] decodedBytes = Base58.decode(encodedData);
		String decodedData = new String(decodedBytes);

		System.out.println("Base58 Decoded: " + decodedData);


		return "Success";
	}

	/**
	 * JSON 데이터 파싱
	 *
	 * @return
	 */
//	@GetMapping("json")
	public String readJson() {

		// JSON 파일 경로 설정
		String filePath = "example/claim.json";

		// JSON 파일 읽기
		try {
			ClassPathResource resource = new ClassPathResource(filePath);
			byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
			String jsonString = new String(jsonData, StandardCharsets.UTF_8);

			// JSON 파싱
			System.out.println(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Success";
	}

	/**
	 * JSON 데이터를 Base58 암호화
	 * JWT - Header
	 *
	 * @param strData
	 * @return
	 * @throws IOException
	 */
//	@PostMapping("base58/json")
	public String encryptJsonWithBas58(@RequestBody String strData) throws IOException {
		byte[] byteData = ByteUtil.objectToBytes(strData);
		String encData = Base58.encode(byteData);
		System.out.println("encData = " + encData);

		return encData;
	}

	/**
	 * JSON 데이터를 개인키로 암호화
	 * for 서명
	 *
	 * @param strData
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
//	@PostMapping("encryptPrv")
	public String encryptJsonWithPrivateKey(@RequestBody String strData) throws NoSuchPaddingException, IllegalBlockSizeException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException, BadPaddingException, InvalidKeyException {
		// String 문자열을 개인키로 암호화
		String encData = rsaKeyGenerator.encryptPrvRSA(strData);
		System.out.println("encData = " + encData);

		return encData;
	}

	/**
	 * Header 데이터를 개인키로 암호화
	 *
	 * @param header
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
//	@PostMapping("signature")
	public String signature(String header) throws NoSuchPaddingException, IllegalBlockSizeException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException, BadPaddingException, InvalidKeyException {
		String signature = rsaKeyGenerator.encryptPrvRSA(header);
		System.out.println("signature = " + signature);

		return signature;
	}

	/**
	 * JWT 토큰 발행 테스트
	 *
	 * @param header
	 * @param payload
	 * @param signature
	 * @return
	 */
//	@PostMapping("jws")
	public String jws(String header, String payload, String signature) {
		String jws = header + "." + payload + "." + signature;
		System.out.println("jws = " + jws);
		return jws;
	}
}