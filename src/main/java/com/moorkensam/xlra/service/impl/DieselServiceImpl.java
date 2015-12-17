package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.DieselRateDao;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
public class DieselServiceImpl implements DieselService {

  @Inject
  private DieselRateDao dieselRateDao;

  private static final Logger logger = LogManager.getLogger();

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
  public void createDieselRate(DieselRate dieselRate) throws IntervalOverlapException {
    validateDieselInterval(dieselRate);
    getDieselRateDao().createDieselRate(dieselRate);
  }

  private void validateDieselInterval(DieselRate dieselRate) throws IntervalOverlapException {
    if (dieselRate.getInterval().getStart() >= dieselRate.getInterval().getEnd()) {
      throw new IntervalOverlapException(
          "The start value of the rate should be smaller then the end value.");
    }
    List<DieselRate> existingRates = dieselRateDao.getAllDieselRates();
    for (DieselRate existing : existingRates) {
      if (dieselRate.getInterval().getStart() >= existing.getInterval().getStart()
          && dieselRate.getInterval().getStart() < existing.getInterval().getEnd()) {
        logger.error("Interval overlap: " + dieselRate.getInterval().toString());
        throw new IntervalOverlapException(
            "The start value of this dieselrate is already in another dieselrate.");
      }
      if (dieselRate.getInterval().getEnd() >= existing.getInterval().getStart()
          && dieselRate.getInterval().getEnd() < existing.getInterval().getEnd()) {
        logger.error("Interval overlap: " + dieselRate.getInterval().toString());
        throw new IntervalOverlapException(
            "The end value of this dieselrate is already in another dieselrate.");
      }
    }
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
