package com.security.jsonwebtoken.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	/**
	 * FIPS 180-4 (Federal Information Processing Standard 180-4) 표준
	 * SHA-256은 암호화 해시 함수
	 *
	 * @param plainText 평문
	 * @return
	 */
	public static String sha256(String plainText) {
		try {
			// SHA-256 MessageDigest 객체 생성
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// 입력 문자열을 바이트 배열로 변환하고 해시 처리
			byte[] hashBytes = digest.digest(plainText.getBytes());

			// 바이트 배열을 16진수 문자열로 변환
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				hexString.append(String.format("%02x", b));  // 바이트를 2자리 16진수로 변환
			}

			return hexString.toString();  // 해시값 반환
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not found", e);
		}
	}
}