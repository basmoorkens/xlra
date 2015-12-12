package com.moorkensam.xlra.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;
import com.moorkensam.xlra.service.LogService;

public class LogServiceImpl implements LogService {

	@Inject
	private LogDAO logDAO;

	@Override
	public List<RateLogRecord> getRateLogRecordsByDate(Date startDate,
			Date endDate) {
		return logDAO.getRateLogRecordsByDate(startDate, endDate);
	}

	@Override
	public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(Date startDate,
			Date endDate) {
		return logDAO.getRaiseRatesLogRecordByDate(startDate, endDate);
	}

	@Override
	public List<QuotationLogRecord> getQuotationLogRecordsByDate(
			Date startDate, Date endDate) {
		return logDAO.getQuotationLogRecordsByDate(startDate, endDate);
	}

}
