package com.moorkensam.xlra.dto;

import java.math.BigDecimal;

public class PriceCalculationDTO {

	private BigDecimal basePrice;

	private BigDecimal finalPrice;

	private BigDecimal dieselPrice;

	private BigDecimal chfPrice;

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

}
