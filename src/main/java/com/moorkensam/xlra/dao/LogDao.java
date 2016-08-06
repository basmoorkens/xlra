package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

import java.util.Date;
import java.util.List;

public interface LogDao {

  public void createLogRecord(final LogRecord record);

  public List<RaiseRatesRecord> getAllRaiseRateLogRecords();

  public RaiseRatesRecord getLastRaiseRates();

  public void updateRaiseRatesRecord(final RaiseRatesRecord rr);

  public List<RateLogRecord> getRateLogRecordsByDate(final Date startDate, final Date endDate);

  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(final Date startDate,
      final Date endDate);

  public List<QuotationLogRecord> getQuotationLogRecordsByDate(final Date startDate,
      final Date endDate);
}
