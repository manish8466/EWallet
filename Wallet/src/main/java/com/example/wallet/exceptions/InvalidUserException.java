package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidUserException extends RuntimeException {
	private static final long serialVersionUID = 7537167259815318140L;

	public InvalidUserException(String message) {
		super(message);
	}

}
