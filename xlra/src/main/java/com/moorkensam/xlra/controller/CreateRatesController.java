package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;

import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.RateFileService;

@ViewScoped
@ManagedBean
public class CreateRatesController {

	@Inject
	private RateFileService rateFileService;

	@Inject
	private CountryService countryService;

	@Inject
	private CustomerService customerService;

	private List<Country> countries;

	private List<Measurement> measurements;

	private List<Kind> kindOfRates;

	private RateFile rateFile;

	private List<FullCustomer> fullCustomers;

	@PostConstruct
	public void init() {
		setRateFile(new RateFile());
		countries = countryService.getAllCountries();
		fullCustomers = customerService.getAllFullCustomers();
		measurements = Arrays.asList(Measurement.values());
		kindOfRates = Arrays.asList(Kind.values());
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public RateFileService getRateFileService() {
		return rateFileService;
	}

	public void setRateFileService(RateFileService rateFileService) {
		this.rateFileService = rateFileService;
	}

	public RateFile getRateFile() {
		return rateFile;
	}

	public void setRateFile(RateFile rateFile) {
		this.rateFile = rateFile;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public List<Kind> getKindOfRates() {
		return kindOfRates;
	}

	public void setKindOfRates(List<Kind> kindOfRates) {
		this.kindOfRates = kindOfRates;
	}

	public List<FullCustomer> completeCustomerName(String input) {
		List<FullCustomer> filteredCustomers = new ArrayList<FullCustomer>();

		for (FullCustomer fc : fullCustomers) {
			if (fc.getName().toLowerCase().contains(input.toLowerCase())) {
				filteredCustomers.add(fc);
			}
		}
		return filteredCustomers;
	}

	public List<FullCustomer> getFullCustomers() {
		return fullCustomers;
	}

	public void setFullCustomers(List<FullCustomer> fullCustomers) {
		this.fullCustomers = fullCustomers;
	}
}
