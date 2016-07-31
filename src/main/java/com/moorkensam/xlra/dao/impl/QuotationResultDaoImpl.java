package com.moorkensam.xlra.dao.impl;


import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.QuotationResultDao;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.util.JpaUtil;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class QuotationResultDaoImpl extends BaseDao implements QuotationResultDao {

  @Override
  public void createQuotationResult(QuotationResult result) {
    getEntityManager().persist(result);
  }

  @Override
  public QuotationResult updateQuotationResult(QuotationResult result) {
    QuotationResult resultMerged = getEntityManager().merge(result);
    fullLoad(resultMerged);
    return resultMerged;
  }

  private void fullLoad(QuotationResult result) {
    result.getQuery();
    result.getEmailResult();
    result.getEmailHistory().size();
    result.getEmailResult().getRecipients().size();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<QuotationResult> getAllQuotationResults() {
    Query query = getEntityManager().createNamedQuery("QuotationResult.findAll");
    return (List<QuotationResult>) query.getResultList();
  }

  @Override
  public List<QuotationResult> getQuotationResults(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, String> filters) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT q FROM QuotationResult q WHERE 1 = 1 ");
    // TODO apply filters
    if (sortField != null && !sortField.isEmpty()) {
      queryBuilder.append("ORDER BY q." + sortField + " ");
      if (sortOrder != null) {
        if (SortOrder.ASCENDING.equals(sortOrder)) {
          queryBuilder.append("ASC");
        } else if (SortOrder.DESCENDING.equals(sortOrder)) {
          queryBuilder.append("DESC");
        }
      }
    }
    Query query = getEntityManager().createQuery(queryBuilder.toString());
    if (first > 0) {
      query.setFirstResult(first);
    }
    if (pageSize > 0) {
      query.setMaxResults(pageSize);
    }

    @SuppressWarnings("unchecked")
    List<QuotationResult> resultList = (List<QuotationResult>) query.getResultList();
    for (QuotationResult q : resultList) {
      fullLoad(q);
    }
    return resultList;
  }

  @Override
  public int getQuotationResultCount(Map<String, String> filters) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT count(q.id) FROM QuotationResult q WHERE 1 = 1 ");
    // TODO apply filters
    Query query = getEntityManager().createQuery(queryBuilder.toString());
    Long result = (Long) query.getSingleResult();
    return new Integer(result + "");
  }

  @Override
  public QuotationResult getQuotationResultById(Long id) {
    QuotationResult result = getEntityManager().find(QuotationResult.class, id);
    fullLoad(result);
    return result;
  }

  @Override
  public QuotationResult getOfferteByKey(String offerteKey) {
    Query query = getEntityManager().createNamedQuery("QuotationResult.findByKey");
    query.setParameter("key", offerteKey);
    QuotationResult result = (QuotationResult) query.getSingleResult();
    fullLoad(result);
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<QuotationResult> getLazyloadedOffertes(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters) {
    String queryString = buildLazyLoadQuery(sortField, sortOrder, filters);
    Query query = getEntityManager().createQuery(queryString);
    JpaUtil.applyPagination(first, pageSize, query);
    JpaUtil.fillInParameters(filters, query);
    return (List<QuotationResult>) query.getResultList();
  }

  private String buildLazyLoadQuery(String sortField, SortOrder sortOrder,
      Map<String, Object> filters) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT q FROM QuotationResult q where 1 = 1 ");
    if (filters != null) {
      for (String key : filters.keySet()) {
        builder.append("AND q." + key + " LIKE :" + key + " ");
      }
    }
    if (StringUtils.isNotBlank(sortField)) {
      builder.append("ORDER BY q." + sortField + " "
          + JpaUtil.convertSortOrderToJpaSortOrder(sortOrder));
    }
    return builder.toString();
  }

  @Override
  public int countOffertes() {
    Query query = getEntityManager().createNamedQuery("QuotationResult.countOffertes");
    return ((Long) query.getSingleResult()).intValue();
  }
}
