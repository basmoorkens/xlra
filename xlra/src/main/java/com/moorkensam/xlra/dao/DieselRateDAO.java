package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.DieselRate;

public interface DieselRateDAO {

	void updateDieselRate(DieselRate dieselRate);
	
	void createDieselRate(DieselRate dieselRate);
	
	List<DieselRate> getAllDieselRates();
	
}
