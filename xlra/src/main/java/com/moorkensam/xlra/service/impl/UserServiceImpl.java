package com.moorkensam.xlra.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.UserService;

@Stateless
public class UserServiceImpl implements UserService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private UserDAO userDAO;

	@Inject
	private EmailService emailService;

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Override
	public void createUser(User user) {
		user.setPassword("xlra");
		user.setPassword(makePasswordHash(user.getPassword()));
		userDAO.createUser(user);
		try {
			emailService.sendUserCreatedEmail(user);
		} catch (MessagingException e) {
			MessageUtil.addErrorMessage(
					"Failed to send email",
					"Failed to send out account created email to "
							+ user.getEmail());
		}
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

	protected String makePasswordHash(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				String hex = Integer.toHexString(0xff & digest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
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
