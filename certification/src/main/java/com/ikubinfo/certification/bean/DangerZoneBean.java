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

import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.SuccessMessages;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificateService;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;

@ManagedBean(name = "dangerZoneBean")
@ViewScoped
public class DangerZoneBean implements Serializable {

	private static Logger log = Logger.getLogger(DangerZoneBean.class);

	private static final long serialVersionUID = 5991371495845614205L;

	@ManagedProperty(value = "#{userService}")
	UserService userService;

	@ManagedProperty(value = "#{certificationService}")
	CertificationService certificationService;

	@ManagedProperty(value = "#{certificateService}")
	CertificateService certificateService;

	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;

	private EmployeeCertification certification;
	private Certificate certificate;
	private User employee;

	private ArrayList<EmployeeCertification> certifications, selectedCertifications;
	private ArrayList<Certificate> certificates, selectedCertificates;
	private ArrayList<User> employees, selectedEmployees;

	@PostConstruct
	public void init() {

		certification = new EmployeeCertification();
		certificate = new Certificate();
		employee = new User();

		selectedCertifications = new ArrayList<EmployeeCertification>();
		selectedCertificates = new ArrayList<Certificate>();
		selectedEmployees = new ArrayList<User>();

		try {
			refreshCertifications();
			refreshCertificates();
			refreshEmployees();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CertificationService getCertificationService() {
		return certificationService;
	}

	public void setCertificationService(CertificationService certificationService) {
		this.certificationService = certificationService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public EmployeeCertification getCertification() {
		return certification;
	}

	public void setCertification(EmployeeCertification certification) {
		this.certification = certification;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public ArrayList<EmployeeCertification> getCertifications() {
		return certifications;
	}

	public void setCertifications(ArrayList<EmployeeCertification> certifications) {
		this.certifications = certifications;
	}

	public ArrayList<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(ArrayList<Certificate> certificates) {
		this.certificates = certificates;
	}

	public ArrayList<User> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<User> employees) {
		this.employees = employees;
	}

	public ArrayList<EmployeeCertification> getSelectedCertifications() {
		return selectedCertifications;
	}

	public void setSelectedCertifications(ArrayList<EmployeeCertification> selectedCertifications) {
		this.selectedCertifications = selectedCertifications;
	}

	public ArrayList<Certificate> getSelectedCertificates() {
		return selectedCertificates;
	}

	public void setSelectedCertificates(ArrayList<Certificate> selectedCertificates) {
		this.selectedCertificates = selectedCertificates;
	}

	public ArrayList<User> getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(ArrayList<User> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}
	
	public String selectEmployee(int id) {
		employee = userService.findById(id);
		return "";
	}
	
	public String restoreEmployee(int id) {
		if (userService.restore(userService.findById(id))) {
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_RESTORED")));
			refreshEmployees();
		} else {
			addMessage(new FacesMessage(getError(), MessageUtility.getMessage("EMPLOYEE_RESTORE_FAIL")));
		}
		return "";
	}
	
	public String removeEmployee(int id) {
		try {
			if (userService.removePermanently(userService.findById(id))) {
				addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("EMPLOYEE_PERMANENTLY_DELETED")));
				refreshEmployees();
			} else {
				addMessage(new FacesMessage(getError(), MessageUtility.getMessage("EMPLOYEE_DELETE_FAIL")));
			}
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
		return "";
	}


	public String removeCertificate(int id) {
		try {
			if (certificateService.removePermanently(certificateService.findById(id))) {
				addMessage(
						new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATE_PERMANENTLY_DELETED")));
				refreshCertificates();
			} else {
				addMessage(new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATE_DELETE_FAIL")));
			}
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
		return "";
	}

	public String restoreCertificate(int id) {
		if (certificateService.restore(certificateService.findById(id))) {
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATE_RESTORED")));
			
			refreshCertificates();
		} else {
			addMessage(new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATE_RESTORE_FAIL")));
		}
		return "";
	}
	
	public String selectCertification(int id) {
		try{
			certification = certificationService.find(id);
		}catch (NullPointerException e) {
			certification = null;
		}
		return "";
	}
	
	public String removeCertification(int id) {
		if (certificationService.removePermanently(certificationService.find(id))) {
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATION_PERMANENTLY_DELETED")));
			refreshCertifications();
		} else {
			addMessage(new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATION_DELETE_FAIL")));
		}
		return "";
	}

	public String restoreCertification(int id) {
		if (certificationService.restore(certificationService.find(id))) {
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATION_RESTORED")));
			refreshCertifications();
		} else {
			addMessage(new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATION_RESTORE_FAIL")));
		}
		return "";
	}

	private void exceptionHandler(GeneralException exception) {
		if (exception != null) {
			addMessage(new FacesMessage(getError(), exception.getMessage()));
		}

	}

	private void addMessage(FacesMessage fm) {
		FacesContext.getCurrentInstance().addMessage(null, fm);
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

	private void refreshEmployees() {
		employees = userService.getAllDisabled(userBean.getUser().getId());
		
	}

	private void refreshCertificates() {
		certificates = certificateService.getAllDisabled();
	}

	private void refreshCertifications() {
		certifications = certificationService.getAllDisabled(userBean.getUser());
	}

	private String getSuccess() {
		return "Success!";
	}

	private String getError() {
		return "Error!";
	}
}
