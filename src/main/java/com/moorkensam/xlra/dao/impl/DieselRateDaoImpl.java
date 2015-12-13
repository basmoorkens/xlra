package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.DieselRateDao;
import com.moorkensam.xlra.model.configuration.DieselRate;

public class DieselRateDaoImpl extends BaseDao implements DieselRateDao {

  @Override
  public void updateDieselRate(DieselRate dieselRate) {
    getEntityManager().merge(dieselRate);

  }

  @Override
  public void createDieselRate(DieselRate dieselRate) {
    getEntityManager().persist(dieselRate);

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<DieselRate> getAllDieselRates() {
    Query query = getEntityManager().createNamedQuery("DieselRate.findAll");
    return (List<DieselRate>) query.getResultList();
  }

  @Override
  public void deleteDieselRate(DieselRate rate) {
    rate = getEntityManager().merge(rate);
    getEntityManager().remove(rate);
  }

}
