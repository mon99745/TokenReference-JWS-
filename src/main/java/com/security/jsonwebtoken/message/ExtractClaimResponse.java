package com.security.jsonwebtoken.message;

import com.security.jsonwebtoken.model.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtractClaimResponse {
	private String jwt;
	private Claims claim;
	private String resultCode;
	private String resultMsg;
}