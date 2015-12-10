package com.moorkensam.xlra.service.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.MailTemplateService;

@Stateless
public class MailTemplateServiceImpl implements MailTemplateService {

	@Inject
	private EmailTemplateDAO mailTemplateDAO;

	private TemplateParseService templateParseService;

	private final static Logger logger = LogManager.getLogger();

	@PostConstruct
	public void init() {
		setTemplateParseService(TemplateParseServiceImpl.getInstance());
		templateParseService = TemplateParseServiceImpl.getInstance();
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

	@Override
	public EmailResult initializeOfferteEmail(QuotationResult result)
			throws TemplatingException, RateFileException {
		EmailResult dto = new EmailResult();
		try {
			MailTemplate template = getMailTemplateDAO()
					.getMailTemplateForLanguage(
							result.getQuery().getResultLanguage());
			logger.info("Parsing template: " + template.getTemplate());
			String emailMessage = templateParseService
					.parseOfferteEmailTemplate(template.getTemplate(), result);
			dto.setToAddress(result.getQuery().getCustomer().getEmail());
			dto.setSubject(template.getSubject());
			dto.setEmail(emailMessage);
		} catch (NoResultException nre) {
			logger.error("Could not find email template for "
					+ result.getQuery().getResultLanguage());
			throw new RateFileException(
					"Could not find email template for language "
							+ result.getQuery().getResultLanguage());
		}
		return dto;

	}

	public EmailTemplateDAO getMailTemplateDAO() {
		return mailTemplateDAO;
	}

	public void setMailTemplateDAO(EmailTemplateDAO mailTemplateDAO) {
		this.mailTemplateDAO = mailTemplateDAO;
	}

	public TemplateParseService getTemplateParseService() {
		return templateParseService;
	}

	public void setTemplateParseService(
			TemplateParseService templateParseService) {
		this.templateParseService = templateParseService;
	}

}
