package com.ikubinfo.certification.bean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import com.ikubinfo.certification.exception.CertificationException;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.EmailExistsException;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificateService;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.TechnologyService;
import com.ikubinfo.certification.service.UserService;

@ManagedBean(name = "editBean")
@ViewScoped
public class EditBean implements Serializable {

	private static final long serialVersionUID = -7649663954102700577L;

	private static Logger log = Logger.getLogger(EditBean.class);

	@ManagedProperty(value = "#{userBean}")
	UserBean user;
	@ManagedProperty(value = "#{certificationService}")
	CertificationService certificationService;
	@ManagedProperty(value = "#{technologyService}")
	TechnologyService technologyService;
	@ManagedProperty(value = "#{certificateService}")
	CertificateService certificateService;
	@ManagedProperty(value = "#{userService}")
	UserService userService;

	private Integer id;
	private String object;
	private String newPassword;
	private User selectedEmployee;
	private EmployeeCertification selectedCertificate;
	private ArrayList<Boolean> statuses;
	private boolean selectedEmployeeEditable;
	private boolean selectedEmployeeCredentialsEditable;
	
	
	@PostConstruct
	public void init() {
		if(object!=null) {
			if(object.equals("employee") && id!=null) {
				selectEmployee(id);
			}else if(object.equals("certification") && id!=null) {
				selectCertificate(id);
				statuses = new ArrayList<Boolean>();
				setStatusesList();
			}
		}
	}
	
	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public CertificationService getCertificationService() {
		return certificationService;
	}

	public void setCertificationService(CertificationService certificationService) {
		this.certificationService = certificationService;
	}

	public TechnologyService getTechnologyService() {
		return technologyService;
	}

	public void setTechnologyService(TechnologyService technologyService) {
		this.technologyService = technologyService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public User getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(User selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}
	
	public EmployeeCertification getSelectedCertificate() {
		return selectedCertificate;
	}


	public void setSelectedCertificate(EmployeeCertification selectedCertificate) {
		this.selectedCertificate = selectedCertificate;
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

	public ArrayList<Boolean> getStatuses() {
		return statuses;
	}

	public void setStatuses(ArrayList<Boolean> statuses) {
		this.statuses = statuses;
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

	public String updateCertification() {
			try {
				certificationService.edit(selectedCertificate);
				setStatusesList();
				log.info("Certification updated successfully!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "Certification updated successfully!") );
				
			} catch (CertificationException e) {
				if(e.getMessage().equals(ErrorMessages.DUPLICATE_CERTIFICATION.getMessage())) {
					log.info("Certification is already assigned to employee!");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Certification is already assigned to employee!") );
				}else if(e.getMessage().equals(ErrorMessages.PREVIOUSLY_DELETED_CERTIFICATION.getMessage())) {
					log.info("Certification has been previously assigned to employee!");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!", "Certification has been previously assigned to employee but has been deleted!") );
				}
				else {
					log.error(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "error message:"+e.getMessage()) );
				}
			}
			return "";
		
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
	public String selectEmployee(int id) {
		selectedEmployee = userService.findById(id);
		return "";
	}
	public String selectCertificate(int id) {
		selectedCertificate = certificationService.find(id);
		return "";
	}	
	public void setStatusesList() {
		if(selectedCertificate.getStatus()==null) {
			statuses.add(null);statuses.add(true);statuses.add(false);
		}else if(selectedCertificate.getStatus()) {
			statuses.add(true);statuses.add(false);statuses.add(null);
		}else if(!selectedCertificate.getStatus()) {
			statuses.add(false);statuses.add(true);statuses.add(null);
		}
	}
}
