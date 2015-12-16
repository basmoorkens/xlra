package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;

import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface QuotationService {

  public void createQuotationQuery(QuotationQuery quotation);

  public QuotationQuery updateQuotationQuery(QuotationQuery quotation);

  List<QuotationQuery> getAllQuotationQueries();

  public void createQuotationResult(QuotationResult result);

  public QuotationResult updateQuotationResult(QuotationResult result);

  public List<QuotationResult> getAllQuotationResults();

  /**
   * Generate a QuotationResult for given query. This implies that for the given query the correct
   * rateline is searched in the database and an email and pdf is generated for this calculation.
   * This is the first step in a 2 step proces.
   * 
   * @param query The query to use to generatE.
   * @return The generated result.
   */
  public QuotationResult generateQuotationResultForQuotationQuery(QuotationQuery query)
      throws RateFileException;

  /**
   * This is the 2nd step of the generation process.
   * 
   * @param offerte The offerte to generate email and pdf for.
   * @return The resulting offerte with mail and pdf attached.
   * @throws RateFileException Thrown when in error.
   */
  public QuotationResult generateEmailAndPdfForOfferte(QuotationResult offerte)
      throws RateFileException;

  /**
   * This method saves a quotation result and sends out the final email to the customer for the
   * result.
   * 
   * @param result Submit the quotationresult.
   */
  public void submitQuotationResult(QuotationResult result) throws RateFileException;

  public List<QuotationResult> getQuotationQueries(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, String> filters);

  public int getQuotationQueryCount(Map<String, String> filters);

  public QuotationResult getFullOfferteById(Long id);

  public QuotationResult getOfferteByOfferteKey(String offertekey);

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

  /**
   * Counts how many offertes are in the database.
   * 
   * @return the number of offertes.
   */
  public int countOffertes();

}
