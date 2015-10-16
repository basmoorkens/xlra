package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;

public interface CalculationService {

	public void calculatePriceAccordingToConditions(
			PriceCalculationDTO priceDTO, Country country,
			List<Condition> conditions, QuotationQuery query)
			throws RateFileException;

}
