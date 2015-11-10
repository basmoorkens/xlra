package com.moorkensam.xlra.service;

import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;

public interface MailTemplateService {

	void updateEmailTemplate(MailTemplate mailTemplate);

	MailTemplateDTO getAllTemplates();

	MailTemplate getMailTemplateForLanguage(Language language);

	void saveMailTemplateDTO(MailTemplateDTO mailTemplateDTO);

	public void initializeOfferteEmail(QuotationResult result,
			OfferteMailDTO dto, RateFile rf, PriceCalculationDTO priceDTO)
			throws TemplatingException, RateFileException;

}
