package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

@ManagedBean
@ViewScoped
public class LogOverviewController {

	private List<RateLogRecord> rateLogRecords;

	private List<RaiseRatesRecord> raiseRatesLogRecords;

	private List<QuotationLogRecord> quotationLogRecords;

	@PostConstruct
	public void init() {

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
