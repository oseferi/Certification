package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.dao.CertificationDao;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificationService;

@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

	@Autowired
	CertificationDao certificationDao;

	@Transactional
	@Override
	public boolean add(EmployeeCertification certification) throws GeneralException {
		if(isValid(certification)) {
			return certificationDao.add(certification);
		}else {
			return false;
		}
	}

	@Transactional
	@Override
	public boolean edit(EmployeeCertification certification) throws GeneralException {
			return certificationDao.edit(certification);
	}

	@Transactional
	@Override
	public boolean removePermanently(EmployeeCertification certification) {
		return certificationDao.removePermanently(certification);
	}

	@Transactional
	@Override
	public boolean restore(EmployeeCertification certification) {
		return certificationDao.restore(certification);
	}

	@Transactional
	@Override
	public boolean remove(EmployeeCertification certification) {
		return certificationDao.remove(certification);
	}

	@Override
	public boolean isValid(EmployeeCertification certification) throws GeneralException {
		return certificationDao.isValid(certification);
	}

	@Override
	public EmployeeCertification find(int id) {
		return certificationDao.find(id);
	}

	@Override
	public ArrayList<EmployeeCertification> findByUser(User user) {
		return certificationDao.findByUser(user);
	}

	@Override
	public ArrayList<EmployeeCertification> getAllActive(User manager) {
		return certificationDao.getAllActive(manager);
	}
	

	@Override
	public ArrayList<EmployeeCertification> getAllDisabled(User manager) {
		return certificationDao.getAllDisabled(manager);
	}

	@Override
	public ArrayList<EmployeeCertification> filter(String searchQuery, User user) {
		return certificationDao.filter(searchQuery, user);
	}

	@Override
	public ArrayList<EmployeeCertification> filterByTitle(String title, User manager) {
		return certificationDao.filterByTitle(title, manager) ;
	}

	@Override
	public ArrayList<EmployeeCertification> filterByEmployee(int employeeId, User manager) {
		return certificationDao.filterByEmployee(employeeId, manager) ;

	}

	@Override
	public ArrayList<EmployeeCertification> filterByTitleAndEmployee(String title, int employeeId, User manager) {
		return certificationDao.filterByTitleAndEmployee(title, employeeId, manager);
	}
	
	@Override
	public ArrayList<EmployeeCertification> filter(String certificate, String status, int managerId) {
		return certificationDao.filter(certificate, status, managerId);
	}

	@Override
	public ArrayList<EmployeeCertification> filter(int userId, String certificate, String status, int managerId) {
			return certificationDao.filter(userId, certificate, status, managerId);
	}
	
	@Override
	public ArrayList<EmployeeCertification> filter(int userId, String certificate, String status) {
		return certificationDao.filter(userId, certificate, status);
	}

	@Override
	public int getTotalRows(int managerId) {
		return certificationDao.getTotalRows(managerId);
	}

	@Override
	public int getTotalDeletedRows(int managerId) {
		return certificationDao.getTotalDeletedRows(managerId);
	}
	
	
	
}
