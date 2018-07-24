package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.model.User;

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
			e.printStackTrace();
			return false;
		}
	}

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
			return false;		}
	}

	@Override
	public boolean update(User user) {
		try {
			entityManager.merge(user);
			System.out.println("User was updated succesfully!");
			return true;
		}
		catch(Exception e){
			System.out.println("User failed to be added! Error message :"+e.getMessage());
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
	public ArrayList<User> getAll(int id) {
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
		} catch (Exception e) {
			System.out.println("User cannot be found!(Maybe wrong credentials) Error message :"+e.getMessage());
			return null;
		
		}
	}

	
	
}
