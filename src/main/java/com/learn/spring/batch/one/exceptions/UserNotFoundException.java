package com.learn.spring.batch.one.exceptions;


public class UserNotFoundException extends CustomException{
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
