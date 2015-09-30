package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

@Stateless
public class DieselServiceImpl implements DieselService {

	@Inject
	private DieselRateDAO dieselRateDAO;

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private ConfigurationDao xlraConfigurationDAO;

	@Inject
	private LogDAO logDAO;

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