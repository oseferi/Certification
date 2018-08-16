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
import org.primefaces.event.RowEditEvent;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.SuccessMessages;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.utility.MessageUtility;

@ManagedBean(name = "userCertificationBean")
@ViewScoped
public class UserCertificationBean implements Serializable{

	private static Logger log = Logger.getLogger(UserCertificationBean.class);
	private static final long serialVersionUID = 3992705742006284117L;

	@ManagedProperty(value="#{userBean}")
	UserBean user;
	
	@ManagedProperty(value="#{certificationService}")
	CertificationService certificationService;
	
	private ArrayList<EmployeeCertification> certifications,backUpCertifications,filteredCertifications;
	private String status;
	private String query;
	
	@PostConstruct
	public void init() {
		refreshCertifications() ;
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
	public ArrayList<EmployeeCertification> getCertifications() {
		return certifications;
	}
	public void setCertifications(ArrayList<EmployeeCertification> certifications) {
		this.certifications = certifications;
	}
	public ArrayList<EmployeeCertification> getBackUpCertifications() {
		return backUpCertifications;
	}

	public void setBackUpCertifications(ArrayList<EmployeeCertification> backUpCertifications) {
		this.backUpCertifications = backUpCertifications;
	}

	public ArrayList<EmployeeCertification> getFilteredCertifications() {
		return filteredCertifications;
	}
	public void setFilteredCertifications(ArrayList<EmployeeCertification> filteredCertifications) {
		this.filteredCertifications = filteredCertifications;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void editRow(RowEditEvent event) {
		EmployeeCertification certification1 = (EmployeeCertification)event.getObject();
		certification1.setStatus(convertStatus(status));
		try {
			certificationService.edit(certification1);
			addMessage(new FacesMessage(getSuccess(),MessageUtility.getMessage("CERTIFICATION_UPDATED")));
		} catch (GeneralException e) {
			exceptionHandler(e);
		}finally {
			refreshCertifications();
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
	private Boolean convertStatus(String sts) {
		if(sts!=null) {
			if(sts.equals("")){
				return null;
			}
			else if(sts.equals("true")){
				return true;
			}
			else if(sts.equals("false")) {
				return false;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public void filter() {
		if(query!=null ) {
			System.out.println("Query for \""+query+"\"");
			if(!query.equals("")){
				filteredCertifications = certificationService.filter(query, user.getUser());
			}else {
				refreshCertifications();
			}
			certifications = filteredCertifications;
		}else {
			refreshCertifications();
		}
	}

	public void refreshCertifications() {
		backUpCertifications = certifications = certificationService.findByUser(user.getUser());
	}
	public void clear() {
		backUpCertifications = certifications = certificationService.findByUser(user.getUser());
		query=null;
	}
	
	private String getSuccess() {
		return "Success!";
	}
	
	private String getError() {
		return "Error!";
	}
	
	private void exceptionHandler(GeneralException exception) {
		if(exception!=null) {
			addMessage(new FacesMessage(getError(),exception.getMessage()));
		}
	}
}

