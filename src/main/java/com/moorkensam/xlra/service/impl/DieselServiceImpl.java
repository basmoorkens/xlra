package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.DieselRateDao;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

@Stateless
public class DieselServiceImpl implements DieselService {

  @Inject
  private DieselRateDao dieselRateDao;

  private final static Logger logger = LogManager.getLogger();

  @Inject
  private ConfigurationDao xlraConfigurationDao;

  @Inject
  private LogDao logDao;

  @Inject
  private UserService userService;

  private LogRecordFactory logRecordFactory;

  @PostConstruct
  public void init() {
    setLogRecordFactory(LogRecordFactory.getInstance());
  }

  @Override
  public void updateDieselRate(DieselRate dieselRate) {
    getDieselRateDao().updateDieselRate(dieselRate);
  }

  @Override
  public void createDieselRate(DieselRate dieselRate) {
    getDieselRateDao().createDieselRate(dieselRate);
  }

  @Override
  public List<DieselRate> getAllDieselRates() {
    return getDieselRateDao().getAllDieselRates();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateCurrentDieselValue(BigDecimal value) {
    Configuration config = getXlraConfigurationDao().getXlraConfiguration();

    LogRecord createDieselLogRecord =
        logRecordFactory.createDieselLogRecord(config.getCurrentDieselPrice(), value,
            getUserService().getCurrentUsername());
    logger.info("Saving dieselprice logrecord " + createDieselLogRecord);
    getLogDao().createLogRecord(createDieselLogRecord);

    config.setCurrentDieselPrice(value);
    logger.info("Saving current diesel price" + config.getCurrentDieselPrice());
    getXlraConfigurationDao().updateXlraConfiguration(config);
  }

  @Override
  public DieselRate getDieselRateForCurrentPrice(BigDecimal price) throws RateFileException {
    List<DieselRate> rates = getAllDieselRates();
    for (DieselRate rate : rates) {
      if (rate.getInterval().getStart() <= price.doubleValue()
          && rate.getInterval().getEnd() > price.doubleValue()) {
        return rate;
      }
    }
    throw new RateFileException(
        "Could not calculate diesel supplement, no multiplier found for price " + price);
  }

  @Override
  public void deleteDieselRate(DieselRate rate) {
    logger.info("Removing diesel rate " + rate);
    dieselRateDao.deleteDieselRate(rate);
  }

  public LogRecordFactory getLogRecordFactory() {
    return logRecordFactory;
  }

  public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
    this.logRecordFactory = logRecordFactory;
  }

  public LogDao getLogDao() {
    return logDao;
  }

  public void setLogDao(LogDao logDao) {
    this.logDao = logDao;
  }

  public DieselRateDao getDieselRateDao() {
    return dieselRateDao;
  }

  public void setDieselRateDao(DieselRateDao dieselRateDao) {
    this.dieselRateDao = dieselRateDao;
  }

  public ConfigurationDao getXlraConfigurationDao() {
    return xlraConfigurationDao;
  }

  public void setXlraConfigurationDao(ConfigurationDao xlraConfigurationDao) {
    this.xlraConfigurationDao = xlraConfigurationDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
