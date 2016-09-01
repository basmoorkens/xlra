package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class LogServiceImpl implements LogService {

  @Inject
  private UserSessionService userSessionService;

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
    record.setUserName(userSessionService.getLoggedInUser().getUserName());
    logDao.createLogRecord(record);
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

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }
}
