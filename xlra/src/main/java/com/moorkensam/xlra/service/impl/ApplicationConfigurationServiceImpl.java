package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

//TODO Implement diesel and chf 4 ciphers after.

@Stateless
public class ApplicationConfigurationServiceImpl implements
		ApplicationConfigurationService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private ConfigurationDao xlraConfigurationDAO;

	@Inject
	private DieselRateDAO dieselRateDAO;

	@Inject
	private CurrencyRateDAO currencyRateDAO;

	@Inject
	private EmailTemplateDAO emailTemplateDAO;

	@Inject
	private LogDAO logDAO;

	@Override
	public void updateXlraConfiguration(Configuration xlraConfiguration) {
		xlraConfigurationDAO.updateXlraConfiguration(xlraConfiguration);
	}

	@Override
	public Configuration getConfiguration() {
		return xlraConfigurationDAO.getXlraConfiguration();
	}

	@Override
	public void updateDieselRate(DieselRate dieselRate) {
		dieselRateDAO.updateDieselRate(dieselRate);
	}

	@Override
	public void createDieselRate(DieselRate dieselRate) {
		dieselRateDAO.createDieselRate(dieselRate);
	}

	@Override
	public List<DieselRate> getAllDieselRates() {
		return dieselRateDAO.getAllDieselRates();
	}

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
	public List<MailTemplate> getAllEmailTemplates() {
		return emailTemplateDAO.getAllTemplates();
	}

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		emailTemplateDAO.updateEmailTemplate(mailTemplate);
	}

	@Override
	public List<CurrencyRate> getAllChfRates() {
		return currencyRateDAO.getAllChfRates();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateCurrentChfValue(double value) {
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateCurrentDieselValue(double value) {
		Configuration config = xlraConfigurationDAO.getXlraConfiguration();

		LogRecord createDieselLogRecord = LogRecordFactory
				.createDieselLogRecord(config.getCurrentDieselPrice());
		logger.info("Saving dieselprice logrecord " + createDieselLogRecord);
		logDAO.createLogRecord(createDieselLogRecord);

		config.setCurrentDieselPrice(value);
		logger.info("Saving current diesel price"
				+ config.getCurrentDieselPrice());
		xlraConfigurationDAO.updateXlraConfiguration(config);
	}

}
