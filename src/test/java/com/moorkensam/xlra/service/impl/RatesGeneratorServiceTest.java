package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dto.QuantityGeneratorDto;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.Zone;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;

public class RatesGeneratorServiceTest extends UnitilsJUnit4 {

  @TestedObject
  private RatesGeneratorServiceImpl generator;

  @Before
  public void init() {
    generator = new RatesGeneratorServiceImpl();
  }

  @Test
  public void testKiloGenerate() {
    RateFile rf = new RateFile();
    rf.setMeasurement(Measurement.KILO);
    Zone zone = new Zone();
    rf.addZone(zone);

    RateFile result = generator.generateFreeCreateRateFile(rf);
    Assert.assertEquals(50.0d, result.getRateLines().get(0).getMeasurement());
    Assert.assertEquals(100.0d, result.getRateLines().get(1).getMeasurement());
    Assert.assertEquals(200.0d, result.getRateLines().get(2).getMeasurement());
    Assert.assertEquals(4000.0d, result.getRateLines().get(40).getMeasurement());
    Assert.assertEquals(5000.0d, result.getRateLines().get(41).getMeasurement());
  }
}
