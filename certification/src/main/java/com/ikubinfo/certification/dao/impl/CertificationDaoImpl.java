package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.dao.CertificationDao;
import com.ikubinfo.certification.exception.CertificationException;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;

@Repository(value="CertificationDao")
@Scope("singleton")
@Component
public class CertificationDaoImpl implements CertificationDao {
	
	private static Logger log = Logger.getLogger(CertificationDaoImpl.class);
	
	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public boolean add(EmployeeCertification certification) {
		try {
			entityManager.persist(certification);
			log.info("Certificate: "+certification.getCertificate().getTitle()+" assigned succesfully to employee :"+certification.getUser().getName()+" "+certification.getUser().getSurname());
			return true;
		} catch (Exception e) {
			log.warn("Error while assign certification to employee! "+certification.getUser().getName()+" "+certification.getUser().getSurname()+" .Error message: "+e.getMessage());
			return false;
		}
		
	}
	
	@Transactional
	@Override
	public boolean edit(EmployeeCertification certification) {
		try {
			entityManager.merge(certification);
			log.info("Certificate: "+certification.getCertificate().getTitle()+" updated succesfully for employee :"+certification.getUser().getName()+" "+certification.getUser().getSurname());
			return true;
		} catch (Exception e) {
			log.warn("Error while updating certification for employee! "+certification.getUser().getName()+" "+certification.getUser().getSurname()+" .Error message: "+e.getMessage());
			return false;
		}
		
	}

	@Transactional
	@Override
	public boolean remove(EmployeeCertification certification) {
		try {
			entityManager.
			 createQuery("UPDATE EmployeeCertification ec set ec.deleted=1 where ec.certificate.id=:certificate and  ec.user.id=:user")
			.setParameter("certificate",certification.getCertificate().getId())
			.setParameter("user", certification.getUser().getId())
			.executeUpdate();
			log.info("Certification: "+certification.getCertificate().getTitle()+" was removed succesfully for employee: "+certification.getUser().getName()+" "+certification.getUser().getSurname());
			return true;			
			
		} catch (Exception e) {
			log.warn("Error while removing certification for employee! "+certification.getUser().getName()+" "+certification.getUser().getSurname()+" .Error message: "+e.getMessage());
			return false;
			}
	}

	@Override
	public boolean isValid(EmployeeCertification certificationToBeValidated) throws CertificationException {
		try {
			EmployeeCertification certification;
			certification= (EmployeeCertification)entityManager
			.createQuery("Select certification From EmployeeCertification certification Where certification.user=:user And certification.certificate",EmployeeCertification.class)
			.setParameter("user", certificationToBeValidated.getUser())
			.setParameter("certificate", certificationToBeValidated.getCertificate())
			.getSingleResult();
			if(certification.isDeleted()) {
				log.warn("Certification has been previously assigned to employee but has been deleted!");
				throw new CertificationException(ErrorMessages.PREVIOUSLY_DELETED_CERTIFICATION.getMessage());
			}
			else {
				log.warn("Certification has already been assigned to employee!");
				throw new CertificationException(ErrorMessages.DUBLICATE_CERTIFICATION.getMessage());
			}
			
		}catch (NoResultException e) {
			log.info("Certification "+certificationToBeValidated.getCertificate().getTitle()+" is Valid for employee: "+certificationToBeValidated.getUser().getName()+" "+certificationToBeValidated.getUser().getSurname());
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> getAllActive(User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery("SELECT ec FROM EmployeeCertification ec WHERE ec.user.manager=:manager AND deleted=0")
					.setParameter("manager", manager)
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificatons cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@Override
	public EmployeeCertification find(User user, Certificate certificate) {
		try {
			EmployeeCertification employeeCertification = (EmployeeCertification) entityManager
			.createQuery("SELECT ec FROM EmployeeCertification ec WHERE ec.user=:user AND ec.certificate=:certificate")
			.setParameter("user", user)
			.setParameter("certificate", certificate)
			.getSingleResult();
															
			if(!employeeCertification.isDeleted()) {
				return employeeCertification;
			}
			else {
				log.warn("Certification : "+certificate.getTitle()+" for employee "+user.getUsername()+" is deleted!");
				return null;
			}
		} catch (Exception e) {
			log.warn("Certification cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> findByUser(User user) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery("SELECT ec FROM EmployeeCertification ec WHERE ec.user=:user AND deleted=0")
					.setParameter("user", user)
					.getResultList();
				
		} catch (Exception e) {
			log.info("Certificatons cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	
	
	
}
