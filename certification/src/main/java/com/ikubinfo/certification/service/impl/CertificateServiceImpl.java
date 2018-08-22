package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.dao.CertificateDao;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.service.CertificateService;
@Service("certificateService")
public class CertificateServiceImpl implements CertificateService,Serializable{
	
	private static final long serialVersionUID = 599322609617517947L;
	
	@Autowired
	CertificateDao certificateDao;
	
	@Transactional
	@Override
	public boolean add(Certificate certificate) throws GeneralException {
		if(isValid(certificate)) {
			return certificateDao.add(certificate);
		}else {
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean remove(Certificate certificate) throws GeneralException {
		if(canBeDeleted(certificate.getId())) {
				return certificateDao.remove(certificate);
		}else {
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean removePermanently(Certificate certificate) throws GeneralException {
		if(canBeDeletedPermanently(certificate.getId())) {
			return certificateDao.removePermanently(certificate);
		}else {
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean update(Certificate certificate) throws GeneralException {
		return certificateDao.update(certificate);
	}
	
	@Transactional
	@Override
	public boolean restore(Certificate certificate) {
		return certificateDao.restore(certificate);
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
	public ArrayList<Certificate> getAllDisabled() {
		return certificateDao.getAllDisabled();
	}

	@Override
	public boolean isValid(Certificate certificate) throws GeneralException {
		return certificateDao.isValid(certificate);
	}

	@Override
	public boolean canBeDeleted(Integer certificateId) throws GeneralException {
		return certificateDao.canBeDeleted(certificateId);
	}

	@Override
	public boolean canBeDeletedPermanently(Integer certificateId) throws GeneralException {
		return certificateDao.canBeDeletedPermanently(certificateId);
	}

	@Override
	public int getTotalRows() {
		return certificateDao.getTotalRows();
	}

	@Override
	public int getTotalDeletedRows() {
		return certificateDao.getTotalDeletedRows();
	}

	
	
}
