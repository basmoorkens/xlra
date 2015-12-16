package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.configuration.DieselRate;

import java.util.List;

public interface DieselRateDao {

  void updateDieselRate(DieselRate dieselRate);

  void createDieselRate(DieselRate dieselRate);

  List<DieselRate> getAllDieselRates();

  void deleteDieselRate(DieselRate rate);

}
