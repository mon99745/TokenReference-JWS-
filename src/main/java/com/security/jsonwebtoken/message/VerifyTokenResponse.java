package com.security.jsonwebtoken.message;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyTokenResponse {
	private int statusCode;
	private String result;
	private String jwt;
	private String errorMessage;
}