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
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.SuccessMessages;
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
				addMessage(new FacesMessage(getError(), 
						   ErrorMessages.EMPLOYEE_SAME_PASSWORD.getMessage()));
			    newPassword="";
			    return null;
			}else {
				selectedEmployee.setPassword(passwordEncryptor.encryptPassword(newPassword));
				addMessage( new FacesMessage(getSuccess(), 
							SuccessMessages.EMPLOYEE_PASSWORD_UPDATED.getMessage()));
			    newPassword="";
			}
		}
		if(selectedEmployee.equals(user)) {
			disableEditing();
			addMessage( new FacesMessage("Info!", 
						SuccessMessages.EMPLOYEE_NO_CHANGE.getMessage()));
		    return null;
		}
		try {
			if(userService.update(selectedEmployee)) {
				disableEditing();
				selectedEmployee = new User();
				addMessage( new FacesMessage(getSuccess(), 
							SuccessMessages.EMPLOYEE_UPDATED.getMessage()) );
			}
			else {
				disableEditing();
				addMessage( new FacesMessage(getError(),
							ErrorMessages.EMPLOYEE_UPDATE_FAIL.getMessage()+" If the problem persists please contact the Administrator.") );
			}
		} catch (GeneralException e) {
			disableEditing();
			log.warn(ErrorMessages.EMPLOYEE_UPDATE_FAIL.getMessage());
			exceptionHandler(e);
		} 
		return null;
	}

	public String updateCertification() {
			try {
				certificationService.edit(selectedCertificate);
				setStatusesList();
				addMessage( new FacesMessage(getSuccess(),
							SuccessMessages.CERTIFICATION_UPDATED.getMessage()) );
				
			} catch (GeneralException e) {
				exceptionHandler(e);
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
