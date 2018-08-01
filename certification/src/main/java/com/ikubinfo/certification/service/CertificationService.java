package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.CertificationException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;

public interface CertificationService {
	public boolean add(EmployeeCertification certification) throws CertificationException;
	public boolean edit(EmployeeCertification certification) throws CertificationException;
	public boolean remove(EmployeeCertification certification);
	public boolean isValid(EmployeeCertification certification) throws CertificationException;
	public EmployeeCertification find(int id);
	public ArrayList<EmployeeCertification> findByUser(User user);
	public ArrayList<EmployeeCertification> getAllActive(User manager);
	
}
