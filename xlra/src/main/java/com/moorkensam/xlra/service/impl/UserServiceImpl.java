package com.moorkensam.xlra.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;

@Stateless
public class UserServiceImpl implements UserService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private UserDAO userDAO;

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Override
	public void createUser(User user) {
		user.setPassword("xlra");
		user.setPassword(makePasswordHash(user.getPassword()));
		userDAO.createUser(user);
	}

	@Override
	public User updateUser(User user) {
		return userDAO.updateUser(user);
	}

	@Override
	public User getUserById(long id) {
		return userDAO.getUserbyId(id);
	}

	@Override
	public void deleteUser(User user) {
		user.setEnabled(false);
		user.setDeleted(true);
		user.setDeletedDateTime(new Date());
		userDAO.updateUser(user);
	}

	private String makePasswordHash(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return new String(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	@Override
	public void resetUserPassword(User user) {
		// update pw
		// send email
	}

	@Override
	public User getUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}

}
