package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.dao.UserDao;
import com.moorkensam.xlra.model.error.UserException;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.LogRecordFactoryService;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;


public class UserServiceImplTest extends UnitilsJUnit4 {

  private static final String xlraHash =
      "c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3";

  @TestedObject
  private UserServiceImpl service;

  @Mock
  private UserDao userDao;

  @Mock
  private LogDao logDao;

  @Mock
  private EmailService emailService;

  @Mock
  private User userMock;

  @Mock
  private LogRecordFactoryService logRecordFactory;

  /**
   * Initializes the test.
   */
  @Before
  public void init() {
    service = new UserServiceImpl();
    service.setUserDao(userDao);
    service.setLogDao(logDao);
    service.setEmailService(emailService);
    service.setLogRecordFactoryService(logRecordFactory);
  }

  @Test(expected = UserException.class)
  public void testCreateUserEmailAlreadyExists() throws UserException {
    User user = new User();
    user.setEmail("bas@mail.com");
    EasyMock.expect(userDao.getUserByEmail(user.getEmail())).andReturn(new User());

    EasyMockUnitils.replay();
    service.createUser(user);
  }

  @Test(expected = UserException.class)
  public void testCreateUserUsernameAlreadyExists() throws UserException {
    User user = new User();
    user.setUserName("bmoork");
    user.setEmail("bas@mail.com");
    EasyMock.expect(userDao.getUserByEmail(user.getEmail())).andThrow(new NoResultException());
    EasyMock.expect(userDao.getUserByUserName(user.getUserName())).andReturn(new User());

    EasyMockUnitils.replay();
    service.createUser(user);
  }

  @Test
  public void testCreateUser() throws MessagingException, UserException {
    User user = new User();
    user.setEmail("test@test.com");
    user.setUserName("bmoork");
    LogRecord record = new UserLogRecord();
    EasyMock.expect(userDao.getUserByEmail(user.getEmail())).andThrow(new NoResultException());
    EasyMock.expect(userDao.getUserByUserName(user.getUserName()))
        .andThrow(new NoResultException());
    EasyMock.expect(logRecordFactory.createUserRecord(user, LogType.USER_CREATED))
        .andReturn(record);

    logDao.createLogRecord(record);
    EasyMock.expectLastCall();

    userDao.createUser(user);
    EasyMock.expectLastCall();

    emailService.sendUserCreatedEmail(user);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();

    service.createUser(user);
    Assert.assertEquals(xlraHash, user.getPassword());
    Assert.assertEquals(UserStatus.FIRST_TIME_LOGIN, user.getUserStatus());
  }

  @Test
  public void testSetPasswordAndACtivateUser() {
    User user = new User();
    user.setPassword("xlra");
    LogRecord record = new UserLogRecord();
    EasyMock.expect(logRecordFactory.createUserRecord(user, LogType.USER_ACTIVATED)).andReturn(
        record);
    logDao.createLogRecord(record);
    EasyMock.expectLastCall();

    EasyMock.expect(userDao.updateUser(user)).andReturn(user);
    EasyMockUnitils.replay();

    service.setPasswordAndActivateUser(user, "xlra");

    Assert.assertEquals(xlraHash, user.getPassword());
    Assert.assertEquals(UserStatus.IN_OPERATION, user.getUserStatus());
  }

  @Test
  public void testEnableUserSucces() throws XlraValidationException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.DISABLED);
    EasyMock.expect(userDao.updateUser(user)).andReturn(user);
    EasyMockUnitils.replay();
    service.enableUser(user);

  }

  @Test(expected = XlraValidationException.class)
  public void testEnableUserFailedValidation() throws XlraValidationException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.IN_OPERATION);
    service.enableUser(user);
  }

  @Test(expected = XlraValidationException.class)
  public void testResetPwFailedValidation() throws XlraValidationException, MessagingException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.PASSWORD_RESET);
    service.resetUserPassword(user);
  }

  @Test
  public void testResetPwSucces() throws XlraValidationException, MessagingException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.IN_OPERATION);
    emailService.sendResetPasswordEmail(user);
    EasyMock.expectLastCall();
    EasyMock.expect(userDao.updateUser(user)).andReturn(user);
    EasyMockUnitils.replay();
    service.resetUserPassword(user);
  }

  @Test(expected = XlraValidationException.class)
  public void testDisableUserFailedValidation() throws XlraValidationException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.DISABLED);
    service.disableUser(user);
  }

  @Test
  public void testDisableUserSucces() throws XlraValidationException {
    User user = new User();
    user.setUserName("bmoork");
    user.setUserStatus(UserStatus.IN_OPERATION);
    EasyMock.expect(userDao.updateUser(user)).andReturn(user);
    EasyMockUnitils.replay();
    service.disableUser(user);
  }
}
