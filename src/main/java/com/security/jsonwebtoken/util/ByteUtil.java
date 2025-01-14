package com.security.jsonwebtoken.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ByteUtil {
	/**
	 * 문자열을 바이트 코드로 변환
	 *
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] stringToBytes(String str) throws IOException {
		byte[] byteData = str.getBytes("UTF-8");
		return byteData;
	}

	/**
	 * 바이트 코드를 UTF-8 문자열로 변환
	 *
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static String bytesToUtfString(byte[] bytes) throws IOException {
		String strData = new String(bytes, "UTF-8");
		return strData;
	}

	/**
	 * 바이트를 16진수 문자열로 변환
	 *
	 * @param hashData
	 * @return
	 */
	public static StringBuilder bytesToHexString(byte[] hashData) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashData) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString;
	}

	/**
	 * 객체를 바이트 배열로 변환
	 * Serialization
	 *
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToBytes(Object obj) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(obj);
			return bos.toByteArray();
		}
	}

	/**
	 * 바이트 배열을 객체로 변환
	 * Deserialization
	 *
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		}
	}
}