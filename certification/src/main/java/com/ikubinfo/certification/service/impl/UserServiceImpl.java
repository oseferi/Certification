package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.loader.plan.exec.internal.LoadQueryJoinAndFetchProcessor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.dao.impl.UserDaoImpl;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.EmailExistsException;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;
import com.mysql.jdbc.log.Log;

@Service("userService")
public class UserServiceImpl implements UserService, Serializable {
	
	private static Logger log = Logger.getLogger(UserServiceImpl.class);

	private static final long serialVersionUID = -1464185810106367640L;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public boolean add(User user) throws GeneralException   {
		if(validateUser(user)) {
				BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
				String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
				user.setPassword(encryptedPassword);
				return userDao.add(user);
			}else {
				return false;
				}
	}

	@Override
	public boolean remove(User user) {
		return userDao.remove(user);
	}

	@Override
	public boolean update(User userToBeUpdated) throws GeneralException  {
		User user = findById(userToBeUpdated.getId());
		log.info("User "+user.getName() +" "+ user.getSurname());
		log.info("User to be validated "+userToBeUpdated.getName()+" "+userToBeUpdated.getSurname());
		if(!(userToBeUpdated.getName().equals(user.getName())) && !(userToBeUpdated.getSurname().equals(user.getSurname()))) {
			log.info("Full names are different");
			if(!isValidFullName(userToBeUpdated)) {
				return false;
			}
		}
		if(!userToBeUpdated.getUsername().equals(user.getUsername())) {
			if(!isValidUsername(userToBeUpdated)) {
				return false;
			}
		}
		if(!userToBeUpdated.getSsn().equals(user.getSsn())) {
			if(!isValidSsn(userToBeUpdated)) {
				return false;
			}
		}
		
		if(!userToBeUpdated.getPhoneNumber().equals(user.getPhoneNumber())) {
			if(!isValidPhoneNumber(userToBeUpdated)) {
				return false;
			}
		}
		if(!userToBeUpdated.getEmail().equals(user.getEmail())) {
			if(!isValidEmail(userToBeUpdated)) {
				return false;
			}
		}
		return userDao.update(userToBeUpdated);
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public ArrayList<User> getAllActive(int id) {
		return userDao.getAllActive(id);
	}

	@Override
	public User exists(String username, String password) {	
		return userDao.exists(username, password);
	}

	@Override
	public boolean isValidUsername(User user) throws GeneralException{
		return userDao.isValidUsername(user);
	}

	@Override
	public boolean isValidSsn(User user) throws GeneralException {
		return userDao.isValidSsn(user);
	}

	@Override
	public boolean isValidFullName(User user) throws GeneralException {
		return userDao.isValidFullName(user);
	}

	@Override
	public boolean isValidPhoneNumber(User userToBeValidated) throws GeneralException {
		return userDao.isValidPhoneNumber(userToBeValidated);
	}

	@Override
	public boolean isValidEmail(User userToBeValidated) throws GeneralException {
		return userDao.isValidEmail(userToBeValidated);
	}
	
	
	private boolean validateUser(User user) throws GeneralException {
		return isValidUsername(user) && isValidSsn(user) && isValidFullName(user) && isValidPhoneNumber(user) && isValidEmail(user);
	}
}
