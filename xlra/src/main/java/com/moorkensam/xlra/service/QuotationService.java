package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.QuotationResult;

public interface QuotationService {

	public void createQuotationQuery(QuotationQuery quotation);

	public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

	List<QuotationQuery> getAllQuotationQueries();

	public void createQuotationResult(QuotationResult result);

	public QuotationResult updateQuotationResult(QuotationResult result);

	public List<QuotationResult> getAllQuotationResults();

	/**
	 * Generate a QuotationResult for given query. This implies that for the
	 * given query the correct rateline is searched in the database and an email
	 * and pdf is generated for this calculation.
	 * 
	 * @param query
	 * @return
	 */
	public QuotationResult generateQuotationResultForQuotationQuery(
			QuotationQuery query) throws RateFileException;

}
