package com.ikubinfo.certification.exception;

public enum ErrorMessages {
	DUPLICATE_CERTIFICATION ("Certification is already assigned to this employee!",0),
	PREVIOUSLY_DELETED_CERTIFICATION("Certification has been previously assigned to this employee, but has been soft deleted!",1),
	DUPLICATE_EMPLOYEE_USERNAME("Username already belongs to another existing Employee!",2),
	DUPLICATE_EMPLOYEE_EMAIL("Email Address already belongs to another existing Employee!",3),
	DUPLICATE_EMPLOYEE_FULL_NAME("Full Name already belongs to another existing Employee!",4),
	DUPLICATE_EMPLOYEE_PHONE("Phone Number already belongs to another existing Employee!",5),
	DUPLICATE_EMPLOYEE_SSN("SSN already belongs to another existing Employee!",6),
	PREVIOUSLY_DELETED_EMPLOYEE("Employee already exists, but has been soft deleted!",7),
	DUPLICATE_CERTIFICATE("Certificate already exists!",8),
	PREVIOUSLY_DELETED_CERTIFICATE("Certificate already exists, but has been soft deleted!",9);
	
	private String message;
	private int errorCode;
	
	public String getMessage() {
		return message;
	}
	
	public int errorCode() {
		return errorCode;
	}
	private ErrorMessages(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}
}
