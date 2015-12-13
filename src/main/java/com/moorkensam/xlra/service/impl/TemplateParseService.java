package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;

public interface TemplateParseService {

  /**
   * Parses a freemarker template with a given map of values.
   * 
   * @param templateFromDb The freemarker template to parse.
   * @param dataModel The values to put in the template.
   * @return The parsed template as a string.
   * @throws TemplatingException When the template could not be parsed
   */
  String parseOfferteEmailTemplate(String templateFromDb, QuotationResult offerte)
      throws TemplatingException;

  String parseOffertePdf(QuotationResult offerte, Language language) throws TemplatingException;

  String parseUserCreatedTemplate(User user) throws TemplatingException;

  String parseUserResetPasswordEmail(User user) throws TemplatingException;

}
