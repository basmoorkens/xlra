package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.hibernate.validator.util.SetAccessibility;
import org.primefaces.event.FlowEvent;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;

@ManagedBean
@ViewScoped
public class CreateQuotationController {

	@Inject
	private CustomerService customerService;

	@Inject
	private CountryService countryService;

	private List<BaseCustomer> customers;

	private QuotationQuery quotation;

	private BaseCustomer customerToAdd;

	private boolean renderAddCustomerGrid;

	private List<Country> allCountries;

	@PostConstruct
	public void init() {
		allCountries = countryService.getAllCountries();
		quotation = new QuotationQuery();
		refreshCustomers();
		initializeNewCustomer();
	}

	private void refreshCustomers() {
		setCustomers(customerService.getAllCustomers());
	}

	private void initializeNewCustomer() {
		customerToAdd = new BaseCustomer();
	}

	public void setupPageForNewCustomer() {
		renderAddCustomerGrid = true;
	}

	public void createCustomer() {
		quotation.setCustomer(customerService.createCustomer(customerToAdd));
		refreshCustomers();
		renderAddCustomerGrid = false;
		customerToAdd = new BaseCustomer();
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().equals("selectFiltersTab")) {
			if (quotation.getCustomer() instanceof FullCustomer) {
				setupFiltersFromExistingCustomer();
			}
		}

		if (event.getNewStep().equals("summaryOfferteTab")) {

		}
		return event.getNewStep();
	}

	private void setupFiltersFromExistingCustomer() {
		FullCustomer fc = (FullCustomer) quotation.getCustomer();
		// TODO implements loading of filters based on customer ratefile present
	}

	public List<BaseCustomer> completeCustomerName(String input) {
		List<BaseCustomer> filteredCustomers = new ArrayList<BaseCustomer>();
		if (customers != null) {
			for (BaseCustomer baseCustomer : customers) {
				if (baseCustomer.getName().toLowerCase()
						.contains(input.toLowerCase())) {
					filteredCustomers.add(baseCustomer);
				}
			}
		}
		return filteredCustomers;
	}

	public List<Measurement> getAllMeasurements() {
		return Arrays.asList(Measurement.values());
	}

	public List<Kind> getAllKinds() {
		return Arrays.asList(Kind.values());
	}

	public QuotationQuery getQuotation() {
		return quotation;
	}

	public void setQuotation(QuotationQuery quotation) {
		this.quotation = quotation;
	}

	public List<BaseCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<BaseCustomer> customers) {
		this.customers = customers;
	}

	public BaseCustomer getCustomerToAdd() {
		return customerToAdd;
	}

	public void setCustomerToAdd(BaseCustomer customerToAdd) {
		this.customerToAdd = customerToAdd;
	}

	public boolean isRenderAddCustomerGrid() {
		return renderAddCustomerGrid;
	}

	public void setRenderAddCustomerGrid(boolean renderAddCustomerGrid) {
		this.renderAddCustomerGrid = renderAddCustomerGrid;
	}

	public List<Country> getAllCountries() {
		return allCountries;
	}

	public void setAllCountries(List<Country> allCountries) {
		this.allCountries = allCountries;
	}

}
