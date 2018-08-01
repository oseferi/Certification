package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.CertificateDao;
import com.ikubinfo.certification.exception.CertificateExistsException;
import com.ikubinfo.certification.exception.DeletedCertificateException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.service.CertificateService;
@Service("certificateService")
public class CertificateServiceImpl implements CertificateService,Serializable{
	

	private static final long serialVersionUID = 599322609617517947L;

	private static Logger log = Logger.getLogger(CertificateServiceImpl.class);
	
	@Autowired
	CertificateDao certificateDao;
	
	@Override
	public boolean add(Certificate certificate) throws CertificateExistsException, DeletedCertificateException {
		if(isValid(certificate)) {
			return certificateDao.add(certificate);
		}else {
			return false;
		}
	}

	@Override
	public boolean remove(Certificate certificate) {
		return certificateDao.remove(certificate);
	}

	@Override
	public boolean update(Certificate certificate) throws CertificateExistsException, DeletedCertificateException {
		if(isValid(certificate)) {
			return certificateDao.update(certificate);
		}else {
			return false;	
		}
	}

	@Override
	public Certificate findById(int id) {
		return certificateDao.findById(id);
	}

	@Override
	public ArrayList<Certificate> getAll() {
		return certificateDao.getAll();
	}

	@Override
	public ArrayList<Certificate> getAllActive() {
		return certificateDao.getAllActive();
	}

	@Override
	public boolean isValid(Certificate certificate) throws CertificateExistsException, DeletedCertificateException {
		return certificateDao.isValid(certificate);
	}

	
}
