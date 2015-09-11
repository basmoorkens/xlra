package com.moorkensam.xlra.model.configuration;

public enum XLRACurrency {

	CHF("Swiss franc");
	
	private String displayName;
	
	XLRACurrency(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
