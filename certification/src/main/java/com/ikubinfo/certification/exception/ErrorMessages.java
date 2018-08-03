package com.ikubinfo.certification.exception;

public enum ErrorMessages {
	CERTIFICATION_DUPLICATE ("Certification is already assigned to this employee!"),
	CERTIFICATION_PREVIOUSLY_DELETED("Certification has been previously assigned to this employee, but has been soft deleted!"),
	CERTIFICATE_UNASSIGN_FAIL("Certification failed to be unassgined!"),
	EMPLOYEE_DUPLICATE_USERNAME("Username already belongs to another active Employee!"),
	EMPLOYEE_DUPLICATE_EMAIL("Email Address already belongs to another active Employee!"),
	EMPLOYEE_DUPLICATE_FULL_NAME("Full Name already belongs to another active Employee!"),
	EMPLOYEE_DUPLICATE_PHONE("Phone Number already belongs to another active Employee!"),
	EMPLOYEE_DUPLICATE_SSN("SSN already belongs to another active Employee!"),
	EMPLOYEE_PREVIOUSLY_DELETED("Employee already exists, but has been soft deleted!"),
	EMPLOYEE_DELETE_FAIL("Employee failed to be deleted!"),
	EMPLOYEE_UPDATE_FAIL("Employee failed to be updated!"),
	EMPLOYEE_SAME_PASSWORD("The new password is the same as the old one!"),
	EMPLOYEE_UPDATE_PASSWORD_FAIL("Password failed to be updated!"),
	CERTIFICATE_DELETE_FAIL("Certificate failed to be deleted!"),
	CERTIFICATE_DUPLICATE("Certificate already exists!"),
	CERTIFICATE_PREVIOUSLY_DELETED("Certificate already exists, but has been soft deleted!");
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	private ErrorMessages(String message) {
		this.message = message;
	}
}
