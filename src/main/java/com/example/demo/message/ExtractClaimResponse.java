package com.example.demo.message;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ExtractClaimResponse {
	private int statusCode;
	private String result;
	private Map<String, String> claim;
	private String jwt;
	private String errorMessage;
}