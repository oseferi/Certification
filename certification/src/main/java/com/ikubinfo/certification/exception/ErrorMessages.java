package com.ikubinfo.certification.exception;

public enum ErrorMessages {
	DUBLICATE_CERTIFICATION ("This Certification is already assigned to the employee!"),
	PREVIOUSLY_DELETED_CERTIFICATION("This Certification has been previously assigned to the employee ,but has been deleted!");
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	private ErrorMessages(String message) {
		this.message = message;
	}
}
