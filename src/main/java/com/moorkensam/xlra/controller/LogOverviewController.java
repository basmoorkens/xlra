package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.service.LogService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class LogOverviewController {

  private List<RateLogRecord> rateLogRecords;

  private List<RaiseRatesRecord> raiseRatesLogRecords;

  private List<QuotationLogRecord> quotationLogRecords;

  @Inject
  private LogService logService;

  /**
   * Initialize the controller.
   */
  @PostConstruct
  public void init() {
    Date logDateNow = new Date();
    Date lastMonthDate = getDateMin1Month(logDateNow);
    rateLogRecords = logService.getRateLogRecordsByDate(lastMonthDate, logDateNow);
    raiseRatesLogRecords = logService.getRaiseRatesLogRecordByDate(lastMonthDate, logDateNow);
    quotationLogRecords = logService.getQuotationLogRecordsByDate(lastMonthDate, logDateNow);
  }

  private Date getDateMin1Month(Date inputDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(inputDate);
    calendar.add(Calendar.MONTH, -1);
    return calendar.getTime();
  }

  public List<QuotationLogRecord> getQuotationLogRecords() {
    return quotationLogRecords;
  }

  public void setQuotationLogRecords(List<QuotationLogRecord> quotationLogRecords) {
    this.quotationLogRecords = quotationLogRecords;
  }

  public List<RaiseRatesRecord> getRaiseRatesLogRecords() {
    return raiseRatesLogRecords;
  }

  public void setRaiseRatesLogRecords(List<RaiseRatesRecord> raiseRatesLogRecords) {
    this.raiseRatesLogRecords = raiseRatesLogRecords;
  }

  public List<RateLogRecord> getRateLogRecords() {
    return rateLogRecords;
  }

  public void setRateLogRecords(List<RateLogRecord> rateLogRecords) {
    this.rateLogRecords = rateLogRecords;
  }
}
