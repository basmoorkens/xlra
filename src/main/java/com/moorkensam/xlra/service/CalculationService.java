package com.moorkensam.xlra.service;


import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationResult;

public interface CalculationService {

	public PriceCalculation calculatePriceAccordingToConditions(
			QuotationResult result) throws RateFileException;

}
