package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.UserSessionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Stateless
public class LogRecordFactoryServiceImpl implements LogRecordFactoryService {

  private final Logger logger = LogManager.getLogger();

  @Inject
  private UserSessionService userSessionService;

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.moorkensam.xlra.service.impl.LogRecordFactoryService#createChfLogRecord(java.math.BigDecimal
   * , java.math.BigDecimal)
   */
  @Override
  public LogRecord createChfLogRecord(final BigDecimal value, final BigDecimal newValue) {
    RateLogRecord record = new RateLogRecord();
    fillInBasicProperties(record);
    record.setRate(value);
    record.setType(LogType.CURRENCYRATE);
    record.setNewRate(newValue);
    return record;
  }

  private void fillInBasicProperties(final LogRecord record) {
    record.setLogDate(new Date());
    record.setUserName(userSessionService.getLoggedInUser().getUserName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.moorkensam.xlra.service.impl.LogRecordFactoryService#createDieselLogRecord(java.math.BigDecimal
   * , java.math.BigDecimal)
   */
  @Override
  public LogRecord createDieselLogRecord(BigDecimal value, BigDecimal newValue) {
    RateLogRecord record = new RateLogRecord();
    fillInBasicProperties(record);
    record.setType(LogType.DIESELRATE);
    record.setRate(value);
    record.setNewRate(newValue);
    return record;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.moorkensam.xlra.service.impl.LogRecordFactoryService#createOfferteLogRecord(com.moorkensam
   * .xlra.model.offerte.QuotationResult)
   */
  @Override
  public LogRecord createOfferteLogRecord(final QuotationResult offerte) {
    QuotationLogRecord record = new QuotationLogRecord();
    record.setCustomerName(offerte.getQuery().getCustomer().getName());
    record.setOfferteKey(offerte.getOfferteUniqueIdentifier());
    fillInBasicProperties(record);
    return record;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.moorkensam.xlra.service.impl.LogRecordFactoryService#createUserRecord(com.moorkensam.xlra
   * .model.security.User, com.moorkensam.xlra.model.log.LogType)
   */
  @Override
  public LogRecord createUserRecord(final User affectedUser, final LogType logType) {
    UserLogRecord record = new UserLogRecord();
    record.setType(logType);
    record.setAffectedAccount(affectedUser.getEmail());
    fillInBasicProperties(record);
    return record;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.moorkensam.xlra.service.impl.LogRecordFactoryService#createRaiseRatesRecord(com.moorkensam
   * .xlra.model.rate.RateOperation, double, java.util.List)
   */
  @Override
  public RaiseRatesRecord createRaiseRatesRecord(RateOperation operation, double percentage,
      List<RateFile> fullRateFiles) {
    if (logger.isDebugEnabled()) {
      logger.debug("Creating log record for " + operation);
    }
    RaiseRatesRecord logRecord = new RaiseRatesRecord();
    logRecord.setPercentage(percentage);
    logRecord.setRateFiles(fullRateFiles);
    logRecord.setOperation(operation);
    fillInBasicProperties(logRecord);
    return logRecord;
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

}
