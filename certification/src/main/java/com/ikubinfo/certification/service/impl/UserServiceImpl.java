package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.certification.dao.UserDao;
import com.ikubinfo.certification.dto.PasswordDto;
import com.ikubinfo.certification.exception.GeneralException;
import com.ikubinfo.certification.model.User;
import com.ikubinfo.certification.service.UserService;
import com.ikubinfo.certification.utility.MessageUtility;

@Service("userService")
public class UserServiceImpl implements UserService, Serializable {

	private static Logger log = Logger.getLogger(UserServiceImpl.class);

	private static final long serialVersionUID = -1464185810106367640L;

	@Autowired
	private UserDao userDao;

	@Transactional
	@Override
	public boolean add(User user) throws GeneralException {
		if (validateUser(user)) {
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
			user.setPassword(encryptedPassword);
			return userDao.add(user);
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public boolean remove(User user) throws GeneralException {
		if (canBeDeleted(user.getId())) {
			return userDao.remove(user);
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public boolean removePermanently(User user) throws GeneralException {
		if (canBeDeletedPermanently(user.getId())) {
			return userDao.removePermanently(user);
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public boolean update(User userToBeUpdated) throws GeneralException {
		User user = findById(userToBeUpdated.getId());
		log.info("User " + user.getName() + " " + user.getSurname());
		log.info("User to be validated " + userToBeUpdated.getName() + " " + userToBeUpdated.getSurname());
		if (!(userToBeUpdated.getName().equals(user.getName()))
				&& !(userToBeUpdated.getSurname().equals(user.getSurname()))) {
			log.info("Full names are different");
			if (!isValidFullName(userToBeUpdated)) {
				return false;
			}
		}
		if (!userToBeUpdated.getUsername().equals(user.getUsername())) {
			if (!isValidUsername(userToBeUpdated)) {
				return false;
			}
		}
		if (!userToBeUpdated.getSsn().equals(user.getSsn())) {
			if (!isValidSsn(userToBeUpdated)) {
				return false;
			}
		}

		if (!userToBeUpdated.getPhoneNumber().equals(user.getPhoneNumber())) {
			if (!isValidPhoneNumber(userToBeUpdated)) {
				return false;
			}
		}
		if (!userToBeUpdated.getEmail().equals(user.getEmail())) {
			if (!isValidEmail(userToBeUpdated)) {
				return false;
			}
		}
		return userDao.update(userToBeUpdated);
	}

	@Transactional
	@Override
	public boolean restore(User user) {
		return userDao.restore(user);
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
	public ArrayList<User> getAllDisabled(int id) {
		return userDao.getAllDisabled(id);
	}

	@Override
	public User exists(String username, String password) {
		return userDao.exists(username, password);
	}

	@Transactional
	@Override
	public boolean changePassword(PasswordDto passwordDto) throws GeneralException {
		if (!passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
			if (userDao.exists(passwordDto.getUsername(), passwordDto.getOldPassword()) != null) {
				BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
				String encryptedPassword = passwordEncryptor.encryptPassword(passwordDto.getNewPassword());
				if (userDao.changePassword(passwordDto.getUsername(), encryptedPassword)) {
					log.info(MessageUtility.getMessage("EMPLOYEE_PASSWORD_UPDATED"));
					return true;
				} else {
					throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_UPDATE_PASSWORD_FAIL"));
				}
			} else {
				throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_WRONG_PASSWORD"));
			}
		} else {
			throw new GeneralException(MessageUtility.getMessage("EMPLOYEE_SAME_PASSWORD"));
		}
	}

	@Override
	public boolean isValidUsername(User user) throws GeneralException {
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

	@Override
	public boolean canBeDeleted(Integer userId) throws GeneralException {
		return userDao.canBeDeleted(userId);
	}

	@Override
	public boolean canBeDeletedPermanently(Integer userId) throws GeneralException {
		return userDao.canBeDeletedPermanently(userId);
	}

	private boolean validateUser(User user) throws GeneralException {
		return isValidUsername(user) && isValidSsn(user) && isValidFullName(user) && isValidPhoneNumber(user)
				&& isValidEmail(user);
	}

	@Override
	public int getTotalRows(int managerId) {
		return userDao.getTotalRows(managerId);
	}

	@Override
	public int getTotalDeletedRows(int managerId) {
		return userDao.getTotalDeletedRows(managerId);
	}

}
