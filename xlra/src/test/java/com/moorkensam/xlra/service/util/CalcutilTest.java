package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class CalcutilTest extends UnitilsJUnit4 {

	@Test
	public void testconvertPercentageToMultiplier() {
		BigDecimal result = CalcUtil.convertPercentageToMultiplier(20d);
		BigDecimal expected = new BigDecimal(1.20d);
		expected = expected.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testConvertPercentagEtoBaseMultiplier() {
		BigDecimal result = CalcUtil.convertPercentageToBaseMultiplier(20d);
		BigDecimal exp = new BigDecimal(0.20d);
		exp = exp.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(exp, result);
	}

}
