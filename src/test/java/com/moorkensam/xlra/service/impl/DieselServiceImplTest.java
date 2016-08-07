package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.DieselRateDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.generator.UserGenerator;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DieselServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private DieselServiceImpl service;

  @Mock
  private UserSessionService userSessionService;

  @Mock
  private DieselRateDao dieselRateDao;

  @Mock
  private LogRecordFactory logFactory;

  @Mock
  private ConfigurationDao configDao;

  @Mock
  private LogService logService;

  private List<DieselRate> rates;

  private DieselRate r1;

  private DieselRate r2;

  private DieselRate existing1;

  private DieselRate existing2;

  /**
   * init the test.
   */
  @Before
  public void init() {
    service = new DieselServiceImpl();
    existing1 = new DieselRate(1.0d, 2.0d);
    existing2 = new DieselRate(2.0d, 3.0d);
    service.setDieselRateDao(dieselRateDao);
    service.setUserSessionService(userSessionService);
    service.setLogRecordFactory(logFactory);
    service.setLogService(logService);
    service.setXlraConfigurationDao(configDao);
    r1 = new DieselRate();
    r1.setInterval(new Interval("1.20", "1.30"));
    r1.setSurchargePercentage(20d);
    r2 = new DieselRate();
    r2.setInterval(new Interval("1.30", "1.40"));
    r2.setSurchargePercentage(40d);
    rates = new ArrayList<DieselRate>();
    rates.add(r2);
    rates.add(r1);
  }

  @Test
  public void testGetChfRateForCurrentPrice() throws RateFileException {
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(rates);

    EasyMockUnitils.replay();
    DieselRate result = service.getDieselRateForCurrentPrice(new BigDecimal(1.25d));
    Assert.assertEquals(r1, result);
  }

  @Test(expected = RateFileException.class)
  public void testGetChfRateForCurrentPriceNotFound() throws RateFileException {
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(rates);

    EasyMockUnitils.replay();
    service.getDieselRateForCurrentPrice(BigDecimal.valueOf(1.55d));

  }

  @Test
  public void testGetChfRateForCurrentPrice2ndCase() throws RateFileException {
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(rates);

    EasyMockUnitils.replay();
    DieselRate result = service.getDieselRateForCurrentPrice(BigDecimal.valueOf(1.35d));
    Assert.assertEquals(r2, result);
  }

  @Test
  public void testGetChfRateForCurrentPriceEdgeCase() throws RateFileException {
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(rates);

    EasyMockUnitils.replay();
    DieselRate result = service.getDieselRateForCurrentPrice(BigDecimal.valueOf(1.30d));
    Assert.assertEquals(r2, result);
  }

  @Test
  public void testDieselUpdateCurrent() {
    User user = UserGenerator.getStandardUser();
    user.setUserName("bmoork");
    Configuration config = new Configuration();
    config.setCurrentDieselPrice(new BigDecimal(1.50d));
    EasyMock.expect(configDao.getXlraConfiguration()).andReturn(config);
    LogRecord log = new RateLogRecord();
    EasyMock.expect(userSessionService.getLoggedInUser()).andReturn(user);
    EasyMock.expect(
        logFactory.createDieselLogRecord(config.getCurrentDieselPrice(), BigDecimal.valueOf(2d),
            "bmoork")).andReturn(log);
    logService.createLogRecord(log);
    EasyMock.expectLastCall();
    configDao.updateXlraConfiguration(config);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();
    service.updateCurrentDieselValue(new BigDecimal(2d));
  }

  @Test
  public void testValidCreateDieselRate() throws IntervalOverlapException {
    DieselRate newRate = new DieselRate(3.0d, 3.5d);
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(
        Arrays.asList(existing1, existing2));
    dieselRateDao.createDieselRate(newRate);
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();
    service.createDieselRate(newRate);
  }

  @Test(expected = IntervalOverlapException.class)
  public void testInvalidIntervalInsertEndValue() throws IntervalOverlapException {
    DieselRate newRate = new DieselRate(0.9d, 1.6d);
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(
        Arrays.asList(existing1, existing2));
    EasyMockUnitils.replay();
    service.createDieselRate(newRate);
  }

  @Test(expected = IntervalOverlapException.class)
  public void testInsertIntervalWithOverlapStart() throws IntervalOverlapException {
    DieselRate newRate = new DieselRate(1.5d, 1.6d);
    EasyMock.expect(dieselRateDao.getAllDieselRates()).andReturn(
        Arrays.asList(existing1, existing2));
    EasyMockUnitils.replay();
    service.createDieselRate(newRate);
  }

}
