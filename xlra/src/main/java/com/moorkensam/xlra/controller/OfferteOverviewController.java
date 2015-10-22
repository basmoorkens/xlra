package com.moorkensam.xlra.controller;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.service.QuotationService;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

	@Inject
	private QuotationService quotationService;

	private LazyDataModel<QuotationResult> lazyModel;
	
	
	
	@PostConstruct
	public void initialize() {

	}

	public QuotationService getQuotationService() {
		return quotationService;
	}

	public void setQuotationService(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	public LazyDataModel<QuotationResult> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<QuotationResult> lazyModel) {
		this.lazyModel = lazyModel;
	}
}
