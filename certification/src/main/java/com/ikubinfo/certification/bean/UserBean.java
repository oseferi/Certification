package com.ikubinfo.certification.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.ikubinfo.certification.model.User;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private User user;
	private String password;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void logOut() {
		user = null;
	}
	
	public UserBean() {
		
	}
}
