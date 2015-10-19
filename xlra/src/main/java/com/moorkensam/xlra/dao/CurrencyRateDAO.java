package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.configuration.CurrencyRate;

public interface CurrencyRateDAO {

	void deleteCurrencyRate(CurrencyRate currencyRate);

	void createCurrencyRate(CurrencyRate currency);

	void updateCurrencyRate(CurrencyRate currency);

	List<CurrencyRate> getAllCurrencyRates();

	List<CurrencyRate> getAllChfRates();
}
