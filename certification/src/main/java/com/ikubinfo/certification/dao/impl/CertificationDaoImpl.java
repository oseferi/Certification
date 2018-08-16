package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.dao.CertificationDao;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.utility.MessageUtility;

@Repository(value = "CertificationDao")
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
			log.info("Certificate: " + certification.getCertificate().getTitle() + " assigned succesfully to employee :"
					+ certification.getUser().getName() + " " + certification.getUser().getSurname());
			return true;
		} catch (NoResultException e) {
			log.warn("Error while assign certification to employee! " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname() + " .Error message: " + e.getMessage());
			return false;
		}

	}

	@Transactional
	@Override
	public boolean edit(EmployeeCertification certification) {
		try {
			entityManager.merge(certification);
			log.info("Certificate: " + certification.getCertificate().getTitle() + " updated succesfully for employee :"
					+ certification.getUser().getName() + " " + certification.getUser().getSurname());
			return true;
		} catch (NoResultException e) {
			log.warn("Error while updating certification for employee! " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname() + " .Error message: " + e.getMessage());
			return false;
		}

	}

	@Transactional
	@Override
	public boolean remove(EmployeeCertification certification) {
		try {
			entityManager.createQuery("UPDATE EmployeeCertification ec set ec.deleted=1 where ec.id=:id")
					.setParameter("id", certification.getId())
					.executeUpdate();
			log.info("Certification: " + certification.getCertificate().getTitle()
					+ " was removed succesfully for employee: " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname());
			return true;

		} catch (NoResultException e) {
			log.warn("Error while removing certification for employee! " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname() + " .Error message: " + e.getMessage());
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean restore(EmployeeCertification certification) {
		try {
			entityManager.createQuery("UPDATE EmployeeCertification ec set ec.deleted=0 where ec.id=:id")
					.setParameter("id", certification.getCertificate().getId())
					.executeUpdate();
			
			log.info("Certification: " + certification.getCertificate().getTitle()
					+ " was restored succesfully for employee: " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname());
			
			return true;

		} catch (NoResultException e) {
			log.warn("Error while restoring certification for employee! " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname() + " .Error message: " + e.getMessage());
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean removePermanently(EmployeeCertification certification) {
		try {
			EmployeeCertification certificationToBeDeleted = find(certification.getId());
			entityManager.remove(certificationToBeDeleted);
			log.info("Certification: " + certification.getCertificate().getTitle()
					+ " was removed succesfully for employee: " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname());
			return true;
		} catch (NoResultException e) {
			log.warn("Error while removing certification for employee! " + certification.getUser().getName() + " "
					+ certification.getUser().getSurname() + " .Error message: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean isValid(EmployeeCertification certificationToBeValidated) throws GeneralException {
		try {
			EmployeeCertification certification;
			certification = (EmployeeCertification) entityManager.createQuery(
					"Select ec From EmployeeCertification ec Where ec.user=:user And ec.certificate=:certificate",
					EmployeeCertification.class).setParameter("user", certificationToBeValidated.getUser())
					.setParameter("certificate", certificationToBeValidated.getCertificate()).getSingleResult();
			if (certification.isDeleted()) {
				// log.warn(ErrorMessages.CERTIFICATION_PREVIOUSLY_DELETED.getMessage());
				// throw new
				// GeneralException(ErrorMessages.CERTIFICATION_PREVIOUSLY_DELETED.getMessage());
				return true;
			} else {
				log.warn(MessageUtility.getMessage("CERTIFICATION_DUPLICATE"));
				throw new GeneralException(MessageUtility.getMessage("CERTIFICATION_DUPLICATE"));
			}

		} catch (NoResultException e) {
			log.info("Certification " + certificationToBeValidated.getCertificate().getTitle()
					+ " is Valid for employee: " + certificationToBeValidated.getUser().getName() + " "
					+ certificationToBeValidated.getUser().getSurname());
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> getAllActive(User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery(
							"SELECT ec FROM EmployeeCertification ec WHERE ec.user.manager=:manager AND ec.deleted=0")
					.setParameter("manager", manager).getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> getAllDisabled(User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery(
							"SELECT ec FROM EmployeeCertification ec WHERE ec.user.manager=:manager AND ec.deleted=1")
					.setParameter("manager", manager).getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public EmployeeCertification find(int id) {
		try {
			return  (EmployeeCertification) entityManager
					.createQuery("SELECT ec FROM EmployeeCertification ec WHERE ec.id=:id").setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("Certification cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> findByUser(User user) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery("SELECT ec FROM EmployeeCertification ec WHERE ec.user=:user AND deleted=0")
					.setParameter("user", user).getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> filter(String searchQuery, User user) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager.createQuery(
					"SELECT ec FROM EmployeeCertification ec WHERE  ec.user=:user AND deleted=0 AND (ec.certificate.title LIKE :query OR ec.certificate.description LIKE :query)")
					.setParameter("user", user).setParameter("query", "%" + searchQuery + "%").getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> filterByTitle(String title, User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager.createQuery(
					"SELECT ec FROM EmployeeCertification ec WHERE  ec.user.manager=:manager AND deleted=0 AND (ec.certificate.title LIKE :title OR ec.certificate.description LIKE :title)")
					.setParameter("manager", manager).setParameter("title", "%" + title + "%").getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> filterByEmployee(int employeeId, User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager.createQuery(
					"SELECT ec FROM EmployeeCertification ec WHERE  ec.user.manager=:manager AND deleted=0 AND ec.user.id = :employeeId")
					.setParameter("manager", manager).setParameter("employeeId", employeeId).getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<EmployeeCertification> filterByTitleAndEmployee(String title, int employeeId, User manager) {
		try {
			return (ArrayList<EmployeeCertification>) entityManager
					.createQuery("SELECT ec " + "FROM EmployeeCertification ec " + "WHERE  ec.user.manager=:manager "
							+ "AND deleted=0 " + "AND ec.user.id = :employeeId "
							+ "AND (ec.certificate.title LIKE :title OR ec.certificate.description LIKE :title)")
					.setParameter("manager", manager).setParameter("employeeId", employeeId)
					.setParameter("title", "%" + title + "%").getResultList();

		} catch (NoResultException e) {
			log.info("Certificatons cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public int getTotalRows(int managerId) {
		return ((Number) entityManager.createQuery("select count(ec) from EmployeeCertification ec where ec.user.manager.id=:managerId")
				.setParameter("managerId", managerId)
				.getSingleResult())
				.intValue();
	}

	@Override
	public int getTotalDeletedRows(int managerId) {
		return ((Number) entityManager.createQuery("select count(ec) from EmployeeCertification ec where ec.user.manager.id=:managerId and ec.deleted=1")
				.setParameter("managerId", managerId)
				.getSingleResult())
				.intValue();
	}

}
