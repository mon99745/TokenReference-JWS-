package com.example.demo.service;

import com.example.demo.config.RsaKeyGenerator;
import com.example.demo.config.VerifyProperties;
import com.example.demo.model.KeyPair;
import com.example.demo.util.ByteUtil;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtSerivce {
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final VerifyProperties verifyProperties;

	public Map<String, Object> createKeyPair(Map<String, Object> keyPair) {
		Map<String, Object> strKeymap = new HashMap<>();

		PublicKey publicKey = (PublicKey) keyPair.get("PublicKey");
		PrivateKey privateKey = (PrivateKey) keyPair.get("PrivateKey");

		String strPublicKey = Base58.encode(publicKey.getEncoded());
		String strPrivateKey = Base58.encode(privateKey.getEncoded());

		strKeymap.put("publicKey", strPublicKey);
		strKeymap.put("privateKey", strPrivateKey);
		return strKeymap;
	}

	public String createHeader(JSONObject jsonObject) throws IOException {
		byte[] byteHeaderData = ByteUtil.stringToBytes(
				jsonObject.get("type").toString() + jsonObject.get("alg").toString());
		String header = Base58.encode(byteHeaderData);

		return header;
	}

	public String createPayload(JSONObject jsonObject) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] bytePayloadData = ByteUtil.stringToBytes(jsonObject.get("credentialSubject").toString());
		byte[] hashData = digest.digest(bytePayloadData);
		byte[] claimHexData = ByteUtil.stringToBytes(ByteUtil.bytesToHexString(hashData).toString());
		String payload = Base58.encode(claimHexData);

		return payload;
	}

	public String createSignature(String payload, KeyPair keyPair)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		String signature = rsaKeyGenerator.encryptPrvRSA(payload, keyPair.getPrivateKey());

		return signature;
	}

	public ResponseEntity<Object> verifyDocument(JSONObject document) throws NoSuchAlgorithmException, IOException,
			NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException,
			InvalidKeyException {
		String header = "";
		String payload = "";
		String signature = "";

		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		byte[] byteData = ByteUtil.stringToBytes(document.get("credentialSubject").toString());
		byte[] hashData = digest.digest(byteData);
		byte[] claimHexData = ByteUtil.stringToBytes(ByteUtil.bytesToHexString(hashData).toString());

		String[] splitArray = document.getString("jws").split("\\.");
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
		String publickey = document.getString("publicKey");
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
}
