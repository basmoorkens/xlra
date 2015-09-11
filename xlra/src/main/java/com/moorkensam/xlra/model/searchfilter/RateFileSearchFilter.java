package com.moorkensam.xlra.model.searchfilter;

import java.io.Serializable;

import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;

public class RateFileSearchFilter implements Serializable {

	private static final long serialVersionUID = -6601295471333940331L;

	private Kind rateKind; 
	
	private Measurement measurement; 
	
	private Country country;
	
	private FullCustomer customer;

	public Kind getRateKind() {
		return rateKind;
	}

	public void setRateKind(Kind rateKind) {
		this.rateKind = rateKind;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public FullCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(FullCustomer customer) {
		this.customer = customer;
	} 
}
