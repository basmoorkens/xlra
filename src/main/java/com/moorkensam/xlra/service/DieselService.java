package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;

import java.math.BigDecimal;
import java.util.List;

public interface DieselService {

  void updateDieselRate(DieselRate dieselRate);

  void createDieselRate(DieselRate dieselRate);

  List<DieselRate> getAllDieselRates();

  void updateCurrentDieselValue(BigDecimal value);

  DieselRate getDieselRateForCurrentPrice(BigDecimal price) throws RateFileException;

  void deleteDieselRate(DieselRate rate);
}
