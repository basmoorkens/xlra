package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.rate.Country;

public interface CountryService {

	List<Country> getAllCountries();
	
	Country getCountryById(long id);
}
