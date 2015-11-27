package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class StringUtilTest extends UnitilsJUnit4 {

	@Test
	public void testStringIsemptyNot() {
		String s = "s";
		Assert.assertFalse(StringUtil.isEmpty(s));
	}

	@Test
	public void testStringIsempty() {
		String s = "";
		Assert.assertTrue(StringUtil.isEmpty(s));
	}

	@Test
	public void testStringIsemptyNull() {
		String s = null;
		Assert.assertTrue(StringUtil.isEmpty(s));
	}
}
