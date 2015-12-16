package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.CountryDao;
import com.moorkensam.xlra.model.rate.Country;

import java.util.List;

import javax.persistence.Query;

public class CountryDaoImpl extends BaseDao implements CountryDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<Country> getAllCountries() {
    Query query = getEntityManager().createNamedQuery("Country.findAll");
    return (List<Country>) query.getResultList();
  }

  @Override
  public Country getCountryById(long id) {
    Country country = (Country) getEntityManager().find(Country.class, id);
    lazyLoadCountry(country);
    return country;
  }

  private void lazyLoadCountry(Country country) {
    country.getNames().size();
  }

  @Override
  public List<Country> getAllCountriesFullyLoaded() {
    List<Country> countries = getAllCountries();
    for (Country c : countries) {
      lazyLoadCountry(c);
    }
    return countries;
  }

  @Override
  public Country updateCountry(Country country) {
    country = getEntityManager().merge(country);
    lazyLoadCountry(country);
    return country;
  }

}
