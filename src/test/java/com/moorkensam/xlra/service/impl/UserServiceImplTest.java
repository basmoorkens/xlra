package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.dao.UserDao;
import com.moorkensam.xlra.model.error.UserException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

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

  @TestedObject
  private UserServiceImpl service;

  @Mock
  private UserDao userDao;

  @Mock
  private LogDao logDao;

  @Mock
  private EmailService emailService;

  private static final String xlraHash =
      "c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3";

  @Mock
  private LogRecordFactory logRecordFactory;

  /**
   * Initializes the test.
   */
  @Before
  public void init() {
    service = new UserServiceImpl();
    service.setLogRecordFactory(logRecordFactory);
    service.setUserDao(userDao);
    service.setLogDao(logDao);
    service.setEmailService(emailService);
  }

  @Test
  public void testGetPasswordHash() {
    String result = service.makePasswordHash("xlra");
    Assert.assertEquals(xlraHash, result);
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
}
