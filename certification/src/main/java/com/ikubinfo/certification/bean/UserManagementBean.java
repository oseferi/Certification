package com.ikubinfo.certification.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Convert;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;

import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.RoleService;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;



@ManagedBean(name="userManagementBean")
@ViewScoped
public class UserManagementBean implements Serializable {
	
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
	private List<User> filteredEmployees;
	
	 @PostConstruct
	 public void init() {
		refreshEmployees(); 
		employee = new User();

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
	
	@SuppressWarnings("finally")
	public String addEmployee() {
		try {
			 employee.setManager(userBean.getUser());
			 employee.setRole(roleService.findById(2));
			 if(userService.add(employee)) {
				 employee = new User();
				 refreshEmployees();
				 System.out.println("User was added!");
				 FacesContext context = FacesContext.getCurrentInstance();
			     context.addMessage(null, new FacesMessage("Employee Added!", "Employee : "+employee.getName()+" was added succesfully!") );
			}else{
				System.out.println("User failed to be added!");
				FacesContext context = FacesContext.getCurrentInstance();
			    context.addMessage(null, new FacesMessage("Error!", "Employee : "+employee.getName()+" could not be added! If this problem persists please contact the Administrator") );
			}
			
		}catch (DeletedUserException e) {
			System.out.println("Error! User already exists but is deleted!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee : "+employee.getName()+" has been previously deleted!") );
		}
		catch (UsernameExistsException u) {
			System.out.println("Error! Username already belongs to existing Employee!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Username : "+employee.getUsername()+" already belongs to existing Employee!") );
		}
		catch (SsnExistsException s) {
			System.out.println("Error! SSN already belongs to existing Employee!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "SSN : "+employee.getSsn()+" already belongs to existing Employee!") );
		}
		catch (FullNameExistsException f) {
			System.out.println("Error! Full Name already belongs to existing Employee!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Full Name : "+employee.getName()+" "+employee.getSurname()+" already belongs to existing Employee!") );
		}
		finally {
			return "";
		}
		
	}
	
	public String updateEmployee(int id) {
		User user = userService.findById(id);
		if(selectedEmployee.equals(user)) {
			disableEditing();
			System.out.println("No changes have been made to employee!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Info!", "No changes have been made to employee :"+selectedEmployee.getName()+" "+selectedEmployee.getSurname()) );
		    return "";
		}
		try {
			if(userService.update(selectedEmployee)) {
				disableEditing();
				refreshEmployees();
				selectedEmployee = new User();
				System.out.println("User updated succesfully!");
				FacesContext context = FacesContext.getCurrentInstance();
			    context.addMessage(null, new FacesMessage("Success!", "Employee was updated Succesfully!") );
			}
			else {
				disableEditing();
				System.out.println("User failed to be updated!! General error.");
				FacesContext context = FacesContext.getCurrentInstance();
			    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!! If the problem persists please contact the Administrator.") );
			}
		} catch (DeletedUserException e) {
			disableEditing();
			System.out.println("User failed to be updated!!Username/SSN/Full Name conflicts with a deleted employee");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Username/SSN/Full conflict with a deleted employee. If the problem persists please contact the Administrator.") );
		} catch (UsernameExistsException e) {
			disableEditing();
			System.out.println("User failed to be updated!!Username belongs to another employee");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Username "+selectedEmployee.getUsername()+" belongs to another employee!") );
		} catch (SsnExistsException e) {
			disableEditing();
			System.out.println("User failed to be updated!!SSN belongs to another employee");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!SSN "+selectedEmployee.getSsn()+" belongs to another employee!") );
		} catch (FullNameExistsException e) {
			disableEditing();
			System.out.println("User failed to be updated!!Full Name belongs to another employee");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be updated!!Full Name "+selectedEmployee.getName()+" "+selectedEmployee.getSurname()+" belongs to another employee!") );
		}
		return null;
		}
	
	public String removeEmployee(int id) {
		
		if(userService.remove(userService.findById(id))) {
			refreshEmployees();
			selectedEmployee = new User();
			System.out.println("User deleted succesfully!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Success!", "Employee was deleted Succesfully!") );
		}else {
			System.out.println("User was not deleted succesfully!");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.addMessage(null, new FacesMessage("Error!", "Employee failed to be deleted!") );
		}
		
		return null;

		}
	
	public void refreshEmployees() {
		employees = userService.getAllActive(userBean.getUser().getId());
	}

	public void enableEditing() {
		selectedEmployeeEditable = true;
	}
	public void disableEditing() {
		selectedEmployeeEditable = false;
	}
}
