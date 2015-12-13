package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.configuration.DieselRate;

public interface DieselRateDao {

  void updateDieselRate(DieselRate dieselRate);

  void createDieselRate(DieselRate dieselRate);

  List<DieselRate> getAllDieselRates();

  void deleteDieselRate(DieselRate rate);

}
