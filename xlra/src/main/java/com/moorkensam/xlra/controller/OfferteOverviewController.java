package com.moorkensam.xlra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.QuotationService;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

	@Inject
	private QuotationService quotationService;

	@Inject
	private CountryService countryService;

	private List<QuotationResult> quotationResults;

	private QuotationResult selectedOfferte;

	private List<Country> allCountrys;

	private OfferteSearchFilter searchFilter;

	private boolean detail;

	@PostConstruct
	public void initialize() {
		quotationResults = quotationService.getAllQuotationResults();
		allCountrys = countryService.getAllCountriesFullLoad();
		searchFilter = new OfferteSearchFilter();
	}

	public void setupOfferteDetail(QuotationResult quotationResult) {
		selectedOfferte = quotationResult;
		detail = true;
	}

	public void search() {
		quotationResults = quotationService
				.getQuotationResultsForFilters(searchFilter);
	}

	public QuotationService getQuotationService() {
		return quotationService;
	}

	public void setQuotationService(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	public List<QuotationResult> getQuotationResults() {
		return quotationResults;
	}

	public void setQuotationResults(List<QuotationResult> quotationResults) {
		this.quotationResults = quotationResults;
	}

	public OfferteSearchFilter getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(OfferteSearchFilter searchFilter) {
		this.searchFilter = searchFilter;
	}

	public List<Country> getAllCountrys() {
		return allCountrys;
	}

	public void setAllCountrys(List<Country> allCountrys) {
		this.allCountrys = allCountrys;
	}

	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	public QuotationResult getSelectedOfferte() {
		return selectedOfferte;
	}

	public void setSelectedOfferte(QuotationResult selectedOfferte) {
		this.selectedOfferte = selectedOfferte;
	}
}
