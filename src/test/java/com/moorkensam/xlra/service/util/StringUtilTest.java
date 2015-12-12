package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class StringUtilTest extends UnitilsJUnit4 {

	@Test
	public void testStringIsemptyNot() {
		String str = "s";
		Assert.assertFalse(StringUtil.isEmpty(str));
	}

	@Test
	public void testStringIsempty() {
		String str = "";
		Assert.assertTrue(StringUtil.isEmpty(str));
	}

	@Test
	public void testStringIsemptyNull() {
		String str = null;
		Assert.assertTrue(StringUtil.isEmpty(str));
	}
}
