package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.Date;

import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.LogType;
import com.moorkensam.xlra.model.configuration.RateLogRecord;

public class LogRecordFactory {

	public static LogRecord createChfLogRecord(BigDecimal value) {
		RateLogRecord record = new RateLogRecord();
		fillInBasicProperties(record);
		record.setRate(value);
		record.setType(LogType.CURRENCYRATE);
		return record;
	}

	private static void fillInBasicProperties(LogRecord record) {
		record.setLogDate(new Date());
	}

	public static LogRecord createDieselLogRecord(BigDecimal value) {
		RateLogRecord record = new RateLogRecord();
		fillInBasicProperties(record);
		record.setType(LogType.DIESELRATE);
		record.setRate(value);
		return record;
	}

}
