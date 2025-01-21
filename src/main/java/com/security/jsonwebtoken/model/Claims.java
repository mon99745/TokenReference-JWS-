package com.security.jsonwebtoken.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Claims {

	/** 등록된 클레임 (Registered Claims) */
	private RegisteredClaim registeredClaims;

	/** 공개 클레임 (Public Claims) */
	private PublicClaim publicClaims;


	@Data
	@Builder
	public static class RegisteredClaim {
		private String issuer;       // issuer: 발급자 (iss)
		private String subject;      // subject: 주제 (sub)
		private String expiration;     // expiration: 만료 시간 (exp)
		private String issuedAt;       // issuedAt: 발급 시간 (iat)
	}

	@Data
	@Builder
	public static class PublicClaim {
		private Map<String, String> publicClaim;
	}
}