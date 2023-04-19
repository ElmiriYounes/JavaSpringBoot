package org.elmiriyounes.javabackend.exception;

public class InvalidRequestBodyException extends RuntimeException {
	public InvalidRequestBodyException(String message) {
		super(message);
	}
}

