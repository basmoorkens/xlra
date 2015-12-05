package com.moorkensam.xlra.service.impl;

import javax.mail.MessagingException;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.LogType;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;
import com.moorkensam.xlra.model.security.UserStatus;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

public class UserServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private UserServiceImpl service;

	@Mock
	private UserDAO userDAO;

	@Mock
	private LogDAO logDAO;

	@Mock
	private EmailService emailService;

	private static final String xlraHash = "c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3";

	@Mock
	private LogRecordFactory logRecordFactory;

	@Before
	public void init() {
		service = new UserServiceImpl();
		service.setLogRecordFactory(logRecordFactory);
		service.setUserDAO(userDAO);
		service.setLogDAO(logDAO);
		service.setEmailService(emailService);
	}

	@Test
	public void testGetPasswordHash() {
		String result = service.makePasswordHash("xlra");
		Assert.assertEquals(xlraHash, result);
	}

	@Test
	public void testCreateUser() throws MessagingException {
		User user = new User();
		user.setEmail("test@test.com");
		LogRecord record = new UserLogRecord();
		EasyMock.expect(
				logRecordFactory.createUserRecord(user, LogType.USER_CREATED))
				.andReturn(record);

		logDAO.createLogRecord(record);
		EasyMock.expectLastCall();

		userDAO.createUser(user);
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
		EasyMock.expect(
				logRecordFactory.createUserRecord(user, LogType.USER_ACTIVATED))
				.andReturn(record);
		logDAO.createLogRecord(record);
		EasyMock.expectLastCall();

		EasyMock.expect(userDAO.updateUser(user)).andReturn(user);
		EasyMockUnitils.replay();

		service.setPasswordAndActivateUser(user, "xlra");

		Assert.assertEquals(xlraHash, user.getPassword());
		Assert.assertEquals(UserStatus.IN_OPERATION, user.getUserStatus());
	}
}
