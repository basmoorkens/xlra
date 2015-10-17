package com.moorkensam.xlra.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.service.impl.RateFileServiceImpl;

public class RateFileDAOImplTest extends UnitilsJUnit4 {
	@TestedObject
	private RateFileDAOImpl dao;

	private RateFile rateFile;

	private RateLine rl;

	private RateLine rl1;

	private RateLine rl2;

	private RateLine rl3;

	private RateLine rl5;

	private RateLine rl4;

	@Before
	public void init() {
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
		List<RateLine> rateLines = new ArrayList<RateLine>();
		rl = new RateLine();
		rl.setMeasurement(100);
		rl.setZone(new Zone("Zone 1"));
		rl.setValue(new BigDecimal(100));
		rateLines.add(rl);
		rl1 = new RateLine();
		rl1.setMeasurement(100);
		rl1.setZone(new Zone("Zone 2"));
		rl1.setValue(new BigDecimal(150));
		rateLines.add(rl1);
		rl2 = new RateLine();
		rl2.setMeasurement(100);
		rl2.setZone(new Zone("Zone 3"));
		rl2.setValue(new BigDecimal(200));
		rateLines.add(rl2);
		rl3 = new RateLine();
		rl3.setMeasurement(200);
		rl3.setZone(new Zone("Zone 1"));
		rl3.setValue(new BigDecimal(130));
		rateLines.add(rl3);
		rl4 = new RateLine();
		rl4.setMeasurement(200);
		rl4.setZone(new Zone("Zone 2"));
		rl4.setValue(new BigDecimal(190));
		rateLines.add(rl4);
		rl5 = new RateLine();
		rl5.setMeasurement(200);
		rl5.setZone(new Zone("Zone 3"));
		rl5.setValue(new BigDecimal(260));
		rateLines.add(rl5);
		rateFile.setRateLines(rateLines);
	}

	@Test
	public void testfillUpRateLineRelationalMap() {
		dao.fillUpRateLineRelationalMap(rateFile);
		Assert.assertEquals(2, rateFile.getRelationalRateLines().size());
		Assert.assertEquals(rl, rateFile.getRelationalRateLines().get(0).get(0));
		Assert.assertEquals(rl1, rateFile.getRelationalRateLines().get(0)
				.get(1));
		Assert.assertEquals(rl2, rateFile.getRelationalRateLines().get(0)
				.get(2));
		Assert.assertEquals(rl3, rateFile.getRelationalRateLines().get(1)
				.get(0));
		Assert.assertEquals(rl4, rateFile.getRelationalRateLines().get(1)
				.get(1));
		Assert.assertEquals(rl5, rateFile.getRelationalRateLines().get(1)
				.get(2));
	}

	@Test
	public void testFillUpRelationProperties() {
		dao.fillUpRelationalProperties(rateFile);
	}

	@Test
	public void testPrepareRateFileForFrontEnd() {
		RateFile rfDb = new RateFile();
		Zone zone1 = new Zone();
		zone1.setAlphaNumericalPostalCodes(Arrays.asList("PZ1", "PZ2"));
		zone1.setZoneType(ZoneType.ALPHANUMERIC_LIST);
		Zone zone2 = new Zone();
		zone2.setZoneType(ZoneType.NUMERIC_CODES);
		zone2.setNumericalPostalCodes(Arrays
				.asList(new Interval("1000", "2000")));
		rfDb.setZones(Arrays.asList(zone1, zone2));
		dao.prepareRateFileForFrontend(rfDb);

		Assert.assertNotNull(zone1.getAlphaNumericPostalCodesAsString());
		Assert.assertNotNull(zone2.getNumericalPostalCodesAsString());
	}
}
