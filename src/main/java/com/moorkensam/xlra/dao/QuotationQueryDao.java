package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.offerte.QuotationQuery;

import java.util.List;

public interface QuotationQueryDao {

  public QuotationQuery createQuotationQuery(QuotationQuery quotation);

  public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

  List<QuotationQuery> getAllQuotationQueries();
}
