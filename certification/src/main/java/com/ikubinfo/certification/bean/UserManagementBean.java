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
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
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
				 log.info("Employee: "+employee.getName()+" was added!");
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Added!", "Employee : "+employee.getName()+" was added succesfully!") );
			     employee = new User();
				 refreshEmployees();
			 }else{
				log.fatal("Employee: "+employee.getName()+" failed to be added!");
				FacesContext context = FacesContext.getCurrentInstance();
			    context.addMessage(null, new FacesMessage("Error!", "Employee : "+employee.getName()+" could not be added! If this problem persists please contact the Administrator") );
			}
			
		}catch (DeletedUserException e) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("Employee : "+employee.getName()+" already exists but is deleted!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee : "+employee.getName()+" has been previously deleted!") );
		}
		catch (UsernameExistsException u) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("Username: "+employee.getUsername()+" already belongs to existing Employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Username : "+employee.getUsername()+" already belongs to existing Employee!") );
		}
		catch (SsnExistsException s) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("SSN: "+employee.getSsn()+" already belongs to existing Employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "SSN : "+employee.getSsn()+" already belongs to existing Employee!") );
		}
		catch (FullNameExistsException f) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("Full Name : "+employee.getName()+" "+employee.getSurname()+" already belongs to existing Employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Full Name : "+employee.getName()+" "+employee.getSurname()+" already belongs to existing Employee!") );
		}catch (PhoneNumberExistsException pn) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("Phone Number : "+employee.getPhoneNumber()+" already belongs to existing Employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Phone Number : "+employee.getPhoneNumber()+" already belongs to existing Employee!") );
		}catch (EmailExistsException ex) {
			log.error("Employee: "+employee.getName()+" failed to be added!");
			log.info("Email Address : "+employee.getEmail()+" already belongs to existing Employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Email Address : "+employee.getEmail()+" already belongs to existing Employee!") );
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
				log.info("Password was not updated");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "The new password is the same as the old one.Please enter a valid password!"));
			    newPassword="";
			    return null;
			}else {
				selectedEmployee.setPassword(passwordEncryptor.encryptPassword(newPassword));
				log.info("Password was updated succesfully");
				System.out.println("Password was updated succesfully");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "The password was updated succesfully!"));
			    newPassword="";
			}
		}
		if(selectedEmployee.equals(user)) {
			disableEditing();
			log.info("No changes have been made to employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info!", "No changes have been made to employee :"+selectedEmployee.getName()+" "+selectedEmployee.getSurname()) );
		    return null;
		}
		try {
			if(userService.update(selectedEmployee)) {
				disableEditing();
				refreshEmployees();
				selectedEmployee = new User();
				log.info("Employee updated succesfully!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "Employee was updated Succesfully!") );
			}
			else {
				disableEditing();
				log.fatal("Employee failed to be updated!!");
				log.info("Unknown error[Returns False]");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!! If the problem persists please contact the Administrator.") );
			}
		} catch (DeletedUserException e) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("Username/SSN/Full Name conflicts with a deleted employee");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Username/SSN/Full Name conflicts with a deleted employee. If the problem persists please contact the Administrator.") );
		} catch (UsernameExistsException e) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("Username "+selectedEmployee.getUsername()+" belongs to another employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Username "+selectedEmployee.getUsername()+" belongs to another employee!") );
		} catch (SsnExistsException e) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("SSN "+selectedEmployee.getSsn()+" belongs to another employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!SSN "+selectedEmployee.getSsn()+" belongs to another employee!") );
		} catch (FullNameExistsException e) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("Full Name "+selectedEmployee.getName()+" "+selectedEmployee.getSurname()+" belongs to another employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Full Name "+selectedEmployee.getName()+" "+selectedEmployee.getSurname()+" belongs to another employee!") );
		} catch (PhoneNumberExistsException ph) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("Phone Number "+selectedEmployee.getPhoneNumber()+" belongs to another employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Phone Number "+selectedEmployee.getPhoneNumber()+" belongs to another employee!") );
		} catch (EmailExistsException ex) {
			disableEditing();
			log.warn("Employee failed to be updated!!");
			log.info("Phone Number "+selectedEmployee.getPhoneNumber()+" belongs to another employee!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Phone Number "+selectedEmployee.getEmail()+" belongs to another employee!") );
		}
		return null;
		}
	
	public String removeEmployee(int id) {
		log.info("removeEmployee Init");
		if(userService.remove(userService.findById(id))) {
			refreshEmployees();
			selectedEmployee = new User();
			log.info("Employee deleted succesfully!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "Employee was deleted Succesfully!") );
		}else {
			log.info("Employee could not be deleted!");
			System.out.println("User could not be deleted!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Employee failed to be deleted!") );
		}
		
		return null;

		}
	
	public String selectEmployee(int id) {
		selectedEmployee = userService.findById(id);
		log.info("selectEmployee executed");
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
	
	
}
