package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.DieselRate;

public interface DieselService {

	void updateDieselRate(DieselRate dieselRate);

	void createDieselRate(DieselRate dieselRate);

	List<DieselRate> getAllDieselRates();

	void updateCurrentDieselValue(double value);

}
