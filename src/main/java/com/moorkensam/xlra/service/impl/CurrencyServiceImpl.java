package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.CurrencyRateDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.RateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.record.UseSelFSRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 * Service to fetch and update currencys (CHF).
 * 
 * @author bas
 *
 */
@Stateless
public class CurrencyServiceImpl implements CurrencyService {

	private static final Logger logger = LogManager.getLogger();

	@Inject
	private ConfigurationDao xlraConfigurationDao;

	@Inject
	private LogService logService;

	@Inject
	private CurrencyRateDao currencyRateDao;

	@Inject
	private UserSessionService userSessionService;

	@Inject
	private LogRecordFactoryService logRecordFactoryService;

	@PostConstruct
	public void init() {
	}

	@Override
	public void updateCurrencyRate(final CurrencyRate currencyRate) {
		getCurrencyRateDao().updateCurrencyRate(currencyRate);
	}

	@Override
	public void createCurrencyRate(final CurrencyRate currencyRate)
			throws IntervalOverlapException {
		RateUtil.validateRateInterval(currencyRate,
				currencyRateDao.getAllChfRates());
		getCurrencyRateDao().createCurrencyRate(currencyRate);
	}

	@Override
	public List<CurrencyRate> getAllCurrencyRates() {
		return getCurrencyRateDao().getAllCurrencyRates();
	}

	@Override
	public List<CurrencyRate> getAllChfRates() {
		return getCurrencyRateDao().getAllChfRates();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateCurrentChfValue(final BigDecimal value) {
		Configuration config = getXlraConfigurationDao().getXlraConfiguration();
		logUpdateCurrentChfValue(value, config);
		config.setCurrentChfValue(value);
		getXlraConfigurationDao().updateXlraConfiguration(config);
	}

	private void logUpdateCurrentChfValue(final BigDecimal value,
			final Configuration config) {
		String loggedInUserName = getUserSessionService().getLoggedInUser()
				.getUserName();
		LogRecord createChfLogRecord = logRecordFactoryService
				.createChfLogRecord(config.getCurrentChfValue(), value);
		logger.info(loggedInUserName + " updated chfprice "
				+ config.getCurrentChfValue() + " to " + value);
		getLogService().createLogRecord(createChfLogRecord);
	}

	@Override
	public CurrencyRate getChfRateForCurrentPrice(final BigDecimal price)
			throws RateFileException {
		List<CurrencyRate> allRates = getAllChfRates();
		if (allRates == null || allRates.isEmpty()) {
			throw new RateFileException("chfrates.rates.empty");
		}
		for (CurrencyRate rate : allRates) {
			if (rate.getInterval().getStart() <= price.doubleValue()
					&& rate.getInterval().getEnd() > price.doubleValue()) {
				return rate;
			}
		}

		RateFileException rfex = new RateFileException("");
		rfex.setExtraArguments(Arrays.asList(price + ""));
		throw rfex;
	}

	public CurrencyRateDao getCurrencyRateDao() {
		return currencyRateDao;
	}

	public void setCurrencyRateDao(CurrencyRateDao currencyRateDao) {
		this.currencyRateDao = currencyRateDao;
	}

	public ConfigurationDao getXlraConfigurationDao() {
		return xlraConfigurationDao;
	}

	public void setXlraConfigurationDao(ConfigurationDao xlraConfigurationDao) {
		this.xlraConfigurationDao = xlraConfigurationDao;
	}

	@Override
	public void deleteCurrencyRate(CurrencyRate toDelete) {
		logger.info("Deleting currency rate " + toDelete);
		currencyRateDao.deleteCurrencyRate(toDelete);
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public UserSessionService getUserSessionService() {
		return userSessionService;
	}

	public void setUserSessionService(UserSessionService userSessionService) {
		this.userSessionService = userSessionService;
	}

	public LogRecordFactoryService getLogRecordFactoryService() {
		return logRecordFactoryService;
	}

	public void setLogRecordFactoryService(
			LogRecordFactoryService logRecordFactoryService) {
		this.logRecordFactoryService = logRecordFactoryService;
	}

}
