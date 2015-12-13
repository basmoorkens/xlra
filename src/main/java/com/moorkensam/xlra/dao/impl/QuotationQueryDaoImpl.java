package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.QuotationQueryDao;
import com.moorkensam.xlra.model.offerte.QuotationQuery;

public class QuotationQueryDaoImpl extends BaseDao implements QuotationQueryDao {

  @Override
  public QuotationQuery createQuotationQuery(QuotationQuery quotation) {
    return getEntityManager().merge(quotation);
  }

  @Override
  public QuotationQuery updateQuotationQuery(QuotationQuery quotation) {
    return getEntityManager().merge(quotation);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<QuotationQuery> getAllQuotationQueries() {
    Query query = getEntityManager().createNamedQuery("QuotationQuery.findAll");
    return (List<QuotationQuery>) query.getResultList();
  }

}
