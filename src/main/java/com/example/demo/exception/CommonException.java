package com.example.demo.exception;

public class CommonException extends RuntimeException {
	public CommonException(String message, Throwable cause) {
		this(null, message, cause);
	}

	public CommonException(Error error, String message, Throwable cause) {
		super();
	}
}