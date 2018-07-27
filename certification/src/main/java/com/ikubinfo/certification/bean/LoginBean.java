package com.ikubinfo.certification.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.Log4JExample;

@ManagedBean(name="loginBean")
@RequestScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginBean.class);

	
	private String username;
	private String password;
	private boolean validCredentials;
	
	@ManagedProperty(value = "#{userService}")
	UserService userService;
	
	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;
	
	public String logIn() {
		if(username!=null && password!=null) {
			User user = userService.exists(username, password);
			if(user!=null) {
				String role = user.getRole().getTitle();
				if(role.equals("Manager")) {
					//System.out.println("Manager Successfully logged in");
					log.info("Manager Successfully logged in!");
					userBean.setUser(user);
					return "administration/index?faces-redirect=true";
				}
				else if (role.equals("Employee")) {
					//System.out.println("Employee Successfully logged in");
					log.info("Employee Successfully logged in!");
					userBean.setUser(user);
					return "employee/index?faces-redirect=true";
				}
				else {
					//System.out.println("Unknown role type");
					log.error("Unknown role type!");
					validCredentials = false;
			        FacesContext context = FacesContext.getCurrentInstance();
			        context.addMessage(null, new FacesMessage("Error", "Wrong username or password!") );
				    return "";
				    
				}
			}
			else {
				log.warn("User failed to log in!");
				log.info("User is null!");
				//System.out.println("User failed to log in! (User is null)");
		        FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(null, new FacesMessage("Error", "Wrong username or password!") );
			    return "";
			}
		}
		else {
			//System.out.println("One of the credentials is empty!");
			log.warn("One of the credentials is empty!");
			return null;
		}
	}
	
	public String logOut() {
		userBean.logOut();
		//System.out.println("User Logged out!");
		log.info("User Logged out!");
		return "/login.xhtml?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidCredentials() {
		return validCredentials;
	}

	public void setValidCredentials(boolean validCredentials) {
		this.validCredentials = validCredentials;
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
	
	public LoginBean() {
		
	}
}
