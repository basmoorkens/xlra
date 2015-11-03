package com.moorkensam.xlra.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;
import com.moorkensam.xlra.service.util.TokenUtil;

@Stateless
public class UserServiceImpl implements UserService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private UserDAO userDAO;

	@Inject
	private LogDAO logDAO;

	@Inject
	private EmailService emailService;

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Override
	public void createUser(User user) {
		LogRecord record = LogRecordFactory.createUserRecord(user);
		logDAO.createLogRecord(record);

		user.setPassword("xlra");
		user.setTokenInfo(TokenUtil.getNextToken());
		user.setPassword(makePasswordHash(user.getPassword()));
		user.setUserStatus(UserStatus.FIRST_TIME_LOGIN);
		userDAO.createUser(user);
		sendUserCreatedEmail(user);
	}

	protected void sendUserCreatedEmail(User user) {
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
		User toDelete = userDAO.getUserbyId(user.getId());
		userDAO.deleteUser(toDelete);
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

	@Override
	public void resendAccountCreatedEmail(User user) {
		sendUserCreatedEmail(user);
	}

	@Override
	public User isValidPasswordRequest(String email, String token) {
		return userDAO.isValidPasswordRequest(email, token);
	}

	@Override
	public void setPasswordAndActivateUser(User user, String password) {
		LogRecord record = LogRecordFactory.createUserRecord(user);
		logDAO.createLogRecord(record);
		user.setPassword(makePasswordHash(password));
		user.setUserStatus(UserStatus.IN_OPERATION);
		user.setEnabled(true);
		userDAO.updateUser(user);
	}
}
