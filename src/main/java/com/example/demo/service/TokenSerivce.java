package com.example.demo.service;

import com.example.demo.config.RsaKeyGenerator;
import com.example.demo.config.VerifyProperties;
import com.example.demo.model.KeyPair;
import com.example.demo.model.Token;
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
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenSerivce {
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final VerifyProperties verifyProperties;

	public String createHeader(JSONObject jsonObject) throws IOException {
		String typ = jsonObject.optString("typ");
		String alg = jsonObject.optString("alg");
		if (Objects.isNull(typ) || typ.isEmpty()
				|| Objects.isNull(alg) || alg.isEmpty()) {
			throw new RuntimeException("Header Info is null or empty");
		}

		byte[] byteHeaderData = ByteUtil.stringToBytes(typ + alg);
		String header = Base58.encode(byteHeaderData);

		return header;
	}

	public String createPayload(JSONObject jsonObject) throws IOException {
		String credentialSubject = jsonObject.optString("credentialSubject");
		if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
			throw new RuntimeException("credentialSubject is null or empty");
		}
		byte[] bytePayloadData = ByteUtil.stringToBytes(credentialSubject);
		String payload = Base58.encode(bytePayloadData);

		return payload;
	}

	public String createSignature(String payload, KeyPair keyPair)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		String signature = rsaKeyGenerator.encryptPrvRSA(payload, keyPair.getPrivateKey());

		return signature;
	}

	public String createSignatureForJwt(String header, String payload, String publicKey)
			throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
			InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		String strData = header + payload;
		String signature = rsaKeyGenerator.encryptPubRSA(strData, publicKey);

		return signature;
	}

	public String createJwt(String header, String payload, String signature) {
		return header + "." + payload + "." + signature;
	}

	public String createJws(String header, String payload, String signature) {
		return header + "." + payload + "." + signature;
	}

	public ResponseEntity<Object> verifyToken(String token, String privateKey) throws NoSuchAlgorithmException,
			IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException,
			BadPaddingException, InvalidKeyException {
		// Token Object Parsing
		Token tokenObject = parseToken(token);

		// Decrypt Signature
		String signature = rsaKeyGenerator.decryptPrvRSA(tokenObject.getSignature(), privateKey);

		// 해시 검증을 통해 위변조 검증
		if (signature.equals(tokenObject.getHeader() + tokenObject.getPayload())) {
			String successMessage = "검증 성공하였습니다.";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} else {
			String errorMessage = "검증 실패하였습니다.";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> verifyDocument(JSONObject document) throws NoSuchAlgorithmException, IOException,
			NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException,
			InvalidKeyException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		String credentialSubject = document.optString("credentialSubject");
		if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
			throw new RuntimeException("credentialSubject is null or empty");
		}

		byte[] claimByteData = ByteUtil.stringToBytes(credentialSubject);

		// Token Object Parsing
		Token tokenObject = parseToken(document.getString("jws"));

		// Signature
		String publickey = document.getString("publicKey");
		String signature = rsaKeyGenerator.decryptPubRSA(tokenObject.getSignature(), publickey);
		byte[] signatureByteData = Base58.decode(signature);

		// 해시 검증을 통해 위변조 검증
		if (Arrays.equals(claimByteData, signatureByteData)) {
			String successMessage = "검증 성공하였습니다.";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} else {
			String errorMessage = "검증 실패하였습니다.";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}

	public JSONObject extractCredentialSubject(String token) throws IOException {
		Token tokenObject = parseToken(token);

		byte[] decodedBytes = Base58.decode(tokenObject.getPayload());
		String credentialSubject = ByteUtil.bytesToUtfString(decodedBytes);
		if (Objects.isNull(credentialSubject) || credentialSubject.isEmpty()) {
			throw new RuntimeException("Extracted credentialSubject is null or empty");
		}

		return new JSONObject(credentialSubject);
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