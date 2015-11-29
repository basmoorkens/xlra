package com.moorkensam.xlra.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.LogType;
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

	@Resource
	private SessionContext sessionContext;

	private LogRecordFactory logRecordFactory;

	@PostConstruct
	public void init() {
		setLogRecordFactory(LogRecordFactory.getInstance());
	}

	@Override
	public List<User> getAllUsers() {
		return getUserDAO().getAllUsers();
	}

	@Override
	public String getCurrentUsername() {
		return getSessionContext().getCallerPrincipal().getName();
	}

	@Override
	public void createUser(User user) {
		LogRecord record = logRecordFactory.createUserRecord(user,
				LogType.USER_CREATED);
		getLogDAO().createLogRecord(record);

		user.setPassword("xlra");
		user.setTokenInfo(TokenUtil.getNextToken());
		user.setPassword(makePasswordHash(user.getPassword()));
		user.setUserStatus(UserStatus.FIRST_TIME_LOGIN);
		getUserDAO().createUser(user);
		sendUserCreatedEmail(user);
	}

	protected void sendUserCreatedEmail(User user) {
		try {
			getEmailService().sendUserCreatedEmail(user);
		} catch (MessagingException e) {
			MessageUtil.addErrorMessage(
					"Failed to send email",
					"Failed to send out account created email to "
							+ user.getEmail());
		}
	}

	@Override
	public User updateUser(User user, boolean updatedPw) {
		if (updatedPw) {
			user.setPassword(makePasswordHash(user.getPassword()));
		}
		return getUserDAO().updateUser(user);
	}

	@Override
	public User getUserById(long id) {
		return getUserDAO().getUserbyId(id);
	}

	@Override
	public void deleteUser(User user) {
		User toDelete = getUserDAO().getUserbyId(user.getId());
		getUserDAO().deleteUser(toDelete);
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
		return getUserDAO().getUserByEmail(email);
	}

	@Override
	public void resendAccountCreatedEmail(User user) {
		sendUserCreatedEmail(user);
	}

	@Override
	public User isValidPasswordRequest(String email, String token) {
		return getUserDAO().isValidPasswordRequest(email, token);
	}

	@Override
	public void setPasswordAndActivateUser(User user, String password) {
		LogRecord record = logRecordFactory.createUserRecord(user,
				LogType.USER_ACTIVATED);
		getLogDAO().createLogRecord(record);
		user.setPassword(makePasswordHash(password));
		user.setUserStatus(UserStatus.IN_OPERATION);
		user.setEnabled(true);
		getUserDAO().updateUser(user);
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public LogDAO getLogDAO() {
		return logDAO;
	}

	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public LogRecordFactory getLogRecordFactory() {
		return logRecordFactory;
	}

	public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
		this.logRecordFactory = logRecordFactory;
	}

	@Override
	public User getUserByUserName(String username) {
		return userDAO.getUserByUserName(username);
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
}
