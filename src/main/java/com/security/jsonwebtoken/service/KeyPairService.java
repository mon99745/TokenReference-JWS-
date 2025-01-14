package com.security.jsonwebtoken.service;

import com.security.jsonwebtoken.config.RsaKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyPairService {
	protected final RsaKeyGenerator rsaKeyGenerator;

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

	public String getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		return Base58.encode(rsaKeyGenerator.getPrivateKey().getEncoded());
	}
}