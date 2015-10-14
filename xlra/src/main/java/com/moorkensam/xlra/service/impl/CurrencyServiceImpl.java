package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

/**
 * Service to fetch and update currencys (CHF).
 * 
 * @author bas
 *
 */
@Stateless
public class CurrencyServiceImpl implements CurrencyService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private ConfigurationDao xlraConfigurationDAO;

	@Inject
	private LogDAO logDAO;

	@Inject
	private CurrencyRateDAO currencyRateDAO;

	@Override
	public void updateCurrencyRate(CurrencyRate currencyRate) {
		currencyRateDAO.updateCurrencyRate(currencyRate);
	}

	@Override
	public void createCurrencyRate(CurrencyRate currencyRate) {
		currencyRateDAO.createCurrencyRate(currencyRate);
	}

	@Override
	public List<CurrencyRate> getAllCurrencyRates() {
		return currencyRateDAO.getAllCurrencyRates();
	}

	@Override
	public List<CurrencyRate> getAllChfRates() {
		return currencyRateDAO.getAllChfRates();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateCurrentChfValue(BigDecimal value) {
		Configuration config = xlraConfigurationDAO.getXlraConfiguration();

		LogRecord createChfLogRecord = LogRecordFactory
				.createChfLogRecord(config.getCurrentChfValue());
		logger.info("saving chfprice logrecord" + createChfLogRecord);
		logDAO.createLogRecord(createChfLogRecord);

		config.setCurrentChfValue(value);
		logger.info("Saving current chf rate " + config.getCurrentChfValue());
		xlraConfigurationDAO.updateXlraConfiguration(config);
	}

	@Override
	public CurrencyRate getChfRateForCurrentPrice(BigDecimal price)
			throws RateFileException {
		List<CurrencyRate> allRates = getAllChfRates();
		if (allRates == null || allRates.isEmpty()) {
			throw new RateFileException(
					"No Chf rates found, please add chf rates first.");
		}
		for (CurrencyRate rate : allRates) {
			if (rate.getInterval().getStart() <= price.doubleValue()
					&& rate.getInterval().getEnd() > price.doubleValue()) {
				return rate;
			}
		}

		throw new RateFileException(
				"Could not find Chf percentage multiplier for chf value of "
						+ price);
	}

}
