package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.configuration.CurrencyRate;

import java.util.List;

public interface CurrencyRateDao {

  void deleteCurrencyRate(CurrencyRate currencyRate);

  void createCurrencyRate(CurrencyRate currency);

  void updateCurrencyRate(CurrencyRate currency);

  List<CurrencyRate> getAllCurrencyRates();

  List<CurrencyRate> getAllChfRates();
}
