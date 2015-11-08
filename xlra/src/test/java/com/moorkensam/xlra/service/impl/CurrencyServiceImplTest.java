package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.RateLogRecord;
import com.moorkensam.xlra.model.configuration.XLRACurrency;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.service.util.LogRecordFactory;

public class CurrencyServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private CurrencyServiceImpl currencyService;

	@Mock
	private CurrencyRateDAO dao;

	@Mock
	private ConfigurationDao configDAO;

	@Mock
	private LogRecordFactory logRecordFactory;

	@Mock
	private LogDAO logDao;

	private List<CurrencyRate> rates;

	private CurrencyRate r1, r2;

	@Before
	public void init() {
		currencyService = new CurrencyServiceImpl();
		currencyService.setCurrencyRateDAO(dao);
		currencyService.setXlraConfigurationDAO(configDAO);
		currencyService.setLogRecordFactory(logRecordFactory);
		currencyService.setLogDAO(logDao);
		r1 = new CurrencyRate();
		r1.setCurrencyType(XLRACurrency.CHF);
		r1.setInterval(new Interval("1.20", "1.30"));
		r1.setSurchargePercentage(20d);
		r2 = new CurrencyRate();
		r2.setCurrencyType(XLRACurrency.CHF);
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
		CurrencyRate result = currencyService
				.getChfRateForCurrentPrice(new BigDecimal(1.25d));
		Assert.assertEquals(r1, result);
	}

	@Test(expected = RateFileException.class)
	public void testGetChfRateForCurrentPriceNotFound()
			throws RateFileException {
		EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

		EasyMockUnitils.replay();
		CurrencyRate result = currencyService
				.getChfRateForCurrentPrice(new BigDecimal(1.55d));

	}

	@Test
	public void testGetChfRateForCurrentPrice2ndCase() throws RateFileException {
		EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

		EasyMockUnitils.replay();
		CurrencyRate result = currencyService
				.getChfRateForCurrentPrice(new BigDecimal(1.35d));
		Assert.assertEquals(r2, result);
	}

	@Test
	public void testGetChfRateForCurrentPriceEdgeCase()
			throws RateFileException {
		EasyMock.expect(dao.getAllChfRates()).andReturn(rates);

		EasyMockUnitils.replay();
		CurrencyRate result = currencyService
				.getChfRateForCurrentPrice(new BigDecimal(1.30d));
		Assert.assertEquals(r2, result);
	}

	@Test(expected = RateFileException.class)
	public void testGetChfRateForCurrentPriceNullChfs()
			throws RateFileException {
		EasyMock.expect(dao.getAllChfRates()).andReturn(null);

		EasyMockUnitils.replay();
		currencyService.getChfRateForCurrentPrice(new BigDecimal(0d));
	}

	@Test
	public void testupdateCurrentChfValue() {
		LogRecord record = new RateLogRecord();
		Configuration config = new Configuration();
		config.setCurrentChfValue(new BigDecimal(110d));
		EasyMock.expect(configDAO.getXlraConfiguration()).andReturn(config);

		EasyMock.expect(
				logRecordFactory.createChfLogRecord(new BigDecimal(110d)))
				.andReturn(record);
		logDao.createLogRecord(record);
		EasyMock.expectLastCall();
		configDAO.updateXlraConfiguration(config);
		EasyMockUnitils.replay();

		currencyService.updateCurrentChfValue(new BigDecimal(100d));
	}
}
