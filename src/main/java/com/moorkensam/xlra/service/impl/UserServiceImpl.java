package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.dao.UserDao;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;
import com.moorkensam.xlra.service.util.TokenUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

@Stateless
public class UserServiceImpl implements UserService {

  private final static Logger logger = LogManager.getLogger();

  @Inject
  private UserDao userDao;

  @Inject
  private LogDao logDao;

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
    return getUserDao().getAllUsers();
  }

  @Override
  public String getCurrentUsername() {
    return getSessionContext().getCallerPrincipal().getName();
  }

  @Override
  public void createUser(User user) {
    LogRecord record = logRecordFactory.createUserRecord(user, LogType.USER_CREATED);
    getLogDao().createLogRecord(record);

    user.setPassword("xlra");
    user.setTokenInfo(TokenUtil.getNextToken());
    user.setPassword(makePasswordHash(user.getPassword()));
    user.setUserStatus(UserStatus.FIRST_TIME_LOGIN);
    getUserDao().createUser(user);
    sendUserCreatedEmail(user);
  }

  protected void sendUserCreatedEmail(User user) {
    try {
      getEmailService().sendUserCreatedEmail(user);
    } catch (MessagingException e) {
      MessageUtil.addErrorMessage("Failed to send email",
          "Failed to send out account created email to " + user.getEmail());
    }
  }

  @Override
  public User updateUser(User user, boolean updatedPw) {
    if (updatedPw) {
      user.setPassword(makePasswordHash(user.getPassword()));
    }
    return getUserDao().updateUser(user);
  }

  @Override
  public User getUserById(long id) {
    return getUserDao().getUserbyId(id);
  }

  @Override
  public void deleteUser(User user) {
    User toDelete = getUserDao().getUserbyId(user.getId());
    getUserDao().deleteUser(toDelete);
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
        if (hex.length() == 1) {
          hexString.append('0');
        }
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
  public void resetUserPassword(User user) throws MessagingException {
    try {
      emailService.sendResetPasswordEmail(user);
      user.setUserStatus(UserStatus.PASSWORD_RESET);
      userDao.updateUser(user);
    } catch (MessagingException e) {
      logger.error(e);
      throw new MessagingException();
    }
  }

  @Override
  public User getUserByEmail(String email) {
    return getUserDao().getUserByEmail(email);
  }

  @Override
  public void resendAccountCreatedEmail(User user) {
    sendUserCreatedEmail(user);
  }

  @Override
  public User isValidPasswordRequest(String email, String token) {
    return getUserDao().isValidPasswordRequest(email, token);
  }

  @Override
  public void setPasswordAndActivateUser(User user, String password) {
    LogRecord record = logRecordFactory.createUserRecord(user, LogType.USER_ACTIVATED);
    getLogDao().createLogRecord(record);
    user.setPassword(makePasswordHash(password));
    user.setUserStatus(UserStatus.IN_OPERATION);
    getUserDao().updateUser(user);
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public LogDao getLogDao() {
    return logDao;
  }

  public void setLogDao(LogDao logDao) {
    this.logDao = logDao;
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
    return userDao.getUserByUserName(username);
  }

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @Override
  public void disableUser(User user) {
    user.setUserStatus(UserStatus.DISABLED);
    updateUser(user, false);
  }

  @Override
  public void enableUser(User user) {
    user.setUserStatus(UserStatus.IN_OPERATION);
    updateUser(user, false);
  }

  @Override
  public User isValidPasswordResetRequest(String email, String token) {
    return userDao.isValidPasswordResetRequest(email, token);
  }
}
