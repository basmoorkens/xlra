package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;

@ManagedBean
@ViewScoped
public class CountryController {

	@Inject
	private CountryService countryService;

	private List<Country> countries;

	private Country selectedCountry;

	@PostConstruct
	public void init() {
		countries = countryService.getAllCountriesFullLoad();
	}

	public void onCountryRowEdit(RowEditEvent event) {
		Country c = (Country) event.getObject();
		MessageUtil.addMessage("Country updated", "Country " + c.getShortName()
				+ " was successfully");
		c = countryService.updateCountry(c);
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
