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

}
