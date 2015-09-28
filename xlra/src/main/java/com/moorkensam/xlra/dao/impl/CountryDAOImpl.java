package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CountryDAO;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;

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
		fillUpZones(c);
		return c;
	}

	private void fillUpZones(Country c) {
		c.getZones().size();
		for (Zone z : c.getZones()) {
			z.convertAlphaNumericPostalCodeListToString();
		}
	}

	@Override
	public List<Country> getAllCountriesFullyLoaded() {
		List<Country> countries = getAllCountries();
		for (Country c : countries) {
			fillUpZones(c);
		}
		return countries;
	}

}
