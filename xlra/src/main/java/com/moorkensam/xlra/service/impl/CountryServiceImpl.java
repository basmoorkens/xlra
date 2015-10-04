package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.dao.ZoneDAO;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.service.CountryService;

@Stateless
public class CountryServiceImpl implements CountryService {

	@Inject
	private CountryDAO countryDAO;

	@Inject
	private ZoneDAO zoneDAO;

	@Override
	public List<Country> getAllCountries() {
		return countryDAO.getAllCountries();
	}

	@Override
	public Country getCountryById(long id) {
		return countryDAO.getCountryById(id);
	}

	@Override
	public List<Country> getAllCountriesFullLoad() {
		return countryDAO.getAllCountriesFullyLoaded();
	}

	@Override
	public void createZone(Zone zone) {
		fillInPersistentPostalCodes(zone);
		zoneDAO.createZone(zone);
	}

	private void fillInPersistentPostalCodes(Zone zone) {
		zone.convertAlphaNumericPostalCodeStringToList();
		zone.convertNumericalPostalCodeStringToList();
	}

	@Override
	public void updateZone(Zone zone) {
		fillInPersistentPostalCodes(zone);
		zoneDAO.updateZone(zone);
	}

}
