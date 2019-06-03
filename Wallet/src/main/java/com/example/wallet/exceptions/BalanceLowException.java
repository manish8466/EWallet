package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Manish.Doodi
 *
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BalanceLowException extends RuntimeException {

	private static final long serialVersionUID = 7537167259815318140L;

	public BalanceLowException(String message) {
		super(message);
	}

}
