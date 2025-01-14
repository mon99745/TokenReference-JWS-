package com.security.jsonwebtoken.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 토큰 클레임 관리
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ConfigurationProperties(ClaimsProperties.PROPERTY_PREFIX)
public class ClaimsProperties {
	/**
	 * 설정 타이틀
	 */
	public static final String PROPERTY_PREFIX = "claims";

	/**
	 * 설정 정보
	 */
	@Getter
	protected static ClaimsProperties instance = new ClaimsProperties();

	/** 등록된 클레임 (Registered Claims) */
//	private RegisteredClaims registeredClaims;

	/** 공개 클레임 (Public Claims) */
//	private PublicClaims publicClaims;

	/** 비공개 클레임 (Private Claims) */
//	private PrivateClaims privateClaims;

	/**
	 * 등록된 클레임 (Registered Claims)
	 */
	@Data
	@NoArgsConstructor
	public static class RegisteredClaims {
		private String issuer;       // issuer: 발급자 (iss)
		private String subject;      // subject: 주제 (sub)
		private String audience;     // audience: 수신자 (aud)
		private Long expiration;     // expiration: 만료 시간 (exp)
		private Long issuedAt;       // issuedAt: 발급 시간 (iat)
		private Long notBefore;      // notBefore: 사용 가능 시작 시간 (nbf)
		private String jwtId;        // jwtId: JWT 고유 ID (jti)
	}

	/**
	 * 공개 클레임 (Public Claims)
	 */
	@Data
	@NoArgsConstructor
	public static class PublicClaims {
		private String role;         // role: 사용자 역할
		private String username;     // username: 사용자 이름
		private String email;        // email: 사용자 이메일
		private String scope;        // scope: 권한 범위
	}

	/**
	 * 비공개 클레임 (Private Claims)
	 */
	@Data
	@NoArgsConstructor
	public static class PrivateClaims {
		private String customerId;   // customerId: 고객 ID
		private String tenantId;     // tenantId: 테넌트 ID
		private String sessionId;    // sessionId: 세션 ID
		private String customClaim;  // customClaim: 사용자 정의 클레임
	}
}