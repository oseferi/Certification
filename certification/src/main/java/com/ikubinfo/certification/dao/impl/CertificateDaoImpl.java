package com.ikubinfo.certification.dao.impl;


import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ikubinfo.certification.dao.CertificateDao;
import com.ikubinfo.certification.exception.CertificateExistsException;
import com.ikubinfo.certification.exception.DeletedCertificateException;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.User;

@Repository("CertificateDao")
@Scope("singleton")
@Component
public class CertificateDaoImpl implements CertificateDao{
	private static Logger log = Logger.getLogger(CertificateDaoImpl.class);
	
	@Autowired
	EntityManager entityManager;
	
	@Transactional
	@Override
	public boolean add(Certificate certificate) {
		try {
			entityManager.persist(certificate);
			log.info("Certificate: "+certificate.getTitle()+" was added succesfully! ");
			return true;
		}
		catch (Exception e) {
			log.error("Failed to add certificate! Error Message :"+e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean remove(Certificate certificate) {
		try {
			entityManager.
			 createQuery("update Certificate set deleted=1 where id=:id")
			.setParameter("id",certificate.getId())
			.executeUpdate();
			log.info("Certificate: "+certificate.getTitle()+" was removed succesfully!");
			return true;			
			
		} catch (Exception e) {
			log.info("Certificate : "+certificate.getTitle()+" failed to be removed! Error message :"+e.getMessage());
			return false;		
			}
	}

	@Transactional
	@Override
	public boolean update(Certificate certificate) {
		try {
			entityManager.merge(certificate);
			log.info("Certificate: "+certificate.getTitle()+ " was updated succesfully!");
			return true;
		}
		catch(Exception e){
			log.error("Certificate: "+certificate.getTitle()+" failed to be updated! Error message :"+e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}

	@Override
	public Certificate findById(int id) {
		try {
			Certificate certificate =  entityManager.find(Certificate.class, id);
			if(!certificate.isDeleted()) {
				return certificate;
			}
			else {
				log.warn("Certificate: "+certificate.getTitle()+" is deleted!");
				return null;
			}
		} catch (Exception e) {
			log.warn("Certificate cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Certificate> getAll() {
		try {
			return (ArrayList<Certificate>) entityManager
					.createQuery("Select certificate from Certificate certificate")
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificates cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Certificate> getAllActive() { 
		try {
			return (ArrayList<Certificate>) entityManager
					.createQuery("Select user from User user Where deleted=0")
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificates cannot be found! Error message :"+e.getMessage());
			//System.out.println("Users cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@Override
	public boolean isValid(Certificate certificateToBeValidated) throws CertificateExistsException, DeletedCertificateException {
		try {
			Certificate certificate;
			certificate= (Certificate)entityManager
			.createQuery("Select certificate From Certificate certificate Where certificate.title=:title",Certificate.class)
			.setParameter("title", certificateToBeValidated.getTitle())
			.getSingleResult();
			if(certificate.isDeleted()) {
				log.warn("Certificate has been previously deleted!");
				throw new DeletedCertificateException();
			}
			else {
				log.warn("Certificate already exists!");
				throw new CertificateExistsException();
			}
			
		}catch (NoResultException e) {
			log.info("Certificate "+certificateToBeValidated.getTitle()+" is Valid");
			//System.out.println("Username "+userToBeValidated.getUsername()+" is Valid");
			return true;
		}

	}
	
	
	
}
