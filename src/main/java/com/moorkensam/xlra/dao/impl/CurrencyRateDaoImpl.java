package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.CurrencyRateDao;
import com.moorkensam.xlra.model.configuration.CurrencyRate;

import java.util.List;

import javax.persistence.Query;

public class CurrencyRateDaoImpl extends BaseDao implements CurrencyRateDao {

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
    Query query = getEntityManager().createNamedQuery("CurrencyRate.findAll");
    return (List<CurrencyRate>) query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CurrencyRate> getAllChfRates() {
    Query query = getEntityManager().createNamedQuery("CurrencyRate.findAllChf");
    return (List<CurrencyRate>) query.getResultList();
  }

  @Override
  public void deleteCurrencyRate(CurrencyRate currencyRate) {
    currencyRate = getEntityManager().merge(currencyRate);
    getEntityManager().remove(currencyRate);
  }

}
