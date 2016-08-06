package com.moorkensam.xlra.service.util;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LogRecordFactory {

  private final Logger logger = LogManager.getLogger();

  private static LogRecordFactory instance;

  private LogRecordFactory() {

  }

  /**
   * Gets the instance of this class.
   * 
   * @return the instance.
   */
  public static LogRecordFactory getInstance() {
    if (instance == null) {
      instance = new LogRecordFactory();
    }
    return instance;
  }

  /**
   * Creates a chf log record.
   * 
   * @param value the old value.
   * @param newValue the new value.
   * @return the generated log record.
   */
  public LogRecord createChfLogRecord(BigDecimal value, BigDecimal newValue, String username) {
    RateLogRecord record = new RateLogRecord();
    fillInBasicProperties(record);
    record.setRate(value);
    record.setType(LogType.CURRENCYRATE);
    record.setUserName(username);
    record.setNewRate(newValue);
    return record;
  }

  private void fillInBasicProperties(LogRecord record) {
    record.setLogDate(new Date());
  }

  /**
   * creates a diesel log record.
   * 
   * @param value the old value.
   * @param newValue the new value.
   * @return the generated record.
   */
  public LogRecord createDieselLogRecord(BigDecimal value, BigDecimal newValue, String username) {
    RateLogRecord record = new RateLogRecord();
    fillInBasicProperties(record);
    record.setType(LogType.DIESELRATE);
    record.setRate(value);
    record.setNewRate(newValue);
    record.setUserName(username);
    return record;
  }

  /**
   * Create a offerte log record.
   * 
   * @param offerte The offerte to generate a log for.
   * @return The generated logrecord.
   */
  public LogRecord createOfferteLogRecord(QuotationResult offerte) {
    QuotationLogRecord record = new QuotationLogRecord();
    record.setCustomerName(offerte.getQuery().getCustomer().getName());
    record.setOfferteKey(offerte.getOfferteUniqueIdentifier());
    record.setUserName(offerte.getCreatedUserFullName());
    fillInBasicProperties(record);
    return record;
  }

  /**
   * create a user created log record.
   * 
   * @param affectedUser the user that was created.
   * @param logType the type of log.
   * @return the generaed logrecord.
   */
  public LogRecord createUserRecord(User affectedUser, LogType logType) {
    UserLogRecord record = new UserLogRecord();
    record.setLogDate(new Date());
    record.setType(logType);
    record.setAffectedAccount(affectedUser.getEmail());
    return record;
  }

  /**
   * create a raise rates record;
   * 
   * @param operation the raise rates operation.
   * @param percentage the percentage.
   * @param fullRateFiles The list of ratefiles to raise;
   * @return the created record.
   */
  public RaiseRatesRecord createRaiseRatesRecord(RateOperation operation, double percentage,
      List<RateFile> fullRateFiles, String username) {
    if (logger.isDebugEnabled()) {
      logger.debug("Creating log record for " + operation);
    }
    RaiseRatesRecord logRecord = new RaiseRatesRecord();
    logRecord.setPercentage(percentage);
    logRecord.setRateFiles(fullRateFiles);
    logRecord.setLogDate(new Date());
    logRecord.setOperation(operation);
    logRecord.setUserName(username);
    return logRecord;
  }

}
