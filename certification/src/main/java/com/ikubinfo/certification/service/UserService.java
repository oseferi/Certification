package com.ikubinfo.certification.service;

import java.util.ArrayList;

import org.hibernate.exception.ConstraintViolationException;

import com.ikubinfo.certification.exception.DeletedUserException;
import com.ikubinfo.certification.exception.EmailExistsException;
import com.ikubinfo.certification.exception.FullNameExistsException;
import com.ikubinfo.certification.exception.PhoneNumberExistsException;
import com.ikubinfo.certification.exception.SsnExistsException;
import com.ikubinfo.certification.exception.UsernameExistsException;
import com.ikubinfo.certification.model.User;

public interface UserService {

	public boolean add(User user) throws DeletedUserException, UsernameExistsException, SsnExistsException, FullNameExistsException, PhoneNumberExistsException, EmailExistsException ;
	public boolean remove(User user);
	public boolean update(User user) throws DeletedUserException, UsernameExistsException, SsnExistsException, FullNameExistsException, PhoneNumberExistsException, EmailExistsException;
	
	public User findById(int id);
	public ArrayList<User> getAllActive(int id);
	public User exists(String username, String password);
	public boolean isValidUsername(User user) throws DeletedUserException, UsernameExistsException;
	public boolean isValidSsn(User user) throws DeletedUserException, SsnExistsException;
	public boolean isValidFullName (User user) throws DeletedUserException, FullNameExistsException ;
	boolean isValidPhoneNumber(User userToBeValidated) throws DeletedUserException, PhoneNumberExistsException;
	boolean isValidEmail(User userToBeValidated) throws DeletedUserException, EmailExistsException;
		
}
