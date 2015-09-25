package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.CurrencyRate;

public interface CurrencyService {

	void updateCurrencyRate(CurrencyRate currencyRate);

	void createCurrencyRate(CurrencyRate currencyRate);

	List<CurrencyRate> getAllCurrencyRates();

	List<CurrencyRate> getAllChfRates();

	void updateCurrentChfValue(double value);
}
