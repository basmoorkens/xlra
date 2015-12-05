package com.moorkensam.xlra.service.impl;

import java.util.List;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;

public interface TemplateParseService {

	/**
	 * Parses a freemarker template with a given map of values.
	 * 
	 * @param templateFromDb
	 *            The freemarker template to parse.
	 * @param dataModel
	 *            The values to put in the template.
	 * @return The parsed template as a string.
	 * @throws TemplatingException
	 *             When the template could not be parsed
	 */
	String parseOfferteEmailTemplate(String templateFromDb,
			QuotationResult offerte, String fullDetail,
			String additionalConditions) throws TemplatingException;

	String parseOffertePdf(QuotationResult offerte, Language language)
			throws TemplatingException;

	String parseUserCreatedTemplate(User user) throws TemplatingException;

	String parseHtmlAdditionalConditions(List<OfferteOptionDTO> options,
			Language language) throws TemplatingException;

	String parseHtmlFullDetailCalculation(List<OfferteOptionDTO> options,
			PriceCalculation priceCalculation, Language language)
			throws TemplatingException;

	String parseUserResetPasswordEmail(User user) throws TemplatingException;

}