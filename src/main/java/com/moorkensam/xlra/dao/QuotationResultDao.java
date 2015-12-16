package com.moorkensam.xlra.dao;


import com.moorkensam.xlra.model.offerte.QuotationResult;

import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface QuotationResultDao {

  public List<QuotationResult> getQuotationResults(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, String> filters);

  public int getQuotationResultCount(Map<String, String> filters);

  public void createQuotationResult(QuotationResult result);

  public QuotationResult updateQuotationResult(QuotationResult result);

  public List<QuotationResult> getAllQuotationResults();

  public QuotationResult getQuotationResultById(Long id);

  public QuotationResult getOfferteByKey(String offerteKey);

  /**
   * This method loads the offertes in pages.
   * 
   * @param first The first offerte to take.
   * @param pageSize The size of offertes to take.
   * @param sortField The sortfield.
   * @param sortOrder The sortorder;
   * @param filters The filters to apply.
   * @return The page of offertes according to sorting and filtering.
   */
  public List<QuotationResult> getLazyloadedOffertes(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters);

  public int countOffertes();

}
