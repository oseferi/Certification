package com.ikubinfo.certification.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Init;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.ikubinfo.certification.service.CertificateService;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.UserService;

@ManagedBean(name = "dashboardBean")
@RequestScoped
public class DashboardBean implements Serializable{

	
	private static final long serialVersionUID = -597743384576998368L;

	@ManagedProperty(value="#{userService}")
	UserService userService;
	
	@ManagedProperty(value="#{certificationService}")
	CertificationService certificationService;
	
	@ManagedProperty(value="#{certificateService}")
	CertificateService certificateService;
	
	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;
	
	private int numberOfEmployees;
	private int numberOfCertifications;
	private int numberOfDeletedRows;
	
	@PostConstruct
	public void init() {
		try {
			numberOfEmployees = userService.getTotalRows(userBean.getUser().getId());
			numberOfCertifications = certificationService.getTotalRows(userBean.getUser().getId());
			numberOfDeletedRows = userService.getTotalDeletedRows(userBean.getUser().getId()) + 
									certificateService.getTotalDeletedRows() + 
										certificationService.getTotalDeletedRows(userBean.getUser().getId());
					
		} catch (Exception e) {
			numberOfEmployees = 0;
			numberOfCertifications = 0;
			numberOfDeletedRows =0;
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

	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public int getNumberOfCertifications() {
		return numberOfCertifications;
	}

	public void setNumberOfCertifications(int numberOfCertifications) {
		this.numberOfCertifications = numberOfCertifications;
	}

	public int getNumberOfDeletedRows() {
		return numberOfDeletedRows;
	}

	public void setNumberOfDeletedRows(int numberOfDeletedRows) {
		this.numberOfDeletedRows = numberOfDeletedRows;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	
	
}