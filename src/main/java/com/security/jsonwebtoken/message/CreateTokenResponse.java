package com.security.jsonwebtoken.message;

import com.security.jsonwebtoken.model.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTokenResponse {
	private Claims.PublicClaims claim;
	private String jwt;
	private String resultCode;
	private String resultMsg;
}