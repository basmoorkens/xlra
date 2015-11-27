package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.model.rate.Country;

public class CountryDAOImpl extends BaseDAO implements CountryDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> getAllCountries() {
		Query query = getEntityManager().createNamedQuery("Country.findAll");
		return (List<Country>) query.getResultList();
	}

	@Override
	public Country getCountryById(long id) {
		Country c = (Country) getEntityManager().find(Country.class, id);
		lazyLoadCountry(c);
		return c;
	}

	private void lazyLoadCountry(Country c) {
		c.getNames().size();
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
		Country c = getEntityManager().merge(country);
		lazyLoadCountry(c);
		return c;
	}

}
