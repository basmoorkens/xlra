package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class LogServiceImpl implements LogService {

  @Inject
  private UserService userService;

  @Inject
  private LogDao logDao;

  @Override
  public List<RateLogRecord> getRateLogRecordsByDate(final Date startDate, final Date endDate) {
    return logDao.getRateLogRecordsByDate(startDate, endDate);
  }

  @Override
  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(final Date startDate,
      final Date endDate) {
    return logDao.getRaiseRatesLogRecordByDate(startDate, endDate);
  }

  @Override
  public List<QuotationLogRecord> getQuotationLogRecordsByDate(final Date startDate,
      final Date endDate) {
    return logDao.getQuotationLogRecordsByDate(startDate, endDate);
  }

  @Override
  public void createLogRecord(final LogRecord record) {
    record.setUserName(userService.getCurrentUsername());
    logDao.createLogRecord(record);
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public RaiseRatesRecord getLastRaiseRates() {
    return logDao.getLastRaiseRates();
  }

  @Override
  public void updateRaiseRatesRecord(final RaiseRatesRecord rr) {
    logDao.updateRaiseRatesRecord(rr);
  }

  @Override
  public List<RaiseRatesRecord> getAllRaiseRateLogRecords() {
    return logDao.getAllRaiseRateLogRecords();
  }
}
