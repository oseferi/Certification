package com.ikubinfo.certification.dao.impl;


import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.ikubinfo.certification.dao.CertificateDao;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.utility.MessageUtility;

@Repository(value="CertificateDao")
@Scope("singleton")
@Component
public class CertificateDaoImpl implements CertificateDao{
	private static Logger log = Logger.getLogger(CertificateDaoImpl.class);
	
	@PersistenceContext
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
	public boolean removePermanently(Certificate certificate) {
		try {
			Certificate certificateToBeDeleted = findById(certificate.getId());
			entityManager.remove(certificateToBeDeleted);
			log.info("Certificate : "+certificate.getTitle()+" was removed permanently!");
			return true;
		}catch (Exception e) {
			log.info("Certificate : "+certificate.getTitle()+" failed to be removed permanently! Error message :"+e.getMessage());
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
			return false;
		}
	}
	
	
	@Transactional
	@Override
	public boolean restore(Certificate certificate) {
		try {
			entityManager.
			 createQuery("update Certificate set deleted=0 where id=:id")
			.setParameter("id",certificate.getId())
			.executeUpdate();
			log.info("Certificate: "+certificate.getTitle()+" was removed restored!");
			return true;			
			
		} catch (Exception e) {
			log.info("Certificate : "+certificate.getTitle()+" failed to be restored! Error message :"+e.getMessage());
			return false;		
			}
	}

	@Override
	public Certificate findById(int id) {
		try {
			Certificate certificate =  entityManager.find(Certificate.class, id);
			//if(!certificate.isDeleted()) {
				return certificate;
			//}
			//else {
			//	log.warn("Certificate: "+certificate.getTitle()+" is deleted!");
			//	return null;
			//}
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
					.createQuery("Select certificate from Certificate certificate Where certificate.deleted=1")
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
					.createQuery("Select certificate from Certificate certificate Where certificate.deleted=0")
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificates cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Certificate> getAllDisabled() {
		try {
			return (ArrayList<Certificate>) entityManager
					.createQuery("Select certificate from Certificate certificate Where certificate.deleted=1")
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificates cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@Override
	public boolean isValid(Certificate certificateToBeValidated) throws GeneralException {
		try {
			Certificate certificate;
			certificate= (Certificate)entityManager
			.createQuery("Select certificate From Certificate certificate Where certificate.title=:title",Certificate.class)
			.setParameter("title", certificateToBeValidated.getTitle())
			.getSingleResult();
			if(certificate.isDeleted()) {
				log.warn(MessageUtility.getMessage("CERTIFICATE_PREVIOUSLY_DELETED"));
				throw new GeneralException(MessageUtility.getMessage("CERTIFICATE_PREVIOUSLY_DELETED"));
			}
			else {
				log.warn(MessageUtility.getMessage("CERTIFICATE_DUPLICATE"));
				throw new GeneralException(MessageUtility.getMessage("CERTIFICATE_DUPLICATE"));
			}
			
		}catch (NoResultException e) {
			log.info("Certificate "+certificateToBeValidated.getTitle()+" is Valid");
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canBeDeleted(Integer certificateId) throws GeneralException {
		try{
			ArrayList<EmployeeCertification> results = (ArrayList<EmployeeCertification>) entityManager
											.createQuery("Select ec from EmployeeCertification ec Where ec.certificate.id=:id and ec.deleted=0")
											.setParameter("id", certificateId)
											.getResultList();
				if(results.isEmpty()) {
					return true;
				}else {
					log.info(MessageUtility.getMessage("CERTIFICATE_FORBID_DELETE"));
					throw new GeneralException(MessageUtility.getMessage("CERTIFICATE_FORBID_DELETE"));	
				}
			}catch(NoResultException e) {
				return true;
			}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean canBeDeletedPermanently(Integer certificateId) throws GeneralException {
		try{
			ArrayList<EmployeeCertification> results = (ArrayList<EmployeeCertification>) entityManager
											.createQuery("Select ec from EmployeeCertification ec Where ec.certificate.id=:id")
											.setParameter("id", certificateId)
											.getResultList();
				if(results.isEmpty()) {
					return true;
				}else {
					log.info(MessageUtility.getMessage("CERTIFICATE_FORBID_DELETE"));
					throw new GeneralException(MessageUtility.getMessage("CERTIFICATE_FORBID_DELETE"));	
				}
			}catch(NoResultException e) {
				return true;
			}
		
	}

	@Override
	public int getTotalRows() {
		return ((Number)entityManager.createQuery("select count(c) from Certificate c")
                .getSingleResult()).intValue();
	}

	@Override
	public int getTotalDeletedRows() {
		return ((Number)entityManager.createQuery("select count(c) from Certificate c where c.deleted=1")
                .getSingleResult()).intValue();
	}
	
	

}
