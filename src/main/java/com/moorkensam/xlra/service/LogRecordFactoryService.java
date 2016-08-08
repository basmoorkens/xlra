package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.LogType;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.security.User;

import java.math.BigDecimal;
import java.util.List;



public interface LogRecordFactoryService {

  /**
   * Creates a chf log record.
   * 
   * @param value the old value.
   * @param newValue the new value.
   * @return the generated log record.
   */
  public abstract LogRecord createChfLogRecord(BigDecimal value, BigDecimal newValue);

  /**
   * creates a diesel log record.
   * 
   * @param value the old value.
   * @param newValue the new value.
   * @return the generated record.
   */
  public abstract LogRecord createDieselLogRecord(BigDecimal value, BigDecimal newValue);

  /**
   * Create a offerte log record.
   * 
   * @param offerte The offerte to generate a log for.
   * @return The generated logrecord.
   */
  public abstract LogRecord createOfferteLogRecord(QuotationResult offerte);

  /**
   * create a user created log record.
   * 
   * @param affectedUser the user that was created.
   * @param logType the type of log.
   * @return the generaed logrecord.
   */
  public abstract LogRecord createUserRecord(User affectedUser, LogType logType);

  /**
   * create a raise rates record;
   * 
   * @param operation the raise rates operation.
   * @param percentage the percentage.
   * @param fullRateFiles The list of ratefiles to raise;
   * @return the created record.
   */
  public abstract RaiseRatesRecord createRaiseRatesRecord(RateOperation operation,
      double percentage, List<RateFile> fullRateFiles);

}
