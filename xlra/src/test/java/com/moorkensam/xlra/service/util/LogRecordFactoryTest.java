package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.LogType;
import com.moorkensam.xlra.model.configuration.RateLogRecord;

public class LogRecordFactoryTest extends UnitilsJUnit4 {

	@TestedObject
	private LogRecordFactory factory;

	@Test
	public void testDieselLogRecord() {
		RateLogRecord expected = new RateLogRecord();
		expected.setType(LogType.DIESELRATE);
		expected.setRate(new BigDecimal(11.44d));
		LogRecord result = LogRecordFactory
				.createDieselLogRecord(new BigDecimal(11.44d));
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof RateLogRecord);
		RateLogRecord rl = (RateLogRecord) result;
		Assert.assertEquals(expected.getType(), rl.getType());
		Assert.assertEquals(expected.getRate(), rl.getRate());
	}

	@Test
	public void testGetChflogRecord() {
		RateLogRecord expected = new RateLogRecord();
		expected.setType(LogType.CURRENCYRATE);
		expected.setRate(new BigDecimal(10.05d));
		LogRecord result = LogRecordFactory.createChfLogRecord(new BigDecimal(
				10.05d));
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof RateLogRecord);
		RateLogRecord rl = (RateLogRecord) result;
		Assert.assertEquals(expected.getType(), rl.getType());
		Assert.assertEquals(expected.getRate(), rl.getRate());
	}
}
