package com.moorkensam.xlra.dto;

import java.math.BigDecimal;
import java.util.List;

import com.moorkensam.xlra.model.configuration.TranslationKey;

public class PriceCalculationDTO {

	private BigDecimal basePrice;

	private BigDecimal finalPrice;

	private BigDecimal dieselPrice;

	private BigDecimal chfPrice;

	private BigDecimal calculatedAdrSurcharge;

	private BigDecimal resultingPriceSurcharge;

	private BigDecimal adrSurchargeMinimum;

	private BigDecimal importFormalities;

	private BigDecimal exportFormalities;

	private List<TranslationKey> appliedOperations;

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getDieselPrice() {
		return dieselPrice;
	}

	public void setDieselPrice(BigDecimal dieselPrice) {
		this.dieselPrice = dieselPrice;
	}

	public BigDecimal getChfPrice() {
		return chfPrice;
	}

	public void setChfPrice(BigDecimal chfPrice) {
		this.chfPrice = chfPrice;
	}

	public BigDecimal getCalculatedAdrSurcharge() {
		return calculatedAdrSurcharge;
	}

	public void setCalculatedAdrSurcharge(BigDecimal calculatedAdrSurcharge) {
		this.calculatedAdrSurcharge = calculatedAdrSurcharge;
	}

	public BigDecimal getAdrSurchargeMinimum() {
		return adrSurchargeMinimum;
	}

	public void setAdrSurchargeMinimum(BigDecimal adrSurchargeMinimum) {
		this.adrSurchargeMinimum = adrSurchargeMinimum;
	}

	public BigDecimal getResultingPriceSurcharge() {
		return resultingPriceSurcharge;
	}

	public void setResultingPriceSurcharge(BigDecimal resultingPriceSurcharge) {
		this.resultingPriceSurcharge = resultingPriceSurcharge;
	}

	public BigDecimal getExportFormalities() {
		return exportFormalities;
	}

	public void setExportFormalities(BigDecimal exportFormalities) {
		this.exportFormalities = exportFormalities;
	}

	public BigDecimal getImportFormalities() {
		return importFormalities;
	}

	public void setImportFormalities(BigDecimal importFormalities) {
		this.importFormalities = importFormalities;
	}

	public void addToFinalPrice(BigDecimal toAdd) {
		if (finalPrice == null) {
			finalPrice = new BigDecimal(0d);
		}
		finalPrice = new BigDecimal(finalPrice.doubleValue()
				+ toAdd.doubleValue());
	}

	public List<TranslationKey> getAppliedOperations() {
		return appliedOperations;
	}

	public void setAppliedOperations(List<TranslationKey> appliedOperations) {
		this.appliedOperations = appliedOperations;
	}

}
