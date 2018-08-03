package com.ikubinfo.certification.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;

import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.EmailExistsException;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.SuccessMessages;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.RoleService;
import com.ikubinfo.certification.service.UserService;

@ManagedBean(name="userManagementBean")
@ViewScoped
public class UserManagementBean implements Serializable {
	
	private static Logger log = Logger.getLogger(UserManagementBean.class);
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{userService}")
	UserService userService;
	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;
	@ManagedProperty(value = "#{roleService}")
	RoleService roleService;
	
	private User employee;
	private ArrayList<User> employees;
	private User selectedEmployee;
	private boolean selectedEmployeeEditable;
	private boolean selectedEmployeeCredentialsEditable;	
	private List<User> filteredEmployees;
	private String newPassword;
	private Integer id; 
	
	 @PostConstruct
	 public void init() {
		refreshEmployees(); 
		employee = new User();
		if(id!=null) {
			selectEmployee(id); 
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
	
	public List<User> getFilteredEmployees() {
		return filteredEmployees;
	}
	public void setFilteredEmployees(List<User> filteredEmployees) {
		this.filteredEmployees = filteredEmployees;
	}
	
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public boolean isSelectedEmployeeEditable() {
		return selectedEmployeeEditable;
	}
	public void setSelectedEmployeeEditable(boolean selectedEmployeeEditable) {
		this.selectedEmployeeEditable = selectedEmployeeEditable;
	}
	
	public boolean isSelectedEmployeeCredentialsEditable() {
		return selectedEmployeeCredentialsEditable;
	}
	public void setSelectedEmployeeCredentialsEditable(boolean selectedEmployeeCredentialsEditable) {
		this.selectedEmployeeCredentialsEditable = selectedEmployeeCredentialsEditable;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@SuppressWarnings("finally")
	public String addEmployee() {
		try {
			 employee.setManager(userBean.getUser());
			 employee.setRole(roleService.findById(2));
			 if(userService.add(employee)) {
				 addMessage(new FacesMessage("Employee Added!", SuccessMessages.EMPLOYEE_ADDED.getMessage()+"Employee: "
						 					+employee.getName()+" "+employee.getSurname()) );
			     employee = new User();
				 refreshEmployees();
			 }else{
				addMessage(new FacesMessage("Error!", "Employee : "+employee.getName()+" could not be added!"
											+ " If this problem persists please contact the Administrator"));
			}
			
		}catch (GeneralException ge) {
			exceptionHandler(ge);
		}  
		
		finally {
			return "";
		}
		
	}
	
	public String updateEmployee(int id) {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		User user = userService.findById(id);
		if(newPassword!=null && newPassword!="") {
			if(passwordEncryptor.checkPassword(newPassword, user.getPassword())) {
				log.warn("New password is the same as the old one");
				new FacesMessage("Error!", "The new password is the same as the old one.Please enter a valid password!");
			    newPassword="";
			    return null;
			}else {
				selectedEmployee.setPassword(passwordEncryptor.encryptPassword(newPassword));
				log.info("Password was updated succesfully");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "The password was updated succesfully!"));
			    newPassword="";
			}
		}
		if(selectedEmployee.equals(user)) {
			disableEditing();
			addMessage(new FacesMessage("Info!", SuccessMessages.EMPLOYEE_NO_CHANGE.getMessage()));
		    return null;
		}
		try {
			if(userService.update(selectedEmployee)) {
				disableEditing();
				refreshEmployees();
				selectedEmployee = new User();
				addMessage(new FacesMessage(getSuccess(), SuccessMessages.EMPLOYEE_UPDATED.getMessage()) );
			}
			else {
				disableEditing();
				log.info("Unknown error[Returns False]");
				addMessage(new FacesMessage("Error!", "Employee failed to be updated!! "
											+ "If the problem persists please contact the Administrator.") );
			}
		}catch (GeneralException ge) {
			disableEditing();
			log.warn("Employee failed to be updated!!"); 
			exceptionHandler(ge);
		} 
		return null;
		}
	
	public String removeEmployee(int id) {
		if(userService.remove(userService.findById(id))) {
			refreshEmployees();
			selectedEmployee = new User();
			addMessage(new FacesMessage(getSuccess(), SuccessMessages.EMPLOYEE_DELETED.getMessage()));
			}else {
				addMessage( new FacesMessage(getError(), ErrorMessages.EMPLOYEE_DELETE_FAIL.getMessage()) );
			}
		return null;
		}
	
	public String selectEmployee(int id) {
		selectedEmployee = userService.findById(id);
		return "";
	}
	public void refreshEmployees() {
		employees = userService.getAllActive(userBean.getUser().getId());
	}

	public void enableEditing() {
		selectedEmployeeEditable = true;
	}
	
	public void disableEditing() {
		selectedEmployeeEditable = false;
		selectedEmployeeCredentialsEditable = false;
	}
	
	public void enableCredentialsEditing() {
		selectedEmployeeCredentialsEditable = true;
	}
	public String edit() {
		return "edit?faces-redirect=true&id="+getId();
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
	
}
