package com.moorkensam.xlra.model;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;

public class RateLineTest extends UnitilsJUnit4 {

	@TestedObject
	private RateLine rl;

	@Test
	public void testDeepCopy() {
		rl.setValue(new BigDecimal(100d));
		rl.setZone(new Zone());
		rl.getZone().setName("ZONETEST");
		rl.setMeasurement(10);
		RateLine copy = rl.deepCopy();
		Assert.assertNotNull(rl);
		Assert.assertEquals(new BigDecimal(100d), copy.getValue());
		Assert.assertEquals(copy.getZone().getName(), "ZONETEST");
		Assert.assertEquals(10.0d, copy.getMeasurement());
	}
}
