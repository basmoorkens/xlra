package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.CurrencyRateDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.XlraCurrency;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.generator.UserGenerator;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;

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
import java.util.List;

public class CurrencyServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private CurrencyServiceImpl currencyService;

  @Mock
  private CurrencyRateDao dao;

  @Mock
  private ConfigurationDao configDao;

  @Mock
  private UserSessionService userSessionService;

  @Mock
  private LogRecordFactoryService logRecordFactory;

  @Mock
  private LogService logService;

  private List<CurrencyRate> rates;

  private CurrencyRate r1;

  private CurrencyRate r2;

  /**
   * init the test.
   */
  @Before
  public void init() {
    currencyService = new CurrencyServiceImpl();
    currencyService.setCurrencyRateDao(dao);
    currencyService.setXlraConfigurationDao(configDao);
    currencyService.setLogRecordFactoryService(logRecordFactory);
    currencyService.setLogService(logService);
    currencyService.setUserSessionService(userSessionService);
    r1 = new CurrencyRate();
    r1.setCurrencyType(XlraCurrency.CHF);
    r1.setInterval(new Interval("1.20", "1.30"));
    r1.setSurchargePercentage(20d);
    r2 = new CurrencyRate();
    r2.setCurrencyType(XlraCurrency.CHF);
    r2.setInterval(new Interval("1.30", "1.40"));
    r2.setSurchargePercentage(40d);
    rates = new ArrayList<CurrencyRate>();
    rates.add(r2);
    rates.add(r1);
  }

  @Test
  public void testGetChfRateForCurrentPrice() throws RateFileException {
    EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

    EasyMockUnitils.replay();
    CurrencyRate result = currencyService.getChfRateForCurrentPrice(new BigDecimal(1.25d));
    Assert.assertEquals(r1, result);
  }

  @Test(expected = RateFileException.class)
  public void testGetChfRateForCurrentPriceNotFound() throws RateFileException {
    EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

    EasyMockUnitils.replay();
    currencyService.getChfRateForCurrentPrice(BigDecimal.valueOf(1.55d));

  }

  @Test
  public void testGetChfRateForCurrentPrice2ndCase() throws RateFileException {
    EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

    EasyMockUnitils.replay();
    CurrencyRate result = currencyService.getChfRateForCurrentPrice(BigDecimal.valueOf(1.35d));
    Assert.assertEquals(r2, result);
  }

  @Test
  public void testGetChfRateForCurrentPriceEdgeCase() throws RateFileException {
    EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

    EasyMockUnitils.replay();
    CurrencyRate result = currencyService.getChfRateForCurrentPrice(BigDecimal.valueOf(1.30d));
    Assert.assertEquals(r2, result);
  }

  @Test(expected = RateFileException.class)
  public void testGetChfRateForCurrentPriceNullChfs() throws RateFileException {
    EasyMock.expect(dao.getAllChfRates()).andReturn(null);

    EasyMockUnitils.replay();
    currencyService.getChfRateForCurrentPrice(new BigDecimal(0d));
  }

  @Test
  public void testupdateCurrentChfValue() {
    User user = UserGenerator.getStandardUser();
    Configuration config = new Configuration();
    config.setCurrentChfValue(new BigDecimal(110d));
    EasyMock.expect(configDao.getXlraConfiguration()).andReturn(config);
    EasyMock.expect(userSessionService.getLoggedInUser()).andReturn(user);
    LogRecord record = new RateLogRecord();
    EasyMock
        .expect(logRecordFactory.createChfLogRecord(new BigDecimal(110d), new BigDecimal(100d)))
        .andReturn(record);
    logService.createLogRecord(record);
    EasyMock.expectLastCall();
    configDao.updateXlraConfiguration(config);
    EasyMockUnitils.replay();

    currencyService.updateCurrentChfValue(new BigDecimal(100d));
  }
}
