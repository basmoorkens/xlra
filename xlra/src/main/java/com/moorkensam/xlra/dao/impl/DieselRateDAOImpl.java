package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.model.configuration.DieselRate;

public class DieselRateDAOImpl extends BaseDAO implements DieselRateDAO {

	@Override
	public void updateDieselRate(DieselRate dieselRate) {
		getEntityManager().merge(dieselRate);

	}

	@Override
	public void createDieselRate(DieselRate dieselRate) {
		getEntityManager().persist(dieselRate);

	}

	@Override
	public List<DieselRate> getAllDieselRates() {
		Query query = getEntityManager().createNamedQuery("DieselRate.findAll");
		return (List<DieselRate>) query.getResultList();
	}

}
