package com.moorkensam.xlra.dao;

import java.math.BigDecimal;
import java.util.List;

import com.moorkensam.xlra.model.configuration.DieselRate;

public interface DieselRateDAO {

	void updateDieselRate(DieselRate dieselRate);

	void createDieselRate(DieselRate dieselRate);

	List<DieselRate> getAllDieselRates();

}
