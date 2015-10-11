package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.rate.QuotationResult;

public interface QuotationService {

	public void createQuotationQuery(QuotationQuery quotation);

	public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

	List<QuotationQuery> getAllQuotationQueries();

	public void createQuotationResult(QuotationResult result);

	public QuotationResult updateQuotationResult(QuotationResult result);

	public List<QuotationResult> getAllQuotationResults();

	public QuotationResult generateQuotationResultForQuotationQuery(
			QuotationQuery query);

}
