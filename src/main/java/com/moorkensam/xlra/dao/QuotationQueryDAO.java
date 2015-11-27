package com.moorkensam.xlra.dao;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.offerte.QuotationQuery;

public interface QuotationQueryDAO {

	public QuotationQuery createQuotationQuery(QuotationQuery quotation);

	public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

	List<QuotationQuery> getAllQuotationQueries();
}
