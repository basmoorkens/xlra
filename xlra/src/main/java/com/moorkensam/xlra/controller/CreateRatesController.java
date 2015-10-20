package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.controller.util.RateUtil;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.IncoTermType;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
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

	private RateFileSearchFilter filter;

	private boolean hasRateFileSelected = false;

	private boolean collapseRateLinesDetailGrid = false;

	private boolean collapseConditionsDetailGrid = false;

	@PostConstruct
	public void init() {
		filter = new RateFileSearchFilter();
		setRateFile(new RateFile());
		countries = countryService.getAllCountries();
		fullCustomers = customerService.getAllFullCustomers();
		measurements = Arrays.asList(Measurement.values());
		kindOfRates = Arrays.asList(Kind.values());
	}

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().equals("rateLineEditor")) {
			RateFile copiedFile = rateFileService
					.getCopyOfRateFileForFilter(filter);
			copiedFile.setCustomer(rateFile.getCustomer());
			copiedFile.setName(rateFile.getName());
			rateFile = copiedFile;
		}
		return event.getNewStep();
	}

	public void onRateLineCellEdit(CellEditEvent event) {
		RateUtil.onRateLineCellEdit(event);
	}

	public void onConditionCellEdit(CellEditEvent event) {
		RateUtil.onConditionCellEdit(event);
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

	public String saveNewRateFile() {
		rateFileService.createRateFile(rateFile);
		MessageUtil.addMessage("Rates created",
				"Succesfully created rates for " + rateFile.getName());
		return "views/rate/admin/manageRates.xhtml";
	}

	public List<Language> getLanguages() {
		return RateUtil.getLanguages();
	}

	public List<FullCustomer> getFullCustomers() {
		return fullCustomers;
	}

	public void setFullCustomers(List<FullCustomer> fullCustomers) {
		this.fullCustomers = fullCustomers;
	}

	public RateFileSearchFilter getFilter() {
		return filter;
	}

	public void setFilter(RateFileSearchFilter filter) {
		this.filter = filter;
	}

	public boolean isHasRateFileSelected() {
		return hasRateFileSelected;
	}

	public void setHasRateFileSelected(boolean hasRateFileSelected) {
		this.hasRateFileSelected = hasRateFileSelected;
	}

	public boolean isCollapseRateLinesDetailGrid() {
		return collapseRateLinesDetailGrid;
	}

	public void setCollapseRateLinesDetailGrid(
			boolean collapseRateLinesDetailGrid) {
		this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
	}

	public boolean isCollapseConditionsDetailGrid() {
		return collapseConditionsDetailGrid;
	}

	public void setCollapseConditionsDetailGrid(
			boolean collapseConditionsDetailGrid) {
		this.collapseConditionsDetailGrid = collapseConditionsDetailGrid;
	}

	public List<IncoTermType> getIncoTermTypes() {
		return RateUtil.getIncoTermTypes();
	}
}
