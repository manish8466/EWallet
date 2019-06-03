package com.example.wallet.exceptions;

/**
 * @author Manish Doodi
 *
 */

public class UserNotFoundException extends Exception {
	
    private static final long serialVersionUID = 7537167259815318140L;

	private int status;
	private String message;
	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(int status , String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
