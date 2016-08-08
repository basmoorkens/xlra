package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.DieselRateDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.RateUtil;

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
  private LogService logService;

  @Inject
  private UserSessionService userSessionService;

  @Inject
  private LogRecordFactoryService logRecordFactoryService;


  @Override
  public void updateDieselRate(final DieselRate dieselRate) {
    getDieselRateDao().updateDieselRate(dieselRate);
  }

  @Override
  public void createDieselRate(final DieselRate dieselRate) throws IntervalOverlapException {
    RateUtil.validateRateInterval(dieselRate, dieselRateDao.getAllDieselRates());
    getDieselRateDao().createDieselRate(dieselRate);
  }

  @Override
  public List<DieselRate> getAllDieselRates() {
    return getDieselRateDao().getAllDieselRates();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateCurrentDieselValue(final BigDecimal value) {
    Configuration config = getXlraConfigurationDao().getXlraConfiguration();

    logUpdateCurrentDieselValue(value, config);

    config.setCurrentDieselPrice(value);
    logger.info("Saving current diesel price" + config.getCurrentDieselPrice());
    getXlraConfigurationDao().updateXlraConfiguration(config);
  }

  private void logUpdateCurrentDieselValue(final BigDecimal value, final Configuration config) {
    User loggedInUser = userSessionService.getLoggedInUser();
    LogRecord createDieselLogRecord =
        getLogRecordFactoryService().createDieselLogRecord(config.getCurrentDieselPrice(), value);
    logger.info(loggedInUser.getUserName() + " updated dieselprice "
        + config.getCurrentDieselPrice() + " to " + value);
    logService.createLogRecord(createDieselLogRecord);
  }

  @Override
  public DieselRate getDieselRateForCurrentPrice(final BigDecimal price) throws RateFileException {
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

  public void setLogRecordFactoryService(LogRecordFactoryService logRecordFactoryService) {
    this.logRecordFactoryService = logRecordFactoryService;
  }
}
