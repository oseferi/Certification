package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.CertificationDao;
import com.ikubinfo.certification.exception.CertificationException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.CertificationService;

@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

	@Autowired
	CertificationDao certificationDao;

	@Override
	public boolean add(EmployeeCertification certification) throws CertificationException {
		if(isValid(certification)) {
			return certificationDao.add(certification);
		}else {
			return false;
		}
	}

	@Override
	public boolean edit(EmployeeCertification certification) throws CertificationException {
			return certificationDao.edit(certification);
	}

	@Override
	public boolean remove(EmployeeCertification certification) {
		return certificationDao.remove(certification);
	}

	@Override
	public boolean isValid(EmployeeCertification certification) throws CertificationException {
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
	
	
	
}
