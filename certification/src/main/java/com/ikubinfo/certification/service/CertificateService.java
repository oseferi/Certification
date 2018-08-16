package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;

public interface CertificateService {
	public boolean add(Certificate certificate) throws GeneralException;
	public boolean remove(Certificate certificate) throws GeneralException;
	boolean removePermanently(Certificate certificate)throws GeneralException;
	public boolean update(Certificate certificate) throws GeneralException;
	public boolean restore(Certificate certificate);
	
	public Certificate findById(int id);
	public ArrayList<Certificate> getAll();
	public ArrayList<Certificate> getAllActive();
	public ArrayList<Certificate> getAllDisabled();
	
	public boolean isValid(Certificate certificate) throws GeneralException ;
	boolean canBeDeleted(Integer certificateId) throws GeneralException;
	boolean canBeDeletedPermanently(Integer certificateId) throws GeneralException;
	
	public int getTotalRows() ;
	public int getTotalDeletedRows();

}
