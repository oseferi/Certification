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

import com.ikubinfo.certification.exception.CertificateExistsException;
import com.ikubinfo.certification.exception.DeletedCertificateException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificateService;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.TechnologyService;
import com.ikubinfo.certification.service.UserService;

@ManagedBean(name="managerCertificationBean")
@ViewScoped
public class ManagerCertificationBean implements Serializable{

	private static Logger log = Logger.getLogger(ManagerCertificationBean.class);
	private static final long serialVersionUID = -590470498129555192L;
	
	@ManagedProperty(value = "#{userBean}")
	UserBean user;
	@ManagedProperty(value = "#{certificationService}")
	CertificationService certificationService;
	@ManagedProperty(value = "#{technologyService}")
	TechnologyService technologyService;
	@ManagedProperty(value = "#{certificateService}")
	CertificateService certificateService;
	@ManagedProperty(value="#{userService}")
	UserService userService;
	
	private EmployeeCertification certification;
	private Certificate certificate;
	private ArrayList<EmployeeCertification> certifications;
	private ArrayList<EmployeeCertification> filteredCertifications;
	private ArrayList<Certificate> certificates;
	private ArrayList<Technology> technologies;
	private ArrayList<User> employees;
	
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

	public ArrayList<EmployeeCertification> getCertifications() {
		return certifications;
	}

	public void setCertifications(ArrayList<EmployeeCertification> certifications) {
		this.certifications = certifications;
	}
	
	public ArrayList<EmployeeCertification> getFilteredCertifications() {
		return filteredCertifications;
	}
	public void setFilteredCertifications(ArrayList<EmployeeCertification> filteredCertifications) {
		this.filteredCertifications = filteredCertifications;
	}

	public ArrayList<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(ArrayList<Certificate> certificates) {
		this.certificates = certificates;
	}
	
	public ArrayList<Technology> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(ArrayList<Technology> technologies) {
		this.technologies = technologies;
	}

	public ArrayList<User> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<User> employees) {
		this.employees = employees;
	}

	@PostConstruct
	public void init() {
		certification = new EmployeeCertification();
		certificate = new Certificate();
		refreshCertifications();
		refreshCertificates();
		refreshTechnologies();
		refreshEmployees();
	}
	
	@SuppressWarnings("finally")
	public String addCertificate() {
		try {
			certificateService.add(certificate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success!","Certificate added successfully!"));
			log.warn("Certificate Already exists!");
		} catch (CertificateExistsException ce) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!","Certificate Already exists!"));
			log.warn("Certificate Already exists!");
			
		}
		catch (DeletedCertificateException dc) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!","Certificate has been previously deleted!"));
			log.warn("Certificate has been previously deleted!");
		}
		finally {
			return "";
		}
	}
	
	public void refreshCertifications() {
		certifications = certificationService.getAllActive(user.getUser());
	}
	public void refreshCertificates() {
		certificates = certificateService.getAllActive();
	}
	public void refreshTechnologies() {
		technologies = technologyService.getAllActive();
	}
	public void refreshEmployees() {
		employees = userService.getAllActive(user.getUser().getId());
	}
	public String edit(EmployeeCertification certification) {
		return "";
	}
}

