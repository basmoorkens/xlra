package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.PriceCalculationDao;
import com.moorkensam.xlra.model.offerte.PriceCalculation;

public class PriceCalculationDaoImpl extends BaseDao implements PriceCalculationDao {

  @Override
  public PriceCalculation createCalculation(PriceCalculation calculation) {
    return getEntityManager().merge(calculation);
  }

}
