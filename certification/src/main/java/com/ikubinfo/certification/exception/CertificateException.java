package com.ikubinfo.certification.exception;

public class CertificateException extends Exception{

	private static final long serialVersionUID = 3068902405360440639L;
	private int errorCode;
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public CertificateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CertificateException(String message, int errorCode) {
		super(message);
		this.errorCode=errorCode;
	}
	
	

}
