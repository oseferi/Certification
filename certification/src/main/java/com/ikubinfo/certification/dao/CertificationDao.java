package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;

public interface CertificationDao {

	public boolean add(EmployeeCertification certification);
	public boolean edit(EmployeeCertification certification);
	public boolean remove(EmployeeCertification certification);
	public boolean removePermanently(EmployeeCertification certification);
	public boolean restore(EmployeeCertification certification);
	
	public boolean isValid(EmployeeCertification certification) throws GeneralException;
	public EmployeeCertification find(int id);
	public ArrayList<EmployeeCertification> findByUser(User user);
	public ArrayList<EmployeeCertification> getAllActive(User manager);
	public ArrayList<EmployeeCertification> getAllDisabled(User manager);	
	
	
	public ArrayList<EmployeeCertification> filter(String searchQuery, User user);
	public ArrayList<EmployeeCertification> filter(String certificate, String status, int managerId);
	public ArrayList<EmployeeCertification> filter(int userId, String certificate, String status, int managerId);
	public ArrayList<EmployeeCertification> filter(int userId, String certificate, String status);

	
	public ArrayList<EmployeeCertification> filterByTitle(String title,User manager);
	public ArrayList<EmployeeCertification> filterByEmployee(int employeeId, User manager);
	public ArrayList<EmployeeCertification> filterByTitleAndEmployee(String title,int employeeId, User manager);
	
	public int getTotalRows(int managerId) ;
	public int getTotalDeletedRows(int managerId);
	
}
