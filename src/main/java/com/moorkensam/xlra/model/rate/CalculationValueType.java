package com.moorkensam.xlra.model.rate;

public enum CalculationValueType {
	PERCENTAGE("%"), CONSTANT("EUR");

	String suffix;

	private CalculationValueType(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}
}
