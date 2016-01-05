package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.EmailTemplateDao;
import com.moorkensam.xlra.dto.MailTemplatesForForLanguages;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.MailTemplateService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

@Stateless
public class MailTemplateServiceImpl implements MailTemplateService {

  @Inject
  private EmailTemplateDao mailTemplateDao;

  private TemplateParseService templateParseService;

  private final static Logger logger = LogManager.getLogger();

  @PostConstruct
  public void init() {
    setTemplateParseService(TemplateParseServiceImpl.getInstance());
    templateParseService = TemplateParseServiceImpl.getInstance();
  }

  @Override
  public void updateEmailTemplate(MailTemplate mailTemplate) {
    getMailTemplateDao().updateEmailTemplate(mailTemplate);
  }

  @Override
  public MailTemplatesForForLanguages getAllTemplates() {
    MailTemplatesForForLanguages dto =
        new MailTemplatesForForLanguages(getMailTemplateDao().getAllTemplates());
    return dto;
  }

  @Override
  public void saveMailTemplateDto(MailTemplatesForForLanguages mailTemplateDto) {
    for (MailTemplate mt : mailTemplateDto.getMailTemplates()) {
      getMailTemplateDao().updateEmailTemplate(mt);
    }
  }

  @Override
  public MailTemplate getMailTemplateForLanguage(Language language) {
    return getMailTemplateDao().getMailTemplateForLanguage(language);
  }

  @Override
  public EmailResult initializeOfferteEmail(QuotationResult result) throws TemplatingException,
      RateFileException {
    EmailResult dto = new EmailResult();
    try {
      MailTemplate template =
          getMailTemplateDao().getMailTemplateForLanguage(result.getQuery().getResultLanguage());
      logger.info("Parsing template: " + template.getTemplate());
      String emailMessage =
          templateParseService.parseOfferteEmailTemplate(template.getTemplate(), result);
      fillInEmailResult(result, dto, template, emailMessage);
    } catch (NoResultException nre) {
      logger.error("Could not find email template for " + result.getQuery().getResultLanguage());
      throw new RateFileException("Could not find email template for language "
          + result.getQuery().getResultLanguage());
    }
    return dto;

  }

  private void fillInEmailResult(QuotationResult result, EmailResult dto, MailTemplate template,
      String emailMessage) {
    dto.addRecipient(result.getQuery().getCustomer().getEmail());
    dto.setSubject(template.getSubject());
    dto.setEmail(emailMessage);
  }

  public EmailTemplateDao getMailTemplateDao() {
    return mailTemplateDao;
  }

  public void setMailTemplateDao(EmailTemplateDao mailTemplateDao) {
    this.mailTemplateDao = mailTemplateDao;
  }

  public TemplateParseService getTemplateParseService() {
    return templateParseService;
  }

  public void setTemplateParseService(TemplateParseService templateParseService) {
    this.templateParseService = templateParseService;
  }

}
