package com.ikubinfo.certification.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ikubinfo.certification.converter.UserConverter;
import com.ikubinfo.certification.dto.PasswordDto;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;

@ManagedBean(name="profileBean")
@ViewScoped
public class ProfileBean implements Serializable{
	
	private static final long serialVersionUID = 7705654587086726526L;
	private Logger log = Logger.getLogger(ProfileBean.class);
	
	@ManagedProperty(value = "#{userService}")
	UserService userService;
	
	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;
	
	private PasswordDto passwordDto;
	private User user;
	private boolean userEditable;

	@PostConstruct
	public void init() {
		user = userBean.getUser();
		passwordDto = UserConverter.toPasswordDto(user);
	}
	
	public void changePassword() {
		try {
			if(userService.changePassword(passwordDto)) {
				addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_PASSWORD_UPDATED")));
				PrimeFaces.current().executeScript("setTimeout( \" location.href = 'profile.xhtml'; \" ,1500);");
			}else {
				addMessage(new FacesMessage(getError(), MessageUtility.getMessage("EMPLOYEE_UPDATE_PASSWORD_FAIL")));
			}
			
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
	}
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public PasswordDto getPasswordDto() {
		return passwordDto;
	}

	public void setPasswordDto(PasswordDto passwordDto) {
		this.passwordDto = passwordDto;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isUserEditable() {
		return userEditable;
	}

	public void setUserEditable(boolean userEditable) {
		this.userEditable = userEditable;
	}

	public void enableEditing() {
		userEditable = true;
	}
	
	
	
	public String update() {
	if(user.equals(userBean.getUser())) {
		disableEditing();
		addMessage(new FacesMessage("Info!", MessageUtility.getMessage("EMPLOYEE_NO_CHANGE")));
	    return null;
	}
	try {
		if(userService.update(user)) {
			disableEditing();
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_UPDATED")) );
		}
		else {
			disableEditing();
			log.info("Unknown error[Returns False]");
			addMessage(new FacesMessage("Error!", MessageUtility.getMessage("EMPLOYEE_UPDATE_FAIL")
										+ "If the problem persists please contact the Administrator.") );
		}
	}catch (GeneralException ge) {
		disableEditing();
		log.warn("Employee failed to be updated!!"); 
		exceptionHandler(ge);
	} 
	return null;
	
	}
	
	private void exceptionHandler(GeneralException exception) {
		if(exception!=null) {
			addMessage(new FacesMessage(getError(),exception.getMessage()));
		}
		
	}
	private void addMessage(FacesMessage fm) {
		FacesContext.getCurrentInstance().addMessage(null, fm );
		String severity = fm.getSummary();
		switch (severity) {
		case "Error!":
			log.warn(fm.getDetail());
			break;
		case "Success!":
			log.info(fm.getDetail());
			break;
		default:
			log.info(fm.getDetail());
			break;
		}
	}
	private String getSuccess() {
		return "Success!";
	}
	private String getError() {
		return "Error!";
	}
	
	public void disableEditing() {
		userEditable = false;
	}

}
