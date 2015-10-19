package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.hibernate.validator.util.SetAccessibility;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.QuotationService;

@ManagedBean
@ViewScoped
public class CreateQuotationController {

	@Inject
	private QuotationService quotationService;

	@Inject
	private CustomerService customerService;

	@Inject
	private CountryService countryService;

	private List<BaseCustomer> customers;

	private QuotationQuery quotationQuery;

	private QuotationResult quotationResult;

	private BaseCustomer customerToAdd;

	private boolean renderAddCustomerGrid;

	private List<Country> allCountries;

	private boolean collapseCustomerPanel, collapseFiltersPanel,
			collapseSummaryPanel;

	@PostConstruct
	public void init() {
		allCountries = countryService.getAllCountries();
		initializeNewQuotationQuery();
		refreshCustomers();
		initializeNewCustomer();
		collapseFiltersPanel = true;
		collapseSummaryPanel = true;
	}

	private void initializeNewQuotationQuery() {
		QuotationQuery query = new QuotationQuery();
		for (Country c : allCountries) {
			if (c.getShortName().toLowerCase().equals("be")) {
				query.setCountry(c);
			}
		}
		query.setMeasurement(Measurement.PALET);
		query.setKindOfRate(Kind.NORMAL);
		setQuotationQuery(query);
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
		getQuotationQuery().setCustomer(
				customerService.createCustomer(customerToAdd));
		MessageUtil.addMessage("Customer created", "Created customer "
				+ customerToAdd.getName());
		refreshCustomers();
		renderAddCustomerGrid = false;
		customerToAdd = new BaseCustomer();
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public void procesCustomer() {
		if (getQuotationQuery().getCustomer() instanceof FullCustomer) {
			setupFiltersFromExistingCustomer();
		}
		collapseCustomerPanel = true;
		collapseFiltersPanel = false;
		collapseSummaryPanel = true;
	}

	public void backToCustomer() {
		collapseCustomerPanel = false;
		collapseFiltersPanel = true;
		collapseSummaryPanel = true;
	}

	public void backToRateFilters() {
		collapseCustomerPanel = true;
		collapseFiltersPanel = false;
		collapseSummaryPanel = true;
	}

	public void processRateFilters() {
		try {
			quotationResult = quotationService
					.generateQuotationResultForQuotationQuery(quotationQuery);
			collapseCustomerPanel = true;
			collapseFiltersPanel = true;
			collapseSummaryPanel = false;
		} catch (RateFileException re2) {
			showRateFileError(re2);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof RateFileException) {
				RateFileException re = (RateFileException) e
						.getCausedByException();
				showRateFileError(re);
			} else {
				MessageUtil
						.addErrorMessage("Unknown exception",
								"An unexpected exception occurred, please contact the system admin.");
			}
		}
	}

	public void submitOfferte() {
		quotationService.submitQuotationResult(quotationResult);
	}

	private void showRateFileError(RateFileException re) {
		MessageUtil.addErrorMessage(
				"Unexpected error whilst processing quotation request.",
				re.getBusinessException());
	}

	private void setupFiltersFromExistingCustomer() {
		FullCustomer fc = (FullCustomer) getQuotationQuery().getCustomer();
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

	public QuotationQuery getQuotationQuery() {
		return quotationQuery;
	}

	public void setQuotationQuery(QuotationQuery quotationQuery) {
		this.quotationQuery = quotationQuery;
	}

	public QuotationResult getQuotationResult() {
		return quotationResult;
	}

	public void setQuotationResult(QuotationResult quotationResult) {
		this.quotationResult = quotationResult;
	}

	public boolean isCollapseSummaryPanel() {
		return collapseSummaryPanel;
	}

	public void setCollapseSummaryPanel(boolean collapseSummaryPanel) {
		this.collapseSummaryPanel = collapseSummaryPanel;
	}

	public boolean isCollapseCustomerPanel() {
		return collapseCustomerPanel;
	}

	public void setCollapseCustomerPanel(boolean collapseCustomerPanel) {
		this.collapseCustomerPanel = collapseCustomerPanel;
	}

	public boolean isCollapseFiltersPanel() {
		return collapseFiltersPanel;
	}

	public void setCollapseFiltersPanel(boolean collapseFiltersPanel) {
		this.collapseFiltersPanel = collapseFiltersPanel;
	}

	public List<TransportType> getAllTransportTypes() {
		return Arrays.asList(TransportType.values());
	}

}
