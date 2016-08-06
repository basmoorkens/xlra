package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

import java.util.Date;
import java.util.List;

/**
 * Interface for generic logservice.
 * 
 * @author bas
 *
 */
public interface LogService {

  public void createLogRecord(final LogRecord record);

  public List<RateLogRecord> getRateLogRecordsByDate(final Date startDate, final Date endDate);

  public List<QuotationLogRecord> getQuotationLogRecordsByDate(final Date startDate,
      final Date endDate);

  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(final Date startDate,
      final Date endDate);

  public RaiseRatesRecord getLastRaiseRates();

  public void updateRaiseRatesRecord(final RaiseRatesRecord rr);

  public List<RaiseRatesRecord> getAllRaiseRateLogRecords();
}
