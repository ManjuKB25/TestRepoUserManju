package com.learn.spring.batch.one.exceptions;

public class EmptyUserRepoException extends CustomException {
	public EmptyUserRepoException(String message) {
		super(message);
	}
}
