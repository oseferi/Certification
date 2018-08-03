package com.ikubinfo.certification.exception;


public enum SuccessMessages {
	CERTIFICATION_ASSIGNED("Certification was assigned successfully!"),
	CERTIFICATION_UPDATED("Certification was updated successfully!"),
	CERTIFICATION_NO_CHANGE("No changes have been made to Certification!"),
	CERTIFICATION_UNASSIGNED("Certification has been unassgined successfully!"),
	CERTIFICATE_ADDED("Certificate was added successfully!"),
	CERTIFICATE_UPDATED("Certificate was updated successfully!"),
	CERTIFICATE_NO_CHANGE("Certificate has no changes!"),
	CERTIFICATE_DELETED("Certificate has been deleted successfully!"),
	EMPLOYEE_ADDED("Employee was added successfully!"),
	EMPLOYEE_UPDATED("Employee was updated successfully!"),
	EMPLOYEE_PASSWORD_UPDATED("Password was updated successfully!"),
	EMPLOYEE_NO_CHANGE("No changes have been made to Employee!"),
	EMPLOYEE_DELETED("Employee has been deleted successfully!"),;
	
	private String message;
	public String getMessage() {
		return message; 
	}
	private SuccessMessages(String message) {
		this.message = message;
	}
	
}
