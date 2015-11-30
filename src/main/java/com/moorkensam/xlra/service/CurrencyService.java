package com.moorkensam.xlra.service;

import java.math.BigDecimal;
import java.util.List;

import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.error.RateFileException;

public interface CurrencyService {

	void updateCurrencyRate(CurrencyRate currencyRate);

	void createCurrencyRate(CurrencyRate currencyRate);

	List<CurrencyRate> getAllCurrencyRates();

	List<CurrencyRate> getAllChfRates();

	void updateCurrentChfValue(BigDecimal value);

	CurrencyRate getChfRateForCurrentPrice(BigDecimal price)
			throws RateFileException;

	public void deleteCurrencyRate(CurrencyRate toDelete);

}
