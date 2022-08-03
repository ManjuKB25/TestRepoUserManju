package com.learn.spring.batch.one.exceptions;

public class CustomException extends RuntimeException {
	protected String message;

	public CustomException() {
	}

	public CustomException(String message) {
		super(message);
		this.message = message;
	}

}
