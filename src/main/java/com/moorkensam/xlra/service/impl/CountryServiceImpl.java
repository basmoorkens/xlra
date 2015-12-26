package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.CountryDao;
import com.moorkensam.xlra.dao.ZoneDao;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.util.ZoneUtil;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Service to fetch and update countries. This service can also create zones for countries and
 * delete them.
 * 
 * @author bas
 *
 */
@Stateless
public class CountryServiceImpl implements CountryService {

  @Inject
  private CountryDao countryDao;

  @Inject
  private ZoneDao zoneDao;

  private ZoneUtil zoneUtil;

  @PostConstruct
  public void init() {
    setZoneUtil(new ZoneUtil());
  }

  @Override
  public List<Country> getAllCountries() {
    return getCountryDao().getAllCountries();
  }

  @Override
  public Country getCountryById(long id) {
    return getCountryDao().getCountryById(id);
  }

  @Override
  public List<Country> getAllCountriesFullLoad() {
    List<Country> countries = getCountryDao().getAllCountriesFullyLoaded();
    for (Country c : countries) {
      if (c.getNames() == null || c.getNames().isEmpty()) {
        c.buildEmptyLanguageMap();
      }
    }
    return countries;
  }

  @Override
  public void createZone(Zone zone) {
    fillInPersistentPostalCodes(zone);
    getZoneDao().createZone(zone);
  }

  private void fillInPersistentPostalCodes(Zone zone) {
    zone.setAlphaNumericalPostalCodes(getZoneUtil().convertAlphaNumericPostalCodeStringToList(zone
        .getAlphaNumericPostalCodesAsString()));
    zone.setNumericalPostalCodes(getZoneUtil().convertNumericalPostalCodeStringToList(zone
        .getNumericalPostalCodesAsString()));
  }

  @Override
  public void updateZone(Zone zone) {
    fillInPersistentPostalCodes(zone);
    getZoneDao().updateZone(zone);
  }

  @Override
  public Country updateCountry(Country country) {
    return getCountryDao().updateCountry(country);
  }

  public CountryDao getCountryDao() {
    return countryDao;
  }

  public void setCountryDao(CountryDao countryDao) {
    this.countryDao = countryDao;
  }

  public ZoneDao getZoneDao() {
    return zoneDao;
  }

  public void setZoneDao(ZoneDao zoneDao) {
    this.zoneDao = zoneDao;
  }

  @Override
  public void deleteCountry(Country country) {
    countryDao.deleteCountry(country);
  }

  public ZoneUtil getZoneUtil() {
    return zoneUtil;
  }

  public void setZoneUtil(ZoneUtil zoneUtil) {
    this.zoneUtil = zoneUtil;
  }
}
