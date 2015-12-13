package com.moorkensam.xlra.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.QuotationResultDao;
import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.QuotationResult;

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

  @SuppressWarnings("unchecked")
  @Override
  public List<QuotationResult> getQuotationResultsForFilter(OfferteSearchFilter filter) {
    StringBuilder queryBuilder = new StringBuilder();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    queryBuilder.append("SELECT q FROM QuotationResult q WHERE 1 = 1 ");
    if (!StringUtils.isEmpty(filter.getOfferteKey())) {
      queryBuilder.append("AND q.offerteUniqueIdentifier = :offerteKey ");
      parameterMap.put("offerteKey", filter.getOfferteKey());
    }
    if (!StringUtils.isEmpty(filter.getCustomerName())) {
      queryBuilder.append("AND q.query.customer.name LIKE :customerName ");
      parameterMap.put("customerName", "%" + filter.getCustomerName() + "%");
    }
    if (!StringUtils.isEmpty(filter.getPostalCode())) {
      queryBuilder.append("AND q.query.postalCode = :postalCode ");
      parameterMap.put("postalCode", filter.getPostalCode());
    }
    if (filter.getCountry() != null) {
      queryBuilder.append("AND q.query.country = :country ");
      parameterMap.put("country", filter.getCountry());
    }
    if (filter.getStartDate() != null) {
      queryBuilder.append("AND q.quotationDate = :quotationDate");
      parameterMap.put("quotationDate", filter.getStartDate());
    }

    Query query = getEntityManager().createQuery(queryBuilder.toString());
    fillQueryFromParameterMap(query, parameterMap);
    return (List<QuotationResult>) query.getResultList();

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
}
