package com.moorkensam.xlra.model;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.rate.Country;

public class CountryTest extends UnitilsJUnit4 {

	@TestedObject
	private Country country;

	@Test
	public void testInitEmptyLangNameMap() {
		country = new Country();
		country.buildEmptyLanguageMap();
		Assert.assertNotNull(country.getNames());
		Assert.assertEquals(4, country.getNames().size());
	}
}
