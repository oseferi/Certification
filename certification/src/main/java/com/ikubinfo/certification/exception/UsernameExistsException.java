package com.ikubinfo.certification.exception;

public class UsernameExistsException extends Exception{

	private static final long serialVersionUID = -4597743312248181313L;

	public UsernameExistsException() {
		super();
	}

	public UsernameExistsException(String message) {
		super(message);
	}

	public UsernameExistsException(Throwable cause) {
		super(cause);
	}
	
	
	
}
