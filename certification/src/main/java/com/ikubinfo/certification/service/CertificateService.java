package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.CertificateExistsException;
import com.ikubinfo.certification.exception.DeletedCertificateException;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;

public interface CertificateService {
	public boolean add(Certificate certificate) throws GeneralException;
	public boolean remove(Certificate certificate);
	public boolean update(Certificate certificate) throws GeneralException;
	
	public Certificate findById(int id);
	public ArrayList<Certificate> getAll();
	public ArrayList<Certificate> getAllActive();
	
	public boolean isValid(Certificate certificate) throws GeneralException ;
}
