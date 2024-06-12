package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyPairService {
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
}