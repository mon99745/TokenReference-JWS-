package com.security.jsonwebtoken.message;

import com.security.jsonwebtoken.model.Claims;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CreateTokenResponse {
	private int statusCode;
	private String result;
//	private Claims claim;
	private Map<String, String> claim;
	private String jwt;
	private String errorMessage;
}