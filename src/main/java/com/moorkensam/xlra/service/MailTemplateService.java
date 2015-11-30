package com.moorkensam.xlra.service;

import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;

public interface MailTemplateService {

	void updateEmailTemplate(MailTemplate mailTemplate);

	MailTemplateDTO getAllTemplates();

	MailTemplate getMailTemplateForLanguage(Language language);

	void saveMailTemplateDTO(MailTemplateDTO mailTemplateDTO);

	public OfferteMailDTO initializeOfferteEmail(QuotationResult result)
			throws TemplatingException, RateFileException;

}
