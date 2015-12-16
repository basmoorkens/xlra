package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.service.LogService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class LogServiceImpl implements LogService {

  @Inject
  private LogDao logDao;

  @Override
  public List<RateLogRecord> getRateLogRecordsByDate(Date startDate, Date endDate) {
    return logDao.getRateLogRecordsByDate(startDate, endDate);
  }

  @Override
  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(Date startDate, Date endDate) {
    return logDao.getRaiseRatesLogRecordByDate(startDate, endDate);
  }

  @Override
  public List<QuotationLogRecord> getQuotationLogRecordsByDate(Date startDate, Date endDate) {
    return logDao.getQuotationLogRecordsByDate(startDate, endDate);
  }

}
