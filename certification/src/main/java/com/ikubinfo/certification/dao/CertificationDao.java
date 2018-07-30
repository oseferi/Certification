package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.CertificationException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;

public interface CertificationDao {

	public boolean add(EmployeeCertification certification);
	public boolean edit(EmployeeCertification certification);
	public boolean remove(EmployeeCertification certification);
	public boolean isValid(EmployeeCertification certification) throws CertificationException;
	public EmployeeCertification find(User user,Certificate certificate);
	public ArrayList<EmployeeCertification> findByUser(User user);
	public ArrayList<EmployeeCertification> getAllActive(User manager);
}
