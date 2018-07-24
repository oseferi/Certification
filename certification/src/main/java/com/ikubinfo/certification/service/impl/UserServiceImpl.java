package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public boolean add(User user) {
		return userDao.add(user);
	}

	@Override
	public boolean remove(User user) {
		return userDao.remove(user);
	}

	@Override
	public boolean update(User user) {
		return update(user);
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public ArrayList<User> getAll(int id) {
		return userDao.getAll(id);
	}

	@Override
	public User exists(String username, String password) {
		
		return userDao.exists(username, password);
	}

}
