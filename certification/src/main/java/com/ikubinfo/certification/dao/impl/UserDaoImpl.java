package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jboss.logging.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.bean.ManagerCertificationBean;
import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.EmployeeCertification;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.utility.MessageUtility;

@Repository(value = "UserDao")
@Scope("singleton")
@Component
public class UserDaoImpl implements UserDao {
	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public boolean add(User user) {
		try {
			entityManager.persist(user);
			log.info("User: " + user.getUsername() + " was added succesfully!");
			return true;
		} catch (Exception e) {
			log.error("Failed to add user! Error Message :" + e.getMessage());
			return false;
		}
	}

	@Transactional
	@Override
	public boolean remove(User user) {
		try {
			entityManager.createQuery("update User set deleted=1 where id=:id").setParameter("id", user.getId())
					.executeUpdate();
			log.info("User: " + user.getUsername() + " was removed succesfully!");
			return true;

		} catch (Exception e) {
			log.info("User: " + user.getUsername() + " failed to be removed! Error message :" + e.getMessage());
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean removePermanently(User user) {
		try {
			User userToBeDeleted = findById(user.getId());
			entityManager.remove(userToBeDeleted);
			log.info("User: " + user.getUsername() + " was removed succesfully!");
			return true;
		} catch (Exception e) {
			log.info("User: " + user.getUsername() + " failed to be removed! Error message :" + e.getMessage());
			return false;
		}
	}
	
	public boolean removeMultiplePermanently(ArrayList<User>employees) {
		EntityTransaction etx= null;
		try {
			etx= entityManager.getTransaction();
			etx.begin();
			for (User user : employees) {
				removePermanently(user);
			}
		}catch (Exception e) {
			if (etx != null && etx.isActive())
                etx.rollback();
            e.printStackTrace();
         }
		finally {
		entityManager.close();	
		}
		return true;
	}

	@Transactional
	@Override
	public boolean update(User user) {
		try {
			entityManager.merge(user);
			log.info("User: " + user.getUsername() + " was updated succesfully!");
			return true;
		} catch (Exception e) {
			log.error("User: " + user.getUsername() + " failed to be updated! Error message :" + e.getMessage());
			// e.printStackTrace();
			return false;
		}
	}

	@Transactional
	@Override
	public boolean restore(User user) {
		try {
			entityManager.createQuery("update User set deleted=0 where id=:id").setParameter("id", user.getId())
					.executeUpdate();
			log.info("User: " + user.getUsername() + " was removed succesfully!");
			return true;

		} catch (Exception e) {
			log.info("User: " + user.getUsername() + " failed to be removed! Error message :" + e.getMessage());
			return false;
		}
	}

	@Override
	public User findById(int id) {
		try {
			User user = entityManager.find(User.class, id);
			//if (!user.isDeleted()) {
				return user;
			//} else {
			//	log.warn("User: " + user.getUsername() + " is deleted!");
			//	return null;
			//}
		} catch (NoResultException e) {
			log.warn("User cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<User> getAllActive(int id) {
		try {
			return (ArrayList<User>) entityManager
					.createQuery("Select user from User user Where user.manager=:manager And user.deleted=0")
					.setParameter("manager", findById(id)).getResultList();

		} catch (NoResultException e) {
			log.info("Users cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<User> getAllDisabled(int id) {
		try {
			return (ArrayList<User>) entityManager
					.createQuery("Select user from User user Where user.manager=:manager And user.deleted=1")
					.setParameter("manager", findById(id)).getResultList();

		} catch (NoResultException e) {
			log.info("Users cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public User exists(String username, String password) {
		try {
			User user;
			user = (User) entityManager
					.createQuery("Select user From User user Where user.username=:username And user.deleted=0",
							User.class)
					.setParameter("username", username).getSingleResult();

			// Compare passwords in encrypted forms
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			if (passwordEncryptor.checkPassword(password, user.getPassword())) {
				log.info("User Found!");
				return user;
			} else {
				log.warn("Wrong credentials!");
				return null;
			}
		} catch (NoResultException e) {
			log.warn("User cannot be found!(Maybe wrong credentials) Error message :" + e.getMessage());
			return null;

		}
	}

	@Override
	public boolean isValidUsername(User userToBeValidated) throws GeneralException {
		try {
			User user;
			user = (User) entityManager
					.createQuery("Select user From User user Where user.username=:username", User.class)
					.setParameter("username", userToBeValidated.getUsername()).getSingleResult();
			if (user.isDeleted()) {
				log.warn("Username belongs to previously deleted User!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_PREVIOUSLY_DELETED"));
			} else {
				log.warn("Username belongs to active User: " + user.getName() + " " + user.getSurname() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_DUPLICATE_USERNAME"));
			}

		} catch (NoResultException e) {
			log.info("Username " + userToBeValidated.getUsername() + " is Valid");
			return true;
		}
	}

	@Override
	public boolean isValidSsn(User userToBeValidated) throws GeneralException {
		try {
			User user = (User) entityManager.createQuery("Select user From User user Where user.ssn=:ssn ", User.class)
					.setParameter("ssn", userToBeValidated.getSsn()).getSingleResult();
			if (user.isDeleted()) {
				log.warn("SSN belongs to previously deleted User: " + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_PREVIOUSLY_DELETED"));
			} else {
				log.warn("SSN belongs to active User: " + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_DUPLICATE_SSN"));
			}

		} catch (NoResultException e) {
			log.warn("SSN " + userToBeValidated.getSsn() + " is Valid");
			return true;
		}
	}

	@Override
	public boolean isValidFullName(User userToBeValidated) throws GeneralException {

		try {
			User user = (User) entityManager
					.createQuery("Select user From User user Where user.name=:name And user.surname=:surname",
							User.class)
					.setParameter("name", userToBeValidated.getName())
					.setParameter("surname", userToBeValidated.getSurname()).getSingleResult();

			if (user.isDeleted()) {
				log.warn("Full Name belongs to previously deleted User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_PREVIOUSLY_DELETED"));
			} else {
				log.warn("Full Name belongs to active User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_DUPLICATE_FULL_NAME"));
			}

		} catch (NoResultException e) {
			log.info("Full Name " + userToBeValidated.getName() + " " + userToBeValidated.getSurname() + " is Valid");
			return true;
		}

	}

	@Override
	public boolean isValidPhoneNumber(User userToBeValidated) throws GeneralException {

		try {
			User user = (User) entityManager
					.createQuery("Select user From User user Where user.phoneNumber=:phoneNumber", User.class)
					.setParameter("phoneNumber", userToBeValidated.getPhoneNumber()).getSingleResult();

			if (user.isDeleted()) {
				log.warn("Phone Number belongs to previously deleted User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_PREVIOUSLY_DELETED"));
			} else {
				log.warn("Phone Number belongs to active User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_DUPLICATE_PHONE"));
			}

		} catch (NoResultException e) {
			log.info("Phone Number " + userToBeValidated.getPhoneNumber() + " is Valid");
			return true;
		}

	}

	@Override
	public boolean isValidEmail(User userToBeValidated) throws GeneralException {

		try {
			User user = (User) entityManager
					.createQuery("Select user From User user Where user.email=:email", User.class)
					.setParameter("email", userToBeValidated.getEmail()).getSingleResult();

			if (user.isDeleted()) {
				log.warn("Email Address belongs to previously deleted User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_PREVIOUSLY_DELETED"));
			} else {
				log.warn("Email Address belongs to active User" + user.getUsername() + "!");
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_DUPLICATE_EMAIL"));
			}
		} catch (NoResultException e) {
			log.info("Email Address " + userToBeValidated.getEmail() + " is Valid");
			return true;
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canBeDeleted(Integer userId) throws GeneralException {
		try{
			ArrayList<EmployeeCertification> results = (ArrayList<EmployeeCertification>) entityManager
											.createQuery("Select ec from EmployeeCertification ec Where ec.user.id=:id AND ec.deleted=0")
											.setParameter("id", userId)
											.getResultList();
				if(results.isEmpty()) {
					return true;
				}else {
					log.info(MessageUtility.getMessage("EMPLOYEE_FORBID_DELETE"));
					throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_FORBID_DELETE"));	
				}
			}catch(NoResultException e) {
				return true;
			}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canBeDeletedPermanently(Integer userId) throws GeneralException {
		try{
			ArrayList<EmployeeCertification> results = (ArrayList<EmployeeCertification>) entityManager
											.createQuery("Select ec from EmployeeCertification ec Where ec.user.id=:id")
											.setParameter("id", userId)
											.getResultList();
				if(results.isEmpty()) {
					return true;
				}else {
					log.info(MessageUtility.getMessage("EMPLOYEE_FORBID_DELETE"));
					throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_FORBID_DELETE"));	
				}
			}catch(NoResultException e) {
				return true;
			}
	}

	@Override
	public int getTotalRows(int managerId) {
		return ((Number)entityManager.createQuery("select count(u) from User u Where u.manager.id=:managerId")
				.setParameter("managerId", managerId)
                .getSingleResult()).intValue();
	}

	@Override
	public int getTotalDeletedRows(int managerId) {
		return ((Number)entityManager.createQuery("select count(u) from User u where u.deleted=1 and u.manager.id=:managerId")
				.setParameter("managerId", managerId)
                .getSingleResult()).intValue();
	}
	
	
	
}
