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
import org.primefaces.PrimeFaces;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.RoleService;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;

@ManagedBean(name="usersAdministrationBean")
@ViewScoped
public class UsersAdministrationBean implements Serializable {
	
	private static Logger log = Logger.getLogger(UsersAdministrationBean.class);
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
		selectedEmployee = new User();
		try{
			if(id!=null) {
				selectEmployeeForEdit(id); 
			}
		}catch(NullPointerException x) {
			log.warn("An error occured.There will be nothing displayed in the front-end");
			selectedEmployee = null;
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
				 addMessage(new FacesMessage("Employee Added!", MessageUtility.getMessage("EMPLOYEE_ADDED")+"Employee: "
						 					+employee.getName()+" "+employee.getSurname()) );
			     employee = new User();
				 refreshEmployees();
			 }else{
				addMessage(new FacesMessage("Error!", "Employee : "+employee.getName()+" could not be added!"
											+ " If this problem persists please contact the Administrator"));
				refreshEmployees();
			}
			
		}catch (GeneralException ge) {
			exceptionHandler(ge);
			refreshEmployees();
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
				addMessage(new FacesMessage(getError(), MessageUtility.getMessage("EMPLOYEE_SAME_PASSWORD")));
			    newPassword="";
			    disableEditing();
			    refreshEmployees();
			    return null;
			}else {
				selectedEmployee.setPassword(passwordEncryptor.encryptPassword(newPassword));
				log.info("Password was updated succesfully");
				addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_PASSWORD_UPDATED")));
			    newPassword="";
			    disableEditing();
			    refreshEmployees();
				PrimeFaces.current().executeScript("setTimeout( \" location.href = 'users.xhtml'; \" ,1500);");
			    return null;
			}
		}
		if(selectedEmployee.equals(user)) {
			disableEditing();
			refreshEmployees();
			addMessage(new FacesMessage("Info!", MessageUtility.getMessage("EMPLOYEE_NO_CHANGE")));
		    return null;
		}
		try {
			if(userService.update(selectedEmployee)) {
				disableEditing();
				refreshEmployees();
				selectedEmployee = new User();
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
	
	public String removeEmployee(int id) {
		try {
			if(userService.remove(userService.findById(id))) {
				refreshEmployees();
				selectedEmployee = new User();
				addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_DELETED")));
				}else {
					addMessage( new FacesMessage(getError(), MessageUtility.getMessage("EMPLOYEE_DELETE_FAIL")) );
				}
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
		return null;
		}
	
	public String selectEmployee(int id) {
		selectedEmployee = userService.findById(id);
		return "";
	}
	
	public String selectEmployeeForEdit(int id) {
		try {
			User temporalUser = userService.findById(id);
			if(temporalUser.getRole().getTitle() != null) {
				if(!temporalUser.getRole().getTitle().equals("Manager"))
					selectedEmployee = temporalUser;
				
				else {
					selectedEmployee = null;
				}
			}else {
				selectedEmployee = null;
			}
			return "";
		} catch (Exception e) {
			selectedEmployee = null;
			System.out.println("Below the error message for selectEmployee");
			e.printStackTrace();
			return "";
		}
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
	
	public String edit(int id) {
		
		return "edituser?faces-redirect=true&id="+id;
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
