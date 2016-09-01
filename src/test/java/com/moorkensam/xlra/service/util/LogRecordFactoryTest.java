package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.generator.UserGenerator;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.impl.LogRecordFactoryServiceImpl;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.Arrays;

public class LogRecordFactoryTest extends UnitilsJUnit4 {

  @TestedObject
  private LogRecordFactoryServiceImpl factory;

  @Mock
  private UserSessionService userSessionService;

  @Before
  public void init() {
    factory = new LogRecordFactoryServiceImpl();
    factory.setUserSessionService(userSessionService);
  }

  @Test
  public void testRaisERatesLogRecord() {
    double percentage = 10.00;
    RateOperation operation = RateOperation.RAISE;
    RateFile rf = new RateFile();
    EasyMock.expect(userSessionService.getLoggedInUser())
        .andReturn(UserGenerator.getStandardUser());
    EasyMockUnitils.replay();
    RaiseRatesRecord record =
        factory.createRaiseRatesRecord(operation, percentage, Arrays.asList(rf));
    Assert.assertNotNull(record);
    Assert.assertEquals(RateOperation.RAISE, record.getOperation());
    Assert.assertEquals(record.getPercentage(), percentage);
    Assert.assertEquals(1, record.getRateFiles().size());
    Assert.assertEquals(UserGenerator.STANDARD_USERNAME, record.getUserName());
  }

  @Test
  public void testDieselLogRecord() {
    RateLogRecord expected = new RateLogRecord();
    expected.setType(LogType.DIESELRATE);
    expected.setRate(BigDecimal.valueOf(11.44d));
    EasyMock.expect(userSessionService.getLoggedInUser())
        .andReturn(UserGenerator.getStandardUser());
    EasyMockUnitils.replay();
    LogRecord result =
        factory.createDieselLogRecord(BigDecimal.valueOf(11.44d), BigDecimal.valueOf(10d));
    Assert.assertNotNull(result);
    Assert.assertTrue(result instanceof RateLogRecord);
    RateLogRecord rl = (RateLogRecord) result;
    Assert.assertEquals(expected.getType(), rl.getType());
    Assert.assertEquals(expected.getRate(), rl.getRate());
  }

  @Test
  public void getUserCreatedLogRecord() {
    User user = new User();
    user.setEmail("bas@bas.com");
    EasyMock.expect(userSessionService.getLoggedInUser())
        .andReturn(UserGenerator.getStandardUser());
    EasyMockUnitils.replay();
    UserLogRecord record = (UserLogRecord) factory.createUserRecord(user, LogType.USER_ACTIVATED);

    Assert.assertNotNull(record);
    Assert.assertEquals(LogType.USER_ACTIVATED, record.getType());
    Assert.assertEquals(user.getEmail(), record.getAffectedAccount());
    Assert.assertNotNull(record.getLogDate());
  }

  @Test
  public void testGetChflogRecord() {
    RateLogRecord expected = new RateLogRecord();
    expected.setType(LogType.CURRENCYRATE);
    expected.setRate(BigDecimal.valueOf(10.05d));
    expected.setNewRate(BigDecimal.valueOf(10.0d));
    EasyMock.expect(userSessionService.getLoggedInUser())
        .andReturn(UserGenerator.getStandardUser());
    EasyMockUnitils.replay();
    LogRecord result =
        factory.createChfLogRecord(BigDecimal.valueOf(10.05d), BigDecimal.valueOf(10.0d));
    Assert.assertNotNull(result);
    Assert.assertTrue(result instanceof RateLogRecord);
    RateLogRecord rl = (RateLogRecord) result;
    Assert.assertEquals(expected.getType(), rl.getType());
    Assert.assertEquals(expected.getRate(), rl.getRate());
    Assert.assertEquals(expected.getNewRate(), rl.getNewRate());
  }
}
