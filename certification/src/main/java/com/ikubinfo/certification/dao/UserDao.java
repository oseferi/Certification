package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.model.User;


public interface UserDao {

	public boolean add(User user);
	public boolean remove(User user);
	public boolean update(User user);
	
	public User findById(int id);
	public ArrayList<User> getAll(int id);
	public User exists(String username, String password);
}
