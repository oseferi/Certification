package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.User;

public interface UserService {

	public boolean add(User user) throws GeneralException ;
	public boolean remove(User user) throws GeneralException;
	public boolean removePermanently(User user) throws GeneralException;
	public boolean update(User user) throws GeneralException;
	public boolean restore(User user);
	
	public User findById(int id);
	public ArrayList<User> getAllActive(int id);
	public ArrayList<User> getAllDisabled(int id);
	
	public User exists(String username, String password);
	public boolean isValidUsername(User user) throws GeneralException;
	public boolean isValidSsn(User user) throws GeneralException;
	public boolean isValidFullName (User user) throws GeneralException ;
	public boolean isValidPhoneNumber(User userToBeValidated) throws GeneralException;
	public boolean isValidEmail(User userToBeValidated) throws GeneralException;
	public boolean canBeDeleted(Integer userId) throws GeneralException;
	public boolean canBeDeletedPermanently(Integer userId) throws GeneralException;
	
	public int getTotalRows() ;
	public int getTotalDeletedRows();
}
