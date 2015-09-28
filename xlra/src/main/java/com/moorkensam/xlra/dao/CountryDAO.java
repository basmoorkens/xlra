package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.rate.Country;

public interface CountryDAO {

	List<Country> getAllCountries();
	
	Country getCountryById(long id);
	
	List<Country> getAllCountriesFullyLoaded();
	
}
