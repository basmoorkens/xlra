package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

import java.util.Date;
import java.util.List;

public interface LogDao {

  public void createLogRecord(LogRecord record);

  public List<RaiseRatesRecord> getAllRaiseRateLogRecords();

  public RaiseRatesRecord getLastRaiseRates();

  public void updateRaiseRatesRecord(RaiseRatesRecord rr);

  public List<RateLogRecord> getRateLogRecordsByDate(Date startDate, Date endDate);

  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(Date startDate, Date endDate);

  public List<QuotationLogRecord> getQuotationLogRecordsByDate(Date startDate, Date endDate);
}
