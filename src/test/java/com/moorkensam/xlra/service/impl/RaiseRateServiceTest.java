package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.service.util.CalcUtil;
import com.moorkensam.xlra.service.util.LogRecordFactory;

public class RaiseRateServiceTest extends UnitilsJUnit4 {

	@TestedObject
	private RaiseRateFileServiceImpl raiseRateFileServiceImpl;

	@Mock
	private RateFileDAO rateFileDAOMock;

	@Mock
	private LogDAO logDao;

	private LogRecordFactory logRecordFactory;

	private RateFile rateFile;

	private RateLine rl;

	private RateLine rl1;

	private RateLine rl2;

	private RateLine rl3;

	private RateLine rl5;

	private RateLine rl4;

	private CalcUtil calcUtil;

	@Before
	public void init() {
		calcUtil = CalcUtil.getInstance();
		rateFile = new RateFile();
		List<Double> measurements = new ArrayList<Double>();
		measurements.add(100d);
		measurements.add(200d);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Zone 1");
		columnNames.add("Zone 2");
		columnNames.add("Zone 3");
		rateFile.setMeasurementRows(measurements);
		rateFile.setColumns(columnNames);
		rateFile.setZones(new ArrayList<Zone>());
		rateFile.getZones().add(new Zone("Zone 1"));
		rateFile.getZones().add(new Zone("Zone 2"));
		rateFile.getZones().add(new Zone("Zone 3"));
		rateFile.getZones().get(0)
				.setAlphaNumericPostalCodesAsString("PZ1,PZ2,PZ3");
		rateFile.getZones().get(0).setZoneType(ZoneType.ALPHANUMERIC_LIST);
		rateFile.getZones().get(1)
				.setAlphaNumericPostalCodesAsString("PZ6,PZ5,PZ4");
		rateFile.getZones().get(1).setZoneType(ZoneType.ALPHANUMERIC_LIST);
		rateFile.getZones().get(2).setNumericalPostalCodesAsString("1000-2000");
		rateFile.getZones().get(2).setZoneType(ZoneType.NUMERIC_CODES);
		for (Zone z : rateFile.getZones()) {
			z.setRateFile(rateFile);
		}
		rl = new RateLine();
		rl.setMeasurement(100);
		rl.setZone(new Zone("Zone 1"));
		rl.setValue(new BigDecimal(100));
		rl1 = new RateLine();
		rl1.setMeasurement(100);
		rl1.setZone(new Zone("Zone 2"));
		rl1.setValue(new BigDecimal(150));
		rl2 = new RateLine();
		rl2.setMeasurement(100);
		rl2.setZone(new Zone("Zone 3"));
		rl2.setValue(new BigDecimal(200));
		rl3 = new RateLine();
		rl3.setMeasurement(200);
		rl3.setZone(new Zone("Zone 1"));
		rl3.setValue(new BigDecimal(130));
		rl4 = new RateLine();
		rl4.setMeasurement(200);
		rl4.setZone(new Zone("Zone 2"));
		rl4.setValue(new BigDecimal(190));
		rl5 = new RateLine();
		rl5.setMeasurement(200);
		rl5.setZone(new Zone("Zone 3"));
		rl5.setValue(new BigDecimal(260));
		List<RateLine> rateLines = new ArrayList<RateLine>();
		rateLines.add(rl);
		rateLines.add(rl1);
		rateLines.add(rl2);
		rateLines.add(rl3);
		rateLines.add(rl4);
		rateLines.add(rl5);
		rateFile.setRateLines(rateLines);
		raiseRateFileServiceImpl = new RaiseRateFileServiceImpl();
		raiseRateFileServiceImpl.setCalcUtil(CalcUtil.getInstance());
		raiseRateFileServiceImpl.setRateFileDAO(rateFileDAOMock);
		logRecordFactory = LogRecordFactory.getInstance();
		raiseRateFileServiceImpl.setLogRecordFactory(logRecordFactory);
		raiseRateFileServiceImpl.setLogDAO(logDao);
	}

	@Test
	public void testfetchFullRateFilesAndApplyRaise() {

		EasyMock.expect(rateFileDAOMock.getFullRateFile(rateFile.getId()))
				.andReturn(rateFile);
		EasyMockUnitils.replay();
		raiseRateFileServiceImpl.fetchFullRateFiles(Arrays.asList(rateFile));
	}

	@Test
	public void testraiseRateFiles() {
		raiseRateFileServiceImpl.raiseRateFiles(10d, Arrays.asList(rateFile),
				RateOperation.RAISE);
		BigDecimal result = new BigDecimal(110.00d);
		BigDecimal r2 = result.setScale(2, RoundingMode.HALF_UP);
		BigDecimal result2 = new BigDecimal(220.00d);
		BigDecimal r3 = result2.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(r2, rateFile.getRateLines().get(0).getValue());
		Assert.assertEquals(r3, rateFile.getRateLines().get(2).getValue());
	}

	@Test
	public void testCancelLastRaiseNoRaiseFound() {
		EasyMock.expect(logDao.getLastRaiseRates()).andReturn(null);
		EasyMockUnitils.replay();

		raiseRateFileServiceImpl.undoLatestRatesRaise();
	}

	@Test
	public void testRaiseRatesWithPercentage() {
		EasyMock.expect(rateFileDAOMock.getFullRateFile(rateFile.getId()))
				.andReturn(rateFile);
		logDao.createLogRecord((LogRecord) EasyMock.anyObject());
		EasyMock.expect(rateFileDAOMock.updateRateFile(rateFile)).andReturn(
				rateFile);

		EasyMockUnitils.replay();
		raiseRateFileServiceImpl.raiseRateFileRateLinesWithPercentage(
				Arrays.asList(rateFile), 10d);
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(110.00d)),
				rateFile.getRateLines().get(0).getValue());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(165.00d)),
				rateFile.getRateLines().get(1).getValue());
	}

	@Test
	public void testApplyRateOperation() {
		EasyMock.expect(rateFileDAOMock.getFullRateFile(rateFile.getId()))
				.andReturn(rateFile);
		logDao.createLogRecord((LogRecord) EasyMock.anyObject());
		EasyMock.expect(rateFileDAOMock.updateRateFile(rateFile)).andReturn(
				rateFile);

		EasyMockUnitils.replay();
		raiseRateFileServiceImpl.applyRateOperation(Arrays.asList(rateFile),
				10d, RateOperation.RAISE);
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(110.00d)),
				rateFile.getRateLines().get(0).getValue());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(165.00d)),
				rateFile.getRateLines().get(1).getValue());
	}

	@Test
	public void testApplyRateOperationNegative() {
		rl.setValue(new BigDecimal(100));
		rl1.setValue(new BigDecimal(150));
		rl2.setValue(new BigDecimal(200));
		rl3.setValue(new BigDecimal(130));
		rl4.setValue(new BigDecimal(190));
		rl5.setValue(new BigDecimal(260));
		EasyMock.expect(rateFileDAOMock.getFullRateFile(rateFile.getId()))
				.andReturn(rateFile);
		logDao.createLogRecord((LogRecord) EasyMock.anyObject());
		EasyMock.expect(rateFileDAOMock.updateRateFile(rateFile)).andReturn(
				rateFile);

		EasyMockUnitils.replay();
		raiseRateFileServiceImpl.applyRateOperation(Arrays.asList(rateFile),
				10d, RateOperation.SUBTRACT);
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(90.00d)),
				rateFile.getRateLines().get(0).getValue());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(135.00d)),
				rateFile.getRateLines().get(1).getValue());
	}
}
