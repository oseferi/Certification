package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.mysql.jdbc.ResultSetInternalMethods;

@Repository(value = "UserDao")
@Scope("singleton")
@Component
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public boolean add(User user) {
		try {
			entityManager.persist(user);
			System.out.println("User was added succesfully!");
			return true;
		} catch (Exception e) {
			
			System.out.println("Failed to add user! Error Message :"+e.getMessage());
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
			System.out.println("User was removed succesfully!");
			return true;			
			
		} catch (Exception e) {
			System.out.println("User failed to be removed! Error message :"+e.getMessage());
			return false;		
			}
	}
	
	@Transactional
	@Override
	public boolean update(User user) {
		try {
			entityManager.merge(user);
			System.out.println("User was updated succesfully!");
			return true;
		}
		catch(Exception e){
			System.out.println("User failed to be updated! Error message :"+e.getMessage());
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
				System.out.println("User is deleted!");
				return null;
			}
		} catch (Exception e) {
			System.out.println("User cannot be found! Error message :"+e.getMessage());
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
			System.out.println("Users cannot be found! Error message :"+e.getMessage());
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
				System.out.println("User Found!");
				return user;
			}
			else {
				System.out.println("Wrong credentials!");
				return null;
			}
		} catch (NoResultException e) {
			System.out.println("User cannot be found!(Maybe wrong credentials) Error message :"+e.getMessage());
			return null;
		
		}
	}

	@SuppressWarnings("null")
	@Override
	public boolean isValidUsername(User userToBeValidated) throws DeletedUserException, UsernameExistsException {
		try {
			User user;
			user= (User)entityManager
			.createQuery("Select user From User user Where user.username=:username",User.class)
			.setParameter("username", userToBeValidated.getUsername())
			.getSingleResult();
			if(user.isDeleted()) {
				System.out.println("Username belongs to previously deleted User!");
				throw new DeletedUserException();
			}
			else {
				System.out.println("Username belongs to active User!");
				throw new UsernameExistsException();
			}
			
		}catch (NoResultException e) {
			System.out.println("Username "+userToBeValidated.getUsername()+" is Valid");
			return true;
		}
	}

	@Override
	public boolean isValidSsn(User userToBeValidated) throws DeletedUserException, SsnExistsException {
			try{
				User user= (User)entityManager
				.createQuery("Select user From User user Where user.ssn=:ssn ",User.class)
				.setParameter("ssn",userToBeValidated.getSsn())
				.getSingleResult();
				if(user.isDeleted()) {
					System.out.println("SSN belongs to previously deleted User!");
					throw new DeletedUserException();
				}
				else {
					System.out.println("SSN belongs to active User!");
					throw new SsnExistsException();
				}
				
			}catch (NoResultException e) {
				System.out.println("SSN "+userToBeValidated.getSsn()+" is Valid");
				return true;
			}
	}

	@Override
	public boolean isValidFullName(User userToBeValidated) throws DeletedUserException, FullNameExistsException {

		try{
			User user= (User)entityManager
			.createQuery("Select user From User user Where user.name=:name And user.surname=:surname",User.class)
			.setParameter("name",userToBeValidated.getName())
			.setParameter("surname",userToBeValidated.getSurname())
			.getSingleResult();
			
			if(user.isDeleted()) {
				System.out.println("Full Name belongs to previously deleted User!");
				throw new DeletedUserException();
			}
			else {
				
				System.out.println("Full Name belongs to active User!");
				throw new FullNameExistsException();
			}
			
		}catch (NoResultException e) {
			System.out.println("Full Name "+userToBeValidated.getName()+" "+userToBeValidated.getSurname()+" is Valid");
			return true;
		}
		
	}

	
	
}
