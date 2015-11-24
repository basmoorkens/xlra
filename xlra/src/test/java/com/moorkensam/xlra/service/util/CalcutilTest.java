package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class CalcutilTest extends UnitilsJUnit4 {

	private CalcUtil calcUtil;

	@Before
	public void init() {
		calcUtil = CalcUtil.getInstance();
	}

	@Test
	public void testConvertPercentagEtoBaseMultiplier() {
		BigDecimal result = calcUtil.convertPercentageToBaseMultiplier(20d);
		BigDecimal exp = new BigDecimal(0.20d);
		exp = exp.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(exp, result);
	}

}
