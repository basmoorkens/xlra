package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.rate.RateLine;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class CalcutilTest extends UnitilsJUnit4 {

  private CalcUtil calcUtil;

  @Before
  public void init() {
    calcUtil = CalcUtil.getInstance();
  }

  @Test
  public void testConvertPercentagEtoBaseMultiplier() {
    BigDecimal result = calcUtil.convertPercentageToBaseMultiplier(20d);
    BigDecimal exp = BigDecimal.valueOf(0.2d);
    exp = exp.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(exp, result);
  }

  @Test
  public void testRoundQuantityToUpper() {
    RateLine rl1 = new RateLine();
    rl1.setMeasurement(100d);
    RateLine rl2 = new RateLine();
    rl2.setMeasurement(200d);
    RateLine rl3 = new RateLine();
    rl3.setMeasurement(300d);
    BigDecimal rounded =
        calcUtil.roundQuantityToUpperIfExactQuantityNotFound(Arrays.asList(rl1, rl2, rl3),
            new BigDecimal(150d));
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(200d)), rounded);
  }

  @Test
  public void testRoundQuantityToUpperNoRoundNeeded() {
    RateLine rl1 = new RateLine();
    rl1.setMeasurement(100d);
    RateLine rl2 = new RateLine();
    rl2.setMeasurement(200d);
    RateLine rl3 = new RateLine();
    rl3.setMeasurement(300d);
    BigDecimal rounded =
        calcUtil.roundQuantityToUpperIfExactQuantityNotFound(Arrays.asList(rl1, rl2, rl3),
            new BigDecimal(200.0d));
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(200d)), rounded);
  }


}
