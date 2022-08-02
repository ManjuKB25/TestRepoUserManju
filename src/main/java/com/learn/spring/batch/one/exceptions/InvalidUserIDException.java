package com.learn.spring.batch.one.exceptions;

public class InvalidUserIDException extends RuntimeException{
	private String message;

	public InvalidUserIDException() {
	}
	public InvalidUserIDException(String message) {
		super(message);
		this.message = message;
	}
	
}
