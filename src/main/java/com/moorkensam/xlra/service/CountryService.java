package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;

public interface CountryService {

  List<Country> getAllCountries();

  List<Country> getAllCountriesFullLoad();

  Country getCountryById(long id);

  public void createZone(Zone zone);

  public void updateZone(Zone zone);

  public Country updateCountry(Country country);

}
