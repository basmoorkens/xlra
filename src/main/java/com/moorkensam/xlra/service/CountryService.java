package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;

import java.util.List;

public interface CountryService {

  List<Country> getAllCountries();

  List<Country> getAllCountriesFullLoad();

  Country getCountryById(long id);

  public void createZone(Zone zone);

  public void updateZone(Zone zone);

  public Country updateCountry(Country country);

  public void deleteCountry(Country country);

}
