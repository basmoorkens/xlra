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

import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.service.QuotationService;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

	@Inject
	private QuotationService quotationService;

	private List<QuotationResult> quotationResults;

	private QuotationQuery selectedQuery;

	@PostConstruct
	public void initialize() {
		quotationResults = quotationService.getAllQuotationResults();
	}

	public QuotationService getQuotationService() {
		return quotationService;
	}

	public void setQuotationService(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	public QuotationQuery getSelectedQuery() {
		return selectedQuery;
	}

	public void setSelectedQuery(QuotationQuery selectedQuery) {
		this.selectedQuery = selectedQuery;
	}

	public List<QuotationResult> getQuotationResults() {
		return quotationResults;
	}

	public void setQuotationResults(List<QuotationResult> quotationResults) {
		this.quotationResults = quotationResults;
	}
}
