package com.ikubinfo.certification.bean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.SuccessMessages;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.Status;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.StatusService;
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
	
	@ManagedProperty(value="#{statusService}")
	StatusService statusService;
	
	private ArrayList<EmployeeCertification> certifications,backUpCertifications,filteredCertifications;
	private String status;
	private ArrayList<Status> statuses;
	private String query;
	private int statusId;
	
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
	
	public StatusService getStatusService() {
		return statusService;
	}

	public void setStatusService(StatusService statusService) {
		this.statusService = statusService;
	}

	public ArrayList<Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(ArrayList<Status> statuses) {
		this.statuses = statuses;
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public void editRow(RowEditEvent event) {
		EmployeeCertification certification1 = (EmployeeCertification)event.getObject();
		certification1.setStatus(statusService.findById(statusId));
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
	public void clearFilter() {
		status="";
		refreshCertifications();
	}
	
	public void filter() {
		if(validQuery() || validStatus()) {
			System.out.println("Query for \""+query+"\"");
			certifications = certificationService.filter(user.getUser().getId(),query, status);
		}else {
			refreshCertifications();
		}
	}

	public void refreshCertifications() {
		backUpCertifications = certifications = certificationService.findByUser(user.getUser());
	}
	public void clear() {
		backUpCertifications = certifications = certificationService.findByUser(user.getUser());
		query="";
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
	
	private boolean validQuery() {
		return query!=null && !query.equals("");
	}
	private boolean validStatus() {
		return status!=null &&!status.equals("-");
	}
}

