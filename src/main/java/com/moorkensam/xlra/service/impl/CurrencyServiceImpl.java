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
import com.moorkensam.xlra.dao.CurrencyRateDao;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

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
  private LogDao logDao;

  @Inject
  private CurrencyRateDao currencyRateDao;

  @Inject
  private UserService userService;

  private LogRecordFactory logRecordFactory;

  @PostConstruct
  public void init() {
    logRecordFactory = LogRecordFactory.getInstance();
  }

  @Override
  public void updateCurrencyRate(CurrencyRate currencyRate) {
    getCurrencyRateDao().updateCurrencyRate(currencyRate);
  }

  @Override
  public void createCurrencyRate(CurrencyRate currencyRate) {
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
  public void updateCurrentChfValue(BigDecimal value) {
    Configuration config = getXlraConfigurationDao().getXlraConfiguration();

    LogRecord createChfLogRecord =
        logRecordFactory.createChfLogRecord(config.getCurrentChfValue(), value, getUserService()
            .getCurrentUsername());
    logger.info("saving chfprice logrecord" + createChfLogRecord);
    getLogDao().createLogRecord(createChfLogRecord);

    config.setCurrentChfValue(value);
    logger.info("Saving current chf rate " + config.getCurrentChfValue());
    getXlraConfigurationDao().updateXlraConfiguration(config);
  }

  @Override
  public CurrencyRate getChfRateForCurrentPrice(BigDecimal price) throws RateFileException {
    List<CurrencyRate> allRates = getAllChfRates();
    if (allRates == null || allRates.isEmpty()) {
      throw new RateFileException("No Chf rates found, please add chf rates first.");
    }
    for (CurrencyRate rate : allRates) {
      if (rate.getInterval().getStart() <= price.doubleValue()
          && rate.getInterval().getEnd() > price.doubleValue()) {
        return rate;
      }
    }

    throw new RateFileException("Could not find Chf percentage multiplier for chf value of "
        + price);
  }

  public CurrencyRateDao getCurrencyRateDao() {
    return currencyRateDao;
  }

  public void setCurrencyRateDao(CurrencyRateDao currencyRateDao) {
    this.currencyRateDao = currencyRateDao;
  }

  public LogDao getLogDao() {
    return logDao;
  }

  public void setLogDao(LogDao logDao) {
    this.logDao = logDao;
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

  public LogRecordFactory getLogRecordFactory() {
    return logRecordFactory;
  }

  public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
    this.logRecordFactory = logRecordFactory;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

}
