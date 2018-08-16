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
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificateService;
import com.ikubinfo.certification.service.CertificationService;
import com.ikubinfo.certification.service.TechnologyService;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;
import com.sun.faces.facelets.tag.jstl.core.IfHandler;

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
	
	private EmployeeCertification certification,newCertification;
	private Certificate certificate;
	private int certificateId, userId, technologyId;
	private ArrayList<EmployeeCertification> certifications,certifications2;
	private ArrayList<EmployeeCertification> filteredCertifications;
	private ArrayList<Certificate> certificates;
	private ArrayList<Technology> technologies;
	private ArrayList<User> employees;
	private String query;
	private String status;
	private ArrayList<Boolean> statuses;
	private Integer id;
	
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

	public EmployeeCertification getNewCertification() {
		return newCertification;
	}

	public void setNewCertification(EmployeeCertification newCertification) {
		this.newCertification = newCertification;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public int getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(int certificateId) {
		this.certificateId = certificateId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTechnologyId() {
		return technologyId;
	}

	public void setTechnologyId(int technologyId) {
		this.technologyId = technologyId;
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Boolean> getStatuses() {
		return statuses;
	}

	public void setStatuses(ArrayList<Boolean> statuses) {
		this.statuses = statuses;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@PostConstruct
	public void init() {
		certification = new EmployeeCertification();
		certificate = new Certificate();
		newCertification = new EmployeeCertification();
		refreshCertifications();
		refreshCertificates();
		refreshTechnologies();
		refreshEmployees();
		statuses = new ArrayList<Boolean>();
		setStatusesList();
		try {
			if(id!=null) {
				selectCertification(id);
				statuses = new ArrayList<Boolean>();
				setStatusesList();
			}
		}
		catch(NullPointerException x) {
			log.warn("An error occured.There will be nothing displayed in the front-end");
			certification= null;
		}
		
		
	}
	
	@SuppressWarnings("finally")
	public String addCertificate() {
		try {
			certificateService.add(certificate);
			certificate=new Certificate();
			refreshCertificates();
			addMessage(new FacesMessage(getSuccess(),MessageUtility.getMessage("CERTIFICATE_ADDED")));
		} catch (GeneralException ge) {
			exceptionHandler(ge);
		}
		finally {
			return "";
		}
	}
	
	public String updateCertification() {
		try {
			certificationService.edit(certification);
			setStatusesList();
			addMessage( new FacesMessage(getSuccess(),
						SuccessMessages.CERTIFICATION_UPDATED.getMessage()) );
			
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
		return "";
	
}
	
	public String removeCertificate(int id) {
		try {
			if(certificateService.remove(certificateService.findById(id))) {
				addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATE_DELETED")));
				refreshCertificates();
			}else {
				addMessage( new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATE_DELETE_FAIL")) );
			}
		} catch (GeneralException e) {
			exceptionHandler(e);
		}
		return null;
	}
	
	public String assignCertification() {
		log.info("assign Certification executed!");
		try {
			newCertification.setUser(userService.findById(userId));
			newCertification.setCertificate(certificateService.findById(certificateId));
			certificationService.add(newCertification);
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATION_ASSIGNED")) );
			newCertification = new EmployeeCertification();
			refreshCertifications();
			userId = 0;
			certificateId = 0;
		}catch(GeneralException e) {
			exceptionHandler(e);
		}
			return null;
	}
	
	public String removeCertification(int id) {
		if(certificationService.remove(certificationService.find(id))) {
			addMessage(new FacesMessage(getSuccess(), MessageUtility.getMessage("CERTIFICATION_UNASSIGNED")));
			refreshCertifications();
		}else {
			log.info("Certificate could not be unassigned!");
			addMessage(new FacesMessage(getError(), MessageUtility.getMessage("CERTIFICATION_UNASSIGN_FAIL")) );
		}
		return null;
		
	}
	public void refreshCertifications() {
		certifications = certificationService.getAllActive(user.getUser());
		certifications2 = certifications;
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
		return "edit-certification?faces-redirect=true&id="+certification.getId();
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
	
	public String filter() {
		System.out.println("Key"+query);
		if(userId>0 || (query!=null && !(query.equals(""))) || (status!=null &&!status.equals("-"))) {
			if(query==null || query.equals("")) {
				filterByEmployee();
				if(status!=null && !status.equals("-")) {
					filterByStatus();
				}
			}else if (userId<=0) {
				filterByDescription();
				if(status!=null && !status.equals("-")) {
					filterByStatus();
				}
			}else if (userId>0 && query!=null) {
				filterByQueryAndUser();
				if(status!=null && !status.equals("-")) {
					filterByStatus();
				}
			}else if(status!=null && !status.equals("-")) {
				filterByStatus();
			}
		}else{
			refreshCertifications();
		}
		return null;
	}
	
	private void filterByDescription() {
		
		certifications = certifications2 = certificationService.filterByTitle(query, user.getUser());
	}
	
	private void filterByEmployee() {
		certifications = certifications2 = certificationService.filterByEmployee(userId, user.getUser());
	}
	
	private void filterByQueryAndUser() {
		certifications = certifications2 = certificationService.filterByTitleAndEmployee(query, userId, user.getUser());
	}
	public String selectCertification(int id) {
		try{
			certification = certificationService.find(id);
		}catch (NullPointerException e) {
			certification = null;
		}
		return "";
	}
	
	
	public void filterByStatus() {
		filteredCertifications = new ArrayList<EmployeeCertification>();
		for(EmployeeCertification ec : certifications2) {
			if( status.equals("Progress") && ec.getStatus()==null) {
				filteredCertifications.add(ec);
			}else if(status.equals("Failed") && ec.getStatus()!=null && !ec.getStatus()) {
				filteredCertifications.add(ec);
			}else if(status.equals("Passed") && ec.getStatus()!=null && ec.getStatus()) {
				filteredCertifications.add(ec);
			}
		}
		certifications = filteredCertifications;
		
	}
	
	public void clearFilter() {
		userId=0;
		status=null;
		refreshCertifications();
	}
	public void clearSearch() {
		query=null;
		filter();
	}
	
	public void setStatusesList() {
		if(certification.getStatus()==null) {
			statuses.add(null);statuses.add(true);statuses.add(false);
		}else if(certification.getStatus()) {
			statuses.add(true);statuses.add(false);statuses.add(null);
		}else if(!certification.getStatus()) {
			statuses.add(false);statuses.add(true);statuses.add(null);
		}
	}
}

