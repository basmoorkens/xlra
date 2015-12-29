package com.moorkensam.xlra.model;

import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.Arrays;

public class RateFileTest extends UnitilsJUnit4 {

  @TestedObject
  private RateFile rateFile;

  /**
   * Exec before test.
   */
  @Before
  public void init() {
    rateFile = new RateFile();
    Zone zone1 = new Zone();
    zone1.setName("Zone 1");
    Zone zone2 = new Zone();
    zone2.setName("Zone 2");
    rateFile.setZones(Arrays.asList(zone1, zone2));
    RateLine rl1 = new RateLine();
    rl1.setMeasurement(5);
    rl1.setValue(new BigDecimal(1000d));
    rl1.setZone(zone1);
    RateLine rl2 = new RateLine();
    rl2.setMeasurement(5);
    rl2.setValue(new BigDecimal(1500d));
    rl2.setZone(zone2);
    RateLine rl3 = new RateLine();
    rl3.setMeasurement(6);
    rl3.setValue(new BigDecimal(1100d));
    rl3.setZone(zone1);
    RateLine rl4 = new RateLine();
    rl4.setMeasurement(6);
    rl4.setValue(new BigDecimal(1600d));
    rl4.setZone(zone2);
    RateLine rl5 = new RateLine();
    rl5.setMeasurement(7);
    rl5.setValue(new BigDecimal(1200d));
    rl5.setZone(zone1);
    RateLine rl6 = new RateLine();
    rl6.setMeasurement(7);
    rl6.setValue(new BigDecimal(1700d));
    rl6.setZone(zone2);
    rateFile.setRateLines(Arrays.asList(rl1, rl2, rl3, rl4, rl5, rl6));
  }

  @Test
  public void testFindNumericalPostalCodes() throws RateFileException {
    for (Zone zone : rateFile.getZones()) {
      zone.setZoneType(ZoneType.NUMERIC_CODES);
    }

    rateFile
        .getZones()
        .get(0)
        .setNumericalPostalCodes(
            Arrays.asList(new Interval("1000", "1999"), new Interval("2500", "4000")));
    rateFile
        .getZones()
        .get(1)
        .setNumericalPostalCodes(
            Arrays.asList(new Interval("2000", "2500"), new Interval("4000", "5000")));

    RateLine rl = rateFile.getRateLineForQuantityAndPostalCode(5, "1999");
    Assert.assertNotNull(rl);
    Assert.assertEquals(new BigDecimal(1000d), rl.getValue());

    rl = rateFile.getRateLineForQuantityAndPostalCode(6, "2300");
    Assert.assertNotNull(rl);
    Assert.assertEquals(new BigDecimal(1600d), rl.getValue());

    rl = rateFile.getRateLineForQuantityAndPostalCode(6, "2000");
    Assert.assertNotNull(rl);
    Assert.assertEquals(new BigDecimal(1600d), rl.getValue());
  }

  @Test(expected = RateFileException.class)
  public void testFindAlphaNumericalPostalCodeNotFound() throws RateFileException {
    for (Zone zone : rateFile.getZones()) {
      zone.setZoneType(ZoneType.ALPHANUMERIC_LIST);
    }
    rateFile.getZones().get(0)
        .setAlphaNumericalPostalCodes(Arrays.asList("CX1", "CX2", "CX4", "AA2", "AA1", "CX3A"));
    rateFile.getZones().get(1).setAlphaNumericalPostalCodes(Arrays.asList("AA3", "AA4"));
    rateFile.getRateLineForQuantityAndPostalCode(6, "CX3");
  }

  @Test
  public void testFindAlphaNumericalPostalCode() throws RateFileException {
    for (Zone zone : rateFile.getZones()) {
      zone.setZoneType(ZoneType.ALPHANUMERIC_LIST);
    }
    rateFile.getZones().get(0)
        .setAlphaNumericalPostalCodes(Arrays.asList("CX1", "CX2", "CX4", "AA2", "AA1", "CX3A"));
    rateFile.getZones().get(1).setAlphaNumericalPostalCodes(Arrays.asList("CX3", "AA3", "AA4"));

    RateLine result = rateFile.getRateLineForQuantityAndPostalCode(6, "CX3");
    Assert.assertNotNull(result);
    Assert.assertEquals(new BigDecimal(1600d), result.getValue());

    result = rateFile.getRateLineForQuantityAndPostalCode(7, "AA3");
    Assert.assertNotNull(result);
    Assert.assertEquals(new BigDecimal(1700d), result.getValue());

  }
}
