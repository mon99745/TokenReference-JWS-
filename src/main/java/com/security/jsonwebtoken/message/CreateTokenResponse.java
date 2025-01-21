package com.security.jsonwebtoken.message;

import com.security.jsonwebtoken.model.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTokenResponse {
	private Claims.PublicClaim claims;
	private String jwt;
	private String resultCode;
	private String resultMsg;
}