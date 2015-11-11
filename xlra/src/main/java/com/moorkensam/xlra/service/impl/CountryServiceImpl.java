package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.dao.ZoneDAO;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.CountryService;

/**
 * Service to fetch and update countries. This service can also create zones for
 * countries and delete them.
 * 
 * @author bas
 *
 */
@Stateless
public class CountryServiceImpl implements CountryService {

	@Inject
	private CountryDAO countryDAO;

	@Inject
	private ZoneDAO zoneDAO;

	@Override
	public List<Country> getAllCountries() {
		return getCountryDAO().getAllCountries();
	}

	@Override
	public Country getCountryById(long id) {
		return getCountryDAO().getCountryById(id);
	}

	@Override
	public List<Country> getAllCountriesFullLoad() {
		List<Country> countries = getCountryDAO().getAllCountriesFullyLoaded();
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
		getZoneDAO().createZone(zone);
	}

	private void fillInPersistentPostalCodes(Zone zone) {
		zone.convertAlphaNumericPostalCodeStringToList();
		zone.convertNumericalPostalCodeStringToList();
	}

	@Override
	public void updateZone(Zone zone) {
		fillInPersistentPostalCodes(zone);
		getZoneDAO().updateZone(zone);
	}

	@Override
	public Country updateCountry(Country country) {
		return getCountryDAO().updateCountry(country);
	}

	public CountryDAO getCountryDAO() {
		return countryDAO;
	}

	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	public ZoneDAO getZoneDAO() {
		return zoneDAO;
	}

	public void setZoneDAO(ZoneDAO zoneDAO) {
		this.zoneDAO = zoneDAO;
	}

}
