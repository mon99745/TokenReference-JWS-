package com.security.jsonwebtoken.message;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ExtractClaimResponse {
	private String jwt;
	private Map<String, String> claim;
	private String resultCode;
	private String resultMsg;
}