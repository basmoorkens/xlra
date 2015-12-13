package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.offerte.QuotationQuery;

public interface QuotationQueryDao {

  public QuotationQuery createQuotationQuery(QuotationQuery quotation);

  public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

  List<QuotationQuery> getAllQuotationQueries();
}
