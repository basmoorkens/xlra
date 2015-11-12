package com.moorkensam.xlra.dao;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.rate.QuotationResult;

public interface QuotationResultDAO {

	public List<QuotationResult> getQuotationResults(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters);

	public void createQuotationResult(QuotationResult result);

	public QuotationResult updateQuotationResult(QuotationResult result);

	public List<QuotationResult> getAllQuotationResults();

}
