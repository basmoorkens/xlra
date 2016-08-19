package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.dao.UserDao;
import com.moorkensam.xlra.model.error.UserException;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.PasswordUtil;
import com.moorkensam.xlra.service.util.TokenUtil;
import com.moorkensam.xlra.service.util.UserStatusUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;

@Stateless
public class UserServiceImpl implements UserService {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  private UserDao userDao;

  @Inject
  private LogDao logDao;

  @Inject
  private EmailService emailService;

  @Resource
  private SessionContext sessionContext;

  @Inject
  private LogRecordFactoryService logRecordFactoryService;

  @Override
  public List<User> getAllUsers() {
    return getUserDao().getAllUsers();
  }

  @Override
  public void createUser(final User user) throws UserException {
    validateUserIsUnique(user);
    LogRecord record = getLogRecordFactoryService().createUserRecord(user, LogType.USER_CREATED);
    getLogDao().createLogRecord(record);

    user.setPassword("xlra");
    user.setTokenInfo(TokenUtil.getNextToken());
    user.setPassword(PasswordUtil.makePasswordHash(user.getPassword()));
    user.setUserStatus(UserStatus.FIRST_TIME_LOGIN);
    getUserDao().createUser(user);
    sendUserCreatedEmail(user);
  }

  private void validateUserIsUnique(final User user) throws UserException {
    validateUserEmail(user);
    validateUserUsername(user);
  }

  private void validateUserUsername(final User user) throws UserException {
    try {
      userDao.getUserByUserName(user.getUserName());
      throw new UserException("A user with this username already exists");
    } catch (NoResultException nre) {
      if (logger.isDebugEnabled()) {
        logger.debug("User with username " + user.getUserName() + " was not found");
      }
    }
  }

  private void validateUserEmail(final User user) throws UserException {
    try {
      userDao.getUserByEmail(user.getEmail());
      throw new UserException("A user with this email adres already exists");
    } catch (NoResultException nre) {
      if (logger.isDebugEnabled()) {
        logger.debug("User with username " + user.getUserName() + " was not found");
      }
    }
  }

  protected void sendUserCreatedEmail(final User user) {
    try {
      logger.info("Sending account created email to " + user.getUserName() + " - "
          + user.getEmail());
      getEmailService().sendUserCreatedEmail(user);
    } catch (MessagingException e) {
      // TODO refactor this...cant call frontend code from the backend silly...
      // MessageUtil.addErrorMessage("Failed to send email",
      // "Failed to send out account created email to " + user.getEmail());
    }
  }

  @Override
  public User updateUser(final User user, boolean updatedPw) {
    if (updatedPw) {
      user.setPassword(PasswordUtil.makePasswordHash(user.getPassword()));
    }
    return getUserDao().updateUser(user);
  }

  @Override
  public User getUserById(long id) {
    return getUserDao().getUserbyId(id);
  }

  @Override
  public void deleteUser(final User user) {
    logger.info("Deleting user " + user.getUserName());
    User toDelete = getUserDao().getUserbyId(user.getId());
    getUserDao().deleteUser(toDelete);
  }

  @Override
  public void resetUserPassword(final User user) throws MessagingException, XlraValidationException {
    validateResetRequest(user);
    try {
      logger
          .info("Sending reset password email to " + user.getUserName() + " - " + user.getEmail());
      emailService.sendResetPasswordEmail(user);
      user.setUserStatus(UserStatus.PASSWORD_RESET);
      userDao.updateUser(user);
    } catch (MessagingException e) {
      logger.error(e);
      throw new MessagingException();
    }
  }

  private void validateResetRequest(final User user) throws XlraValidationException {
    if (!UserStatusUtil.canResetPassword(user)) {
      String businessException =
          "Can not change the user status to PASSWORD_RESET for user " + user.getUserName()
              + " which has status " + user.getUserStatus();
      logger.error(businessException);
      throw new XlraValidationException(businessException);
    }
  }

  @Override
  public User getUserByEmail(final String email) {
    return getUserDao().getUserByEmail(email);
  }

  @Override
  public void resendAccountCreatedEmail(final User user) {
    sendUserCreatedEmail(user);
  }

  @Override
  public User isValidPasswordRequest(final String email, final String token) {
    return getUserDao().isValidPasswordRequest(email, token);
  }

  @Override
  public void setPasswordAndActivateUser(final User user, final String password) {
    logger.info("User " + user.getUserName() + " is now activated");
    LogRecord record = getLogRecordFactoryService().createUserRecord(user, LogType.USER_ACTIVATED);
    getLogDao().createLogRecord(record);
    user.setPassword(PasswordUtil.makePasswordHash(password));
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

  @Override
  public User getUserByUserName(final String username) {
    return userDao.getUserByUserName(username);
  }

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @Override
  public void disableUser(final User user) throws XlraValidationException {
    validateDisableRequest(user);
    user.setUserStatus(UserStatus.DISABLED);
    updateUser(user, false);
  }

  private void validateDisableRequest(final User user) throws XlraValidationException {
    if (!UserStatusUtil.canDisableUser(user)) {
      String businessException =
          "Can not disable user " + user.getUserName() + " with status " + user.getUserStatus();
      logger.error(businessException);
      throw new XlraValidationException(businessException);
    }
  }

  @Override
  public void enableUser(final User user) throws XlraValidationException {
    validateEnableRequest(user);
    user.setUserStatus(UserStatus.IN_OPERATION);
    updateUser(user, false);
  }

  private void validateEnableRequest(final User user) throws XlraValidationException {
    if (!UserStatusUtil.canEnableUser(user)) {
      String businessException =
          "Can not enable user " + user.getUserName() + " with status " + user.getUserStatus();
      logger.error(businessException);
      throw new XlraValidationException(businessException);
    }
  }

  @Override
  public User isValidPasswordResetRequest(final String email, final String token) {
    return userDao.isValidPasswordResetRequest(email, token);
  }

  public LogRecordFactoryService getLogRecordFactoryService() {
    return logRecordFactoryService;
  }

  public void setLogRecordFactoryService(LogRecordFactoryService logRecordFactoryService) {
    this.logRecordFactoryService = logRecordFactoryService;
  }
}
