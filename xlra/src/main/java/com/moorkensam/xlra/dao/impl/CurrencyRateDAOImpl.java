package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.model.configuration.CurrencyRate;

public class CurrencyRateDAOImpl extends BaseDAO implements CurrencyRateDAO {

	@Override
	public void createCurrencyRate(CurrencyRate currency) {
		getEntityManager().persist(currency);
	}

	@Override
	public void updateCurrencyRate(CurrencyRate currency) {
		getEntityManager().merge(currency);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyRate> getAllCurrencyRates() {
		Query query = getEntityManager().createNamedQuery(
				"CurrencyRate.findAll");
		return (List<CurrencyRate>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyRate> getAllChfRates() {
		Query query = getEntityManager().createNamedQuery(
				"CurrencyRate.findAllChf");
		return (List<CurrencyRate>) query.getResultList();
	}

	@Override
	public void deleteCurrencyRate(CurrencyRate currencyRate) {
		currencyRate = getEntityManager().merge(currencyRate);
		getEntityManager().remove(currencyRate);
	}

}
