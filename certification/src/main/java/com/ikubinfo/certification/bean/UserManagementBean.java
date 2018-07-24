package com.ikubinfo.certification.bean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;


@ManagedBean(name="userManagementBean")
@ViewScoped
public class UserManagementBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{userService}")
	UserService userService;
	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;
	
	private User employee;
	private ArrayList<User> employees;
	private User selectedEmployee;
	
	
	 @PostConstruct
	 public void init() {
		refreshEmployees(); 
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
	public User getEmployee() {
		return employee;
	}
	public void setEmployee(User employee) {
		this.employee = employee;
	}
	public ArrayList<User> getEmployees() {
		return employees;
	}
	public void setEmployees(ArrayList<User> employees) {
		this.employees = employees;
	}
	
	public User getSelectedEmployee() {
		return selectedEmployee;
	}
	public void setSelectedEmployee(User selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}
	
	public String addEmployee() {return null;}
	
	public String updateEmployee(int id) {return null;}
	
	public String removeEmployee(int id) {return null;}
	
	public void refreshEmployees() {
		employees = userService.getAll(userBean.getUser().getId());
	}

	
}
