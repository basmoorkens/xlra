package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.service.QuotationService;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

	@Inject
	private QuotationService quotationService;

	private List<QuotationResult> offertes;

	@PostConstruct
	public void initialize() {

	}

	public QuotationService getQuotationService() {
		return quotationService;
	}

	public void setQuotationService(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	public List<QuotationResult> getOffertes() {
		return offertes;
	}

	public void setOffertes(List<QuotationResult> offertes) {
		this.offertes = offertes;
	}

}
