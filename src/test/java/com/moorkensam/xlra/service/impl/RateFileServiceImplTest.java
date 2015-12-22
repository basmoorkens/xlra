package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.RateFileDao;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RateFileServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private RateFileServiceImpl rateFileServiceImpl;

  @InjectIntoByType
  private Mock<RateFileDao> rateFileDaoMock;

  private RateFile rateFile;

  private RateLine rl;

  private RateLine rl1;

  private RateLine rl2;

  private RateLine rl3;

  private RateLine rl5;

  private RateLine rl4;

  /**
   * init the test.
   */
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
    rateFile.getZones().get(0).setAlphaNumericPostalCodesAsString("PZ1,PZ2,PZ3");
    rateFile.getZones().get(0).setZoneType(ZoneType.ALPHANUMERIC_LIST);
    rateFile.getZones().get(1).setAlphaNumericPostalCodesAsString("PZ6,PZ5,PZ4");
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
  }

  @Test
  public void testCreateRateFile() {
    rateFileServiceImpl.createRateFile(rateFile);
    Assert.assertNotNull(rateFile.getZones().get(0).getAlphaNumericalPostalCodes());
    Assert.assertTrue(rateFile.getZones().get(0).getAlphaNumericalPostalCodes().get(0)
        .equals("PZ1"));
    Assert.assertTrue(rateFile.getZones().get(0).getAlphaNumericalPostalCodes().get(1)
        .equals("PZ2"));
    Assert.assertTrue(rateFile.getZones().get(0).getAlphaNumericalPostalCodes().get(2)
        .equals("PZ3"));
    Assert.assertEquals(rateFile.getZones().get(2).getNumericalPostalCodes().get(0), new Interval(
        "1000", "2000"));
    Assert.assertNull(rateFile.getZones().get(0).getNumericalPostalCodes());
  }

}
