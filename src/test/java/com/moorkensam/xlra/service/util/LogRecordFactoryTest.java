package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.Arrays;

public class LogRecordFactoryTest extends UnitilsJUnit4 {

  @TestedObject
  private LogRecordFactory factory;

  @Before
  public void init() {
    factory = LogRecordFactory.getInstance();
  }

  @Test
  public void testRaisERatesLogRecord() {
    double percentage = 10.00;
    RateOperation operation = RateOperation.RAISE;
    RateFile rf = new RateFile();
    RaiseRatesRecord record =
        factory.createRaiseRatesRecord(operation, percentage, Arrays.asList(rf), "bas");
    Assert.assertNotNull(record);
    Assert.assertEquals(RateOperation.RAISE, record.getOperation());
    Assert.assertEquals(record.getPercentage(), percentage);
    Assert.assertEquals(1, record.getRateFiles().size());
  }

  @Test
  public void testDieselLogRecord() {
    RateLogRecord expected = new RateLogRecord();
    expected.setType(LogType.DIESELRATE);
    expected.setRate(BigDecimal.valueOf(11.44d));
    LogRecord result =
        factory.createDieselLogRecord(BigDecimal.valueOf(11.44d), BigDecimal.valueOf(10d), "bas");
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
    LogRecord result =
        factory.createChfLogRecord(BigDecimal.valueOf(10.05d), BigDecimal.valueOf(10.0d), "bas");
    Assert.assertNotNull(result);
    Assert.assertTrue(result instanceof RateLogRecord);
    RateLogRecord rl = (RateLogRecord) result;
    Assert.assertEquals(expected.getType(), rl.getType());
    Assert.assertEquals(expected.getRate(), rl.getRate());
    Assert.assertEquals(expected.getNewRate(), rl.getNewRate());
  }
}
