package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.RateFileDao;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.RaiseRateFileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.CalcUtil;
import com.moorkensam.xlra.service.util.LogRecordFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
public class RaiseRateFileServiceImpl implements RaiseRateFileService {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  private LogService logService;

  private LogRecordFactory logRecordFactory;

  @Inject
  private RateFileDao rateFileDao;

  @Inject
  private UserSessionService userSessionService;

  private CalcUtil calcUtil;

  @PostConstruct
  public void init() {
    setCalcUtil(CalcUtil.getInstance());
    setLogRecordFactory(LogRecordFactory.getInstance());
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles, double percentage) {
    logger.info("Raising ratelines with percentage " + percentage);
    applyRateOperation(rateFiles, percentage, RateOperation.RAISE);
  }

  @Override
  public List<RaiseRatesRecord> getRaiseRatesLogRecordsThatAreNotUndone() {
    return logService.getAllRaiseRateLogRecords();
  }

  @Override
  public void undoLatestRatesRaise() {
    RaiseRatesRecord lastRaise = logService.getLastRaiseRates();
    if (lastRaise == null) {
      logger.info("No raise found in the database");
    } else {
      logger.info("Subtracting latest raise");
      applyRateOperation(lastRaise.getRateFiles(), lastRaise.getPercentage(),
          RateOperation.SUBTRACT);
      lastRaise.setUndone(true);
      logService.updateRaiseRatesRecord(lastRaise);
    }
  }

  /**
   * Applies a RateOperation to a set of ratefiles. Concrete this means that the values in each
   * rateline in each ratefile will be diminished or added with the percentage.
   * 
   * @param rateFiles The rate files to raise.
   * @param percentage The percentage to raise with.
   * @param operation The operation (raise of diminish)
   */
  protected void applyRateOperation(List<RateFile> rateFiles, double percentage,
      RateOperation operation) {
    List<RateFile> fullRateFiles = fetchFullRateFiles(rateFiles);

    raiseRateFiles(percentage, fullRateFiles, operation);

    logRaiseRates(percentage, operation, fullRateFiles);

    for (RateFile rf : fullRateFiles) {
      if (operation == RateOperation.RAISE) {
        logger.info("Saving raised rates file " + rf.getName());
      } else {
        logger.info("Saving subtracted rates file " + rf.getName());
      }
      getRateFileDao().updateRateFile(rf);
    }
  }

  private void logRaiseRates(double percentage, RateOperation operation,
      List<RateFile> fullRateFiles) {
    RaiseRatesRecord createRaiseRatesRecord =
        getLogRecordFactory().createRaiseRatesRecord(operation, percentage, fullRateFiles,
            userSessionService.getLoggedInUser().getUserName());
    logService.createLogRecord(createRaiseRatesRecord);
  }

  /**
   * Raises a list of ratefiles their values.
   * 
   * @param rateLineMultiplier The rateline multiplier.
   * @param fullRateFiles The list of ratefiles.
   * @param operation The operation to execute.
   */
  protected void raiseRateFiles(double percentage, List<RateFile> fullRateFiles,
      RateOperation operation) {
    for (RateFile rf : fullRateFiles) {
      if (operation == RateOperation.RAISE) {
        raiseRateLinesOfRateFile(percentage, rf);
      }
      if (operation == RateOperation.SUBTRACT) {
        lowerRateLinesOfRateFile(percentage, rf);
      }
    }
  }

  private void raiseRateLinesOfRateFile(double percentage, RateFile rf) {
    for (RateLine rl : rf.getRateLines()) {
      BigDecimal percentageDividedby100 = new BigDecimal(percentage / 100d);
      percentageDividedby100 = calcUtil.roundBigDecimal(percentageDividedby100);
      BigDecimal addition =
          new BigDecimal(rl.getValue().doubleValue() * percentageDividedby100.doubleValue());
      addition = calcUtil.roundBigDecimal(addition);
      BigDecimal result = new BigDecimal(rl.getValue().doubleValue() + addition.doubleValue());
      result = calcUtil.roundBigDecimal(result);
      rl.setValue(result);
    }
  }

  /**
   * Fetches a list of ratefiles that are fully eager loaded.
   * 
   * @param rateFiles The list of ratelines.
   * @param rateLineMultiplier The multiplier.
   * @return The operation to execute.
   */
  protected List<RateFile> fetchFullRateFiles(List<RateFile> rateFiles) {
    List<RateFile> fullRateFiles = new ArrayList<RateFile>();
    for (RateFile rf : rateFiles) {
      fullRateFiles.add(getRateFileDao().getFullRateFile(rf.getId()));
    }

    return fullRateFiles;
  }

  protected void lowerRateLinesOfRateFile(double percentage, RateFile rf) {
    for (RateLine rl : rf.getRateLines()) {
      BigDecimal dividedBy100 = new BigDecimal(percentage / 100d);
      dividedBy100 = calcUtil.roundBigDecimal(dividedBy100);
      BigDecimal subtraction =
          new BigDecimal(rl.getValue().doubleValue() * dividedBy100.doubleValue());
      subtraction = calcUtil.roundBigDecimal(subtraction);
      BigDecimal bd2 = new BigDecimal(rl.getValue().doubleValue() - subtraction.doubleValue());
      bd2 = calcUtil.roundBigDecimal(bd2);
      rl.setValue(bd2);
    }
  }

  public LogRecordFactory getLogRecordFactory() {
    return logRecordFactory;
  }

  public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
    this.logRecordFactory = logRecordFactory;
  }

  public RateFileDao getRateFileDao() {
    return rateFileDao;
  }

  public void setRateFileDao(RateFileDao rateFileDao) {
    this.rateFileDao = rateFileDao;
  }

  public CalcUtil getCalcUtil() {
    return calcUtil;
  }

  public void setCalcUtil(CalcUtil calcUtil) {
    this.calcUtil = calcUtil;
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

}
