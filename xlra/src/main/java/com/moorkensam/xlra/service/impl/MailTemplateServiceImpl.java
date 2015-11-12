package com.moorkensam.xlra.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.mapper.OfferteEmailParameterGenerator;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.MailTemplateService;

@Stateless
public class MailTemplateServiceImpl implements MailTemplateService {

	@Inject
	private EmailTemplateDAO mailTemplateDAO;

	private OfferteEmailParameterGenerator offerteEmailParameterGenerator;

	@Inject
	private TemplateEngine templateEngine;

	private final static Logger logger = LogManager.getLogger();

	@PostConstruct
	public void init() {
		offerteEmailParameterGenerator = new OfferteEmailParameterGenerator();
	}

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		getMailTemplateDAO().updateEmailTemplate(mailTemplate);
	}

	@Override
	public MailTemplateDTO getAllTemplates() {
		MailTemplateDTO dto = new MailTemplateDTO(getMailTemplateDAO()
				.getAllTemplates());
		return dto;
	}

	@Override
	public void saveMailTemplateDTO(MailTemplateDTO mailTemplateDTO) {
		for (MailTemplate mt : mailTemplateDTO.getMailTemplates()) {
			getMailTemplateDAO().updateEmailTemplate(mt);
		}
	}

	@Override
	public MailTemplate getMailTemplateForLanguage(Language language) {
		return getMailTemplateDAO().getMailTemplateForLanguage(language);
	}

	public void initializeOfferteEmail(QuotationResult result,
			OfferteMailDTO dto, RateFile rf, PriceCalculationDTO priceDTO)
			throws TemplatingException, RateFileException {
		PriceResultDTO resultDTO = new PriceResultDTO();
		getOfferteEmailParameterGenerator().fillInParameters(priceDTO,
				resultDTO, result.getOfferteUniqueIdentifier());
		try {
			MailTemplate template = getMailTemplateDAO()
					.getMailTemplateForLanguage(
							result.getQuery().getResultLanguage());
			Map<String, Object> templateParameters = templateEngine
					.createOfferteEmailTemplateParams(result.getQuery(),
							resultDTO);
			String emailMessage = templateEngine.parseOfferteEmailTemplate(
					template.getTemplate(), templateParameters);
			dto.setAddress(result.getQuery().getCustomer().getEmail());
			dto.setSubject(template.getSubject());
			dto.setContent(emailMessage);
		} catch (NoResultException nre) {
			logger.error("Could not find email template for "
					+ result.getQuery().getResultLanguage());
			throw new RateFileException(
					"Could not find email template for language "
							+ result.getQuery().getResultLanguage());
		}

	}

	public OfferteEmailParameterGenerator getOfferteEmailParameterGenerator() {
		return offerteEmailParameterGenerator;
	}

	public void setOfferteEmailParameterGenerator(
			OfferteEmailParameterGenerator offerteEmailParameterGenerator) {
		this.offerteEmailParameterGenerator = offerteEmailParameterGenerator;
	}

	public EmailTemplateDAO getMailTemplateDAO() {
		return mailTemplateDAO;
	}

	public void setMailTemplateDAO(EmailTemplateDAO mailTemplateDAO) {
		this.mailTemplateDAO = mailTemplateDAO;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

}
