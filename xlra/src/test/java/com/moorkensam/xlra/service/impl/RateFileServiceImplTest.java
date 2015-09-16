package com.moorkensam.xlra.service.impl;

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
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;

public class RateFileServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private RateFileServiceImpl rateFileServiceImpl;

	@InjectIntoByType
	private Mock<RateFileDAO> rateFileDAOMock;

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
		List<Integer> measurements = new ArrayList<Integer>();
		measurements.add(100);
		measurements.add(200);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Zone 1");
		columnNames.add("Zone 2");
		columnNames.add("Zone 3");
		rateFile.setMeasurementRows(measurements);
		rateFile.setColumns(columnNames);
		List<RateLine> rateLines = new ArrayList<RateLine>();
		rl = new RateLine();
		rl.setMeasurement(100);
		rl.setZone("Zone 1");
		rl.setValue(100);
		rateLines.add(rl);
		rl1 = new RateLine();
		rl1.setMeasurement(100);
		rl1.setZone("Zone 2");
		rl1.setValue(150);
		rateLines.add(rl1);
		rl2 = new RateLine();
		rl2.setMeasurement(100);
		rl2.setZone("Zone 3");
		rl2.setValue(200);
		rateLines.add(rl2);
		rl3 = new RateLine();
		rl3.setMeasurement(200);
		rl3.setZone("Zone 1");
		rl3.setValue(130);
		rateLines.add(rl3);
		rl4 = new RateLine();
		rl4.setMeasurement(200);
		rl4.setZone("Zone 2");
		rl4.setValue(190);
		rateLines.add(rl4);
		rl5 = new RateLine();
		rl5.setMeasurement(200);
		rl5.setZone("Zone 3");
		rl5.setValue(260);
		rateLines.add(rl5);
		rateFile.setRateLines(rateLines);
	}

	@Test
	public void testfillUpRateLineRelationalMap() {
		rateFileServiceImpl.fillUpRateLineRelationalMap(rateFile);
		Assert.assertEquals(2, rateFile.getRelationalRateLines().size());
		Assert.assertEquals(rl, rateFile.getRelationalRateLines().get(0).get(0));
		Assert.assertEquals(rl1, rateFile.getRelationalRateLines().get(0).get(1));
		Assert.assertEquals(rl2, rateFile.getRelationalRateLines().get(0).get(2));
		Assert.assertEquals(rl3, rateFile.getRelationalRateLines().get(1).get(0));
		Assert.assertEquals(rl4, rateFile.getRelationalRateLines().get(1).get(1));
		Assert.assertEquals(rl5, rateFile.getRelationalRateLines().get(1).get(2));
	}

	@Test
	public void testFillUpRelationProperties() {
		rateFileServiceImpl.fillUpRelationalProperties(rateFile);
		rateFileDAOMock.assertInvoked().getDistinctZonesForRateFile(rateFile);
		rateFileDAOMock.assertInvoked().getDistinctMeasurementsForRateFile(
				rateFile);
	}
	
	@Test
	public void testraiseRateFileRateLinesWithPercentage() {
		rateFileServiceImpl.raiseRateFileRateLinesWithPercentage(Arrays.asList(rateFile), 20);
		Assert.assertEquals(120d, rl.getValue());
		Assert.assertEquals(180d, rl1.getValue());
		Assert.assertEquals(240d, rl2.getValue());
	}
	
	@Test
	public void testconvertPercentageToMultiplier() {
		double result = rateFileServiceImpl.convertPercentageToMultiplier(30);
		Assert.assertEquals(1.30d, result);
	}

}
