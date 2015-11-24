package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;

public interface CalculationService {

	public void calculatePriceAccordingToConditions(
			PriceCalculation priceDTO, Country country,
			List<Condition> conditions, QuotationQuery query)
			throws RateFileException;

}
