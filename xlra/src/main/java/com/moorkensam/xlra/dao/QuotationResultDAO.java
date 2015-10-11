package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.rate.QuotationResult;

public interface QuotationResultDAO {

	public void createQuotationResult(QuotationResult result);

	public QuotationResult updateQuotationResult(QuotationResult result);

	public List<QuotationResult> getAllQuotationResults();

}
