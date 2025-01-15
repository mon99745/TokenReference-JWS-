package com.security.jsonwebtoken.message;

import com.security.jsonwebtoken.model.Claims;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CreateTokenResponse {
	private Map<String, String> claim;
//	private Claims claim;
	private String jwt;
	private String resultCode;
	private String resultMsg;
}