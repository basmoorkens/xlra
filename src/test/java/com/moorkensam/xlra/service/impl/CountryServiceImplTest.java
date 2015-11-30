package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.dao.ZoneDAO;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;

public class CountryServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private CountryServiceImpl service;

	@Mock
	private CountryDAO countryDAO;

	@Mock
	private ZoneDAO zoneDAO;

	@Before
	public void init() {
		service = new CountryServiceImpl();
		service.setCountryDAO(countryDAO);
		service.setZoneDAO(zoneDAO);
	}

	@Test
	public void testCreateZone() {
		Zone z = new Zone();
		z.setAlphaNumericPostalCodesAsString("aa,ab,ac");
		zoneDAO.createZone(z);
		EasyMock.expectLastCall();

		EasyMockUnitils.replay();
		service.createZone(z);

		Assert.assertNotNull(z.getAlphaNumericPostalCodesAsString());
		Assert.assertNotNull(z.getAlphaNumericalPostalCodes());
	}

	@Test
	public void testgetAllCountries() {
		EasyMock.expect(countryDAO.getAllCountries()).andReturn(
				new ArrayList<Country>());
		EasyMockUnitils.replay();

		service.getAllCountries();
	}

	@Test
	public void testGetCountriesFullLoad() {
		Country c1 = new Country();
		c1.setShortName("1");
		Country c2 = new Country();
		c2.setShortName("2");
		EasyMock.expect(countryDAO.getAllCountriesFullyLoaded()).andReturn(
				Arrays.asList(c1, c2));

		EasyMockUnitils.replay();
		List<Country> res = service.getAllCountriesFullLoad();
		Assert.assertEquals(2, res.size());
		Assert.assertEquals(4, res.get(0).getNames().size());

	}

}
