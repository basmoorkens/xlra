package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.PriceCalculationDAO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;

public class PriceCalculationDAOImpl extends BaseDAO implements
		PriceCalculationDAO {

	@Override
	public PriceCalculation createCalculation(PriceCalculation calculation) {
		return getEntityManager().merge(calculation);
	}

}
