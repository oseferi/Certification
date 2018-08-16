package com.ikubinfo.certification.utility;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

public class MessageUtility {
	
	private final static ResourceBundle bundle = ResourceBundle.getBundle("com.ikubinfo.certification.utility.messages");
	
	public static void addMessage(String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary, detail));
	}
	
	public static String getMessage(String key) {
		return bundle.getString(key);
	}
}
