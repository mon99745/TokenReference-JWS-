package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	private String header;
	private String payload;
	private String signature;

	@Getter
	@Builder
	public static class Header {
		private String typ;
		private String alg;
	}

	@Getter
	@Builder
	public static class Payload {
		private JSONObject credentialSubject;
	}
}