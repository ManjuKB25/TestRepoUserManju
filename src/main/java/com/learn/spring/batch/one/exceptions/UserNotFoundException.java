package com.learn.spring.batch.one.exceptions;


public class UserNotFoundException extends RuntimeException{
	private String message;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String message) {
		super(message);
		this.message = message;
	}
	
}
