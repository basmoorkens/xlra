package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.RaiseRatesRecord;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.LogType;
import com.moorkensam.xlra.model.configuration.RateLogRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserLogRecord;

public class LogRecordFactory {

	private final static Logger logger = LogManager.getLogger();

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

	public static LogRecord createUserRecord(User affectedUser) {
		UserLogRecord record = new UserLogRecord();
		record.setLogDate(new Date());
		record.setType(LogType.USER_ACTIVATED);
		record.setAffectedAccount(affectedUser.getEmail());
		return record;
	}

	public static RaiseRatesRecord createRaiseRatesRecord(
			RateOperation operation, double percentage,
			List<RateFile> fullRateFiles) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating log record for " + operation);
		}
		RaiseRatesRecord logRecord = new RaiseRatesRecord();
		logRecord.setPercentage(percentage);
		logRecord.setRateFiles(fullRateFiles);
		logRecord.setLogDate(new Date());
		logRecord.setOperation(operation);
		return logRecord;
	}

}
