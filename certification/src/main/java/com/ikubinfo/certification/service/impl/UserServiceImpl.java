package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService, Serializable {
	
	private static final long serialVersionUID = -1464185810106367640L;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public boolean add(User user) throws DeletedUserException, UsernameExistsException, SsnExistsException, FullNameExistsException  {
		if(isValidUsername(user) && isValidSsn(user) && isValidFullName(user)) {
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
	public boolean update(User userToBeUpdated) throws DeletedUserException, UsernameExistsException, SsnExistsException, FullNameExistsException  {
		User user = findById(userToBeUpdated.getId());
		if(!(userToBeUpdated.getName().equals(user.getName())) && !(userToBeUpdated.getSurname().equals(user.getSurname()))) {
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
			if(!isValidSsn(user)) {
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
	public boolean isValidUsername(User user) throws DeletedUserException, UsernameExistsException{
		return userDao.isValidUsername(user);
	}

	@Override
	public boolean isValidSsn(User user) throws DeletedUserException, SsnExistsException {
		return userDao.isValidSsn(user);
	}

	@Override
	public boolean isValidFullName(User user) throws DeletedUserException, FullNameExistsException {
		return userDao.isValidFullName(user);
	}
	
	
	

	
}
