package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;

@Stateless
public class CountryServiceImpl implements CountryService {

	@Inject
	private CountryDAO countryDAO;
	
	@Override
	public List<Country> getAllCountries() {
		return countryDAO.getAllCountries();
	}

	@Override
	public Country getCountryById(long id) {
		return countryDAO.getCountryById(id);
	}

}
