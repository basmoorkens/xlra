package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

import java.util.Date;
import java.util.List;

public interface LogService {

  public List<RateLogRecord> getRateLogRecordsByDate(Date startDate, Date endDate);

  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(Date startDate, Date endDate);

  public List<QuotationLogRecord> getQuotationLogRecordsByDate(Date startDate, Date endDate);

}
