package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.EmailExistsException;
import com.ikubinfo.certification.exception.ErrorMessages;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.mysql.jdbc.ResultSetInternalMethods;

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
			log.info("User: "+user.getUsername()+" was added succesfully!");
			return true;
		} catch (Exception e) {
			log.error("Failed to add user! Error Message :"+e.getMessage());
			return false;
		}
	}
	
	@Transactional
	@Override
	public boolean remove(User user) {
		try {
			entityManager.
			 createQuery("update User set deleted=1 where id=:id")
			.setParameter("id",user.getId())
			.executeUpdate();
			log.info("User: "+user.getUsername()+" was removed succesfully!");
			return true;			
			
		} catch (Exception e) {
			log.info("User: "+user.getUsername()+" failed to be removed! Error message :"+e.getMessage());
			return false;		
			}
	}
	
	@Transactional
	@Override
	public boolean update(User user) {
		try {
			entityManager.merge(user);
			log.info("User: "+user.getUsername()+ " was updated succesfully!");
			return true;
		}
		catch(Exception e){
			log.error("User: "+user.getUsername()+" failed to be updated! Error message :"+e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}

	@Override
	public User findById(int id) {
		try {
			User user =  entityManager.find(User.class, id);
			if(!user.isDeleted()) {
				return user;
			}
			else {
				log.warn("User: "+user.getUsername()+" is deleted!");
				return null;
			}
		} catch (Exception e) {
			log.warn("User cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<User> getAllActive(int id) {
		try {
			return (ArrayList<User>) entityManager
					.createQuery("Select user from User user Where user.manager=:manager And deleted=0")
					.setParameter("manager", findById(id))
					.getResultList();
				
		} catch (Exception e) {
			log.info("Users cannot be found! Error message :"+e.getMessage());
			return null;
		}
	}

	@Override
	public User exists(String username, String password) {
		try {
			User user;
			user= (User)entityManager
			.createQuery("Select user From User user Where user.username=:username And user.deleted=0",User.class)
			.setParameter("username", username)
			.getSingleResult();
			
			//Compare passwords in encrypted forms
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			if(passwordEncryptor.checkPassword(password, user.getPassword())) {
				log.info("User Found!");
				return user;
			}
			else {
				log.warn("Wrong credentials!");
				return null;
			}
		} catch (NoResultException e) {
			log.warn("User cannot be found!(Maybe wrong credentials) Error message :"+e.getMessage());
			return null;
		
		}
	}

	@SuppressWarnings("null")
	@Override
	public boolean isValidUsername(User userToBeValidated) throws GeneralException {
		try {
			User user;
			user= (User)entityManager
			.createQuery("Select user From User user Where user.username=:username",User.class)
			.setParameter("username", userToBeValidated.getUsername())
			.getSingleResult();
			if(user.isDeleted()) {
				log.warn("Username belongs to previously deleted User!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_PREVIOUSLY_DELETED.getMessage());
			}
			else {
				log.warn("Username belongs to active User: "+user.getName()+" "+user.getSurname()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_DUPLICATE_USERNAME.getMessage());
			}
			
		}catch (NoResultException e) {
			log.info("Username "+userToBeValidated.getUsername()+" is Valid");
			return true;
		}
	}

	@Override
	public boolean isValidSsn(User userToBeValidated) throws GeneralException {
			try{
				User user= (User)entityManager
				.createQuery("Select user From User user Where user.ssn=:ssn ",User.class)
				.setParameter("ssn",userToBeValidated.getSsn())
				.getSingleResult();
				if(user.isDeleted()) {
					log.warn("SSN belongs to previously deleted User: "+user.getUsername()+"!");
					throw new GeneralException(ErrorMessages.EMPLOYEE_PREVIOUSLY_DELETED.getMessage());
				}
				else {
					log.warn("SSN belongs to active User: "+user.getUsername()+"!");
					throw new GeneralException(ErrorMessages.EMPLOYEE_DUPLICATE_SSN.getMessage());
				}
				
			}catch (NoResultException e) {
				log.warn("SSN "+userToBeValidated.getSsn()+" is Valid");
				return true;
			}
	}

	@Override
	public boolean isValidFullName(User userToBeValidated) throws GeneralException {

		try{
			User user= (User)entityManager
			.createQuery("Select user From User user Where user.name=:name And user.surname=:surname",User.class)
			.setParameter("name",userToBeValidated.getName())
			.setParameter("surname",userToBeValidated.getSurname())
			.getSingleResult();
			
			if(user.isDeleted()) {
				log.warn("Full Name belongs to previously deleted User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_PREVIOUSLY_DELETED.getMessage());
			}
			else {
				log.warn("Full Name belongs to active User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_DUPLICATE_FULL_NAME.getMessage());
			}
			
		}catch (NoResultException e) {
			log.info("Full Name "+userToBeValidated.getName()+" "+userToBeValidated.getSurname()+" is Valid");
			return true;
		}
		
	}
	
	@Override
	public boolean isValidPhoneNumber(User userToBeValidated) throws GeneralException {

		try{
			User user= (User)entityManager
			.createQuery("Select user From User user Where user.phoneNumber=:phoneNumber",User.class)
			.setParameter("phoneNumber",userToBeValidated.getPhoneNumber())
			.getSingleResult();
			
			if(user.isDeleted()) {
				log.warn("Phone Number belongs to previously deleted User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_PREVIOUSLY_DELETED.getMessage());
			}
			else {
				log.warn("Phone Number belongs to active User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_DUPLICATE_PHONE.getMessage());
			}
			
		}catch (NoResultException e) {
			log.info("Phone Number "+userToBeValidated.getPhoneNumber()+" is Valid");
			return true;
		}
		
	}
	
	@Override
	public boolean isValidEmail(User userToBeValidated) throws GeneralException {

		try{
			User user= (User)entityManager
			.createQuery("Select user From User user Where user.email=:email",User.class)
			.setParameter("email",userToBeValidated.getEmail())
			.getSingleResult();

			if(user.isDeleted()) {
				log.warn("Email Address belongs to previously deleted User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_PREVIOUSLY_DELETED.getMessage());
			}
			else {
				log.warn("Email Address belongs to active User"+user.getUsername()+"!");
				throw new GeneralException(ErrorMessages.EMPLOYEE_DUPLICATE_EMAIL.getMessage());
			}
		}catch (NoResultException e) {
			log.info("Email Address "+userToBeValidated.getEmail()+" is Valid");
			return true;
		}
		
	}
}
