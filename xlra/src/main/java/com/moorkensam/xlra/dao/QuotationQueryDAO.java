package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.QuotationQuery;

public interface QuotationQueryDAO {

	public QuotationQuery createQuotationQuery(QuotationQuery quotation);

	public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

	List<QuotationQuery> getAllQuotationQueries();

}
