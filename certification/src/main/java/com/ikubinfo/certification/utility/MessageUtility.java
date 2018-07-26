package com.ikubinfo.certification.utility;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageUtility {
	public final static ResourceBundle bundle = ResourceBundle.getBundle("com.Messages");

	
	public static void AddMessage(String severityType, String messageSummary,String messageDetail) {
		FacesMessage message = new FacesMessage(messageSummary, messageDetail);
		switch (severityType) {
		case "info":
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			break;
		case "error":
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			break;
		case "warn":
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			break;
		case "fatal":
			message.setSeverity(FacesMessage.SEVERITY_FATAL);
			break;
		default:
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			break;
		}
        requestContext().addMessage(null, message);
	}
	
	public static FacesContext requestContext() {
        return FacesContext.getCurrentInstance();
    }
}
