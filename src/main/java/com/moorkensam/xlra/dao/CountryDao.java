package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.rate.Country;

import java.util.List;

public interface CountryDao {

  List<Country> getAllCountries();

  Country getCountryById(long id);

  List<Country> getAllCountriesFullyLoaded();

  Country updateCountry(Country country);

  void deleteCountry(Country country);
}
