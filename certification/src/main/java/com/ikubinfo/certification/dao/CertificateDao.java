package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.User;

public interface CertificateDao {
	
	public boolean add(Certificate certificate);
	public boolean remove(Certificate certificate);
	public boolean removePermanently(Certificate certificate);
	public boolean update(Certificate certificate);
	public boolean restore(Certificate certificate);
	
	
	public Certificate findById(int id);
	public ArrayList<Certificate> getAll();
	public ArrayList<Certificate> getAllActive();
	public ArrayList<Certificate> getAllDisabled();
	
	public boolean isValid(Certificate certificate) throws GeneralException ;
	public boolean canBeDeleted(Integer certificateId) throws GeneralException;
	public boolean canBeDeletedPermanently(Integer certificateId) throws GeneralException;
	public int getTotalRows() ;
	public int getTotalDeletedRows();
}
