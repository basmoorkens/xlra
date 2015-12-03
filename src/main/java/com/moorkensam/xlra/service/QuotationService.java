package com.moorkensam.xlra.service;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;

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
	 * and pdf is generated for this calculation. This is the first step in a 2
	 * step proces.
	 * 
	 * @param query
	 * @return
	 */
	public QuotationResult generateQuotationResultForQuotationQuery(
			QuotationQuery query) throws RateFileException;

	/**
	 * This is the 2nd step of the generation process.
	 * 
	 * @param offerte
	 * @return
	 * @throws RateFileException
	 */
	public QuotationResult generateEmailAndPdfForOfferte(QuotationResult offerte)
			throws RateFileException;

	/**
	 * This method saves a quotation result and sends out the final email to the
	 * customer for the result.
	 * 
	 * @param result
	 */
	public void submitQuotationResult(QuotationResult result)
			throws RateFileException;

	public List<QuotationResult> getQuotationQueries(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters);

	public int getQuotationQueryCount(Map<String, String> filters);

	public List<QuotationResult> getQuotationResultsForFilters(
			OfferteSearchFilter filter);

	public QuotationResult getFullOfferteById(Long id);

}
