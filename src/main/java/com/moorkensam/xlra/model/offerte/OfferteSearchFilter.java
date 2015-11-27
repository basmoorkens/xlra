package com.moorkensam.xlra.model.offerte;

import java.util.Date;

import com.moorkensam.xlra.model.rate.Country;

public class OfferteSearchFilter {

	private String offerteKey;

	private Country country;

	private String postalCode;

	private String customerName;

	private Date startDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getOfferteKey() {
		return offerteKey;
	}

	public void setOfferteKey(String offerteKey) {
		this.offerteKey = offerteKey;
	}

}
