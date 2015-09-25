package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.RaiseRatesRecord;
import com.moorkensam.xlra.model.configuration.LogRecord;

public interface LogDAO {

	public void createLogRecord(LogRecord record);
	
	public List<RaiseRatesRecord> getAllRaiseRateLogRecords();
	
	public RaiseRatesRecord getLastRaiseRates();
	
	public void updateRaiseRatesRecord(RaiseRatesRecord rr);
}
