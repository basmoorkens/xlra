package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.CountryDao;
import com.moorkensam.xlra.dao.ZoneDao;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.util.ZoneUtil;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private CountryServiceImpl service;

  @Mock
  private CountryDao countryDao;

  @Mock
  private ZoneDao zoneDao;

  /**
   * init the test.
   */
  @Before
  public void init() {
    service = new CountryServiceImpl();
    service.setCountryDao(countryDao);
    service.setZoneDao(zoneDao);
    service.setZoneUtil(new ZoneUtil());
  }

  @Test
  public void testCreateZone() {
    Zone zone = new Zone();
    zone.setAlphaNumericPostalCodesAsString("aa,ab,ac");
    zoneDao.createZone(zone);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();
    service.createZone(zone);

    Assert.assertNotNull(zone.getAlphaNumericPostalCodesAsString());
    Assert.assertNotNull(zone.getAlphaNumericalPostalCodes());
  }

  @Test
  public void testgetAllCountries() {
    EasyMock.expect(countryDao.getAllCountries()).andReturn(new ArrayList<Country>());
    EasyMockUnitils.replay();

    service.getAllCountries();
  }

  @Test
  public void testGetCountriesFullLoad() {
    Country c1 = new Country();
    c1.setShortName("1");
    Country c2 = new Country();
    c2.setShortName("2");
    EasyMock.expect(countryDao.getAllCountriesFullyLoaded()).andReturn(Arrays.asList(c1, c2));

    EasyMockUnitils.replay();
    List<Country> res = service.getAllCountriesFullLoad();
    Assert.assertEquals(2, res.size());
    Assert.assertEquals(4, res.get(0).getNames().size());

  }

  @Test
  public void testUpdateZone() {
    Zone zone = new Zone();
    zoneDao.updateZone(zone);
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();
    service.updateZone(zone);
  }

  @Test
  public void testCountryServiceInit() {
    CountryServiceImpl impl = new CountryServiceImpl();
    impl.init();
    Assert.assertNotNull(impl.getZoneUtil());
  }

}
