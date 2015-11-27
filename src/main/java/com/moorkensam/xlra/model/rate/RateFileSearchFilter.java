package com.moorkensam.xlra.model.rate;

import java.io.Serializable;

import com.moorkensam.xlra.model.customer.Customer;

public class RateFileSearchFilter implements Serializable {

	private static final long serialVersionUID = -6601295471333940331L;

	private Kind rateKind;

	private Measurement measurement;

	private Country country;

	private Customer customer;

	private TransportType transportationType;

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

	@Override
	public String toString() {
		String result = "";
		if (rateKind != null) {
			result += "Ratekind: " + rateKind;
		}
		if (measurement != null) {
			result += " Measurement: " + measurement;
		}
		if (country != null) {
			result += " Country: " + country;
		}
		if (getCustomer() != null) {
			result += " Customer: " + getCustomer().getName();
		}
		if (transportationType != null) {
			result += "TransportationType: " + getTransportationType();
		}
		if (result.equals("")) {
			result = "Empty filter";
		}
		return result;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public TransportType getTransportationType() {
		return transportationType;
	}

	public void setTransportationType(TransportType transportationType) {
		this.transportationType = transportationType;
	}
}
