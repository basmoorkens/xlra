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
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.XLRACurrency;
import com.moorkensam.xlra.model.error.RateFileException;

public class DieselServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private DieselServiceImpl service;

	@Mock
	private DieselRateDAO dieselRateDAO;

	@Mock
	private ConfigurationDao configDAO;

	@InjectIntoByType
	private LogDAO logDao;

	private List<DieselRate> rates;

	private DieselRate r1, r2;

	private Configuration config;

	@Before
	public void init() {
		service = new DieselServiceImpl();
		service.setDieselRateDAO(dieselRateDAO);
		service.setXlraConfigurationDAO(configDAO);
		r1 = new DieselRate();
		r1.setInterval(new Interval("1.20", "1.30"));
		r1.setSurchargePercentage(20d);
		r2 = new DieselRate();
		r2.setInterval(new Interval("1.30", "1.40"));
		r2.setSurchargePercentage(40d);
		rates = new ArrayList<DieselRate>();
		rates.add(r2);
		rates.add(r1);
		config = new Configuration();
	}

	@Test
	public void testGetChfRateForCurrentPrice() throws RateFileException {
		EasyMock.expect(dieselRateDAO.getAllDieselRates()).andReturn(rates);

		EasyMockUnitils.replay();
		DieselRate result = service
				.getDieselRateForCurrentPrice(new BigDecimal(1.25d));
		Assert.assertEquals(r1, result);
	}

	@Test(expected = RateFileException.class)
	public void testGetChfRateForCurrentPriceNotFound()
			throws RateFileException {
		EasyMock.expect(dieselRateDAO.getAllDieselRates()).andReturn(rates);

		EasyMockUnitils.replay();
		DieselRate result = service
				.getDieselRateForCurrentPrice(new BigDecimal(1.55d));

	}

	@Test
	public void testGetChfRateForCurrentPrice2ndCase() throws RateFileException {
		EasyMock.expect(dieselRateDAO.getAllDieselRates()).andReturn(rates);

		EasyMockUnitils.replay();
		DieselRate result = service
				.getDieselRateForCurrentPrice(new BigDecimal(1.35d));
		Assert.assertEquals(r2, result);
	}

	@Test
	public void testGetChfRateForCurrentPriceEdgeCase()
			throws RateFileException {
		EasyMock.expect(dieselRateDAO.getAllDieselRates()).andReturn(rates);

		EasyMockUnitils.replay();
		DieselRate result = service
				.getDieselRateForCurrentPrice(new BigDecimal(1.30d));
		Assert.assertEquals(r2, result);
	}

}
