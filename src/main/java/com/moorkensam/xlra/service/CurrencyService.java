package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.error.RateFileException;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {

  void updateCurrencyRate(CurrencyRate currencyRate);

  void createCurrencyRate(CurrencyRate currencyRate);

  List<CurrencyRate> getAllCurrencyRates();

  List<CurrencyRate> getAllChfRates();

  void updateCurrentChfValue(BigDecimal value);

  CurrencyRate getChfRateForCurrentPrice(BigDecimal price) throws RateFileException;

  public void deleteCurrencyRate(CurrencyRate toDelete);

}
