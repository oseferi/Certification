package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;

import com.ikubinfo.certification.exception.CertificateExistsException;
import com.ikubinfo.certification.exception.DeletedCertificateException;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;

public interface CertificateDao {
	
	public boolean add(Certificate certificate);
	public boolean remove(Certificate certificate);
	public boolean update(Certificate certificate);
	
	public Certificate findById(int id);
	public ArrayList<Certificate> getAll();
	public ArrayList<Certificate> getAllActive();
	
	public boolean isValid(Certificate certificate) throws GeneralException ;
}
