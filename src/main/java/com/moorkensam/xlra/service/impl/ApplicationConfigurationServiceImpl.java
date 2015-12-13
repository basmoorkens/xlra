package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.EmailTemplateDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.service.ApplicationConfigurationService;

// TODO Implement diesel and chf 4 ciphers after.
/**
 * This service is used to get email templates to the front end and the main application
 * cofiguration.
 * 
 * @author bas
 *
 */
@Stateless
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService {

  private final static Logger logger = LogManager.getLogger();

  @Inject
  private ConfigurationDao xlraConfigurationDao;

  @Inject
  private EmailTemplateDao emailTemplateDao;

  @Override
  public void updateXlraConfiguration(Configuration xlraConfiguration) {
    getXlraConfigurationDao().updateXlraConfiguration(xlraConfiguration);
  }

  @Override
  public Configuration getConfiguration() {
    Configuration config = getXlraConfigurationDao().getXlraConfiguration();
    return config;
  }

  @Override
  public List<MailTemplate> getAllEmailTemplates() {
    return getEmailTemplateDao().getAllTemplates();
  }

  @Override
  public void updateEmailTemplate(MailTemplate mailTemplate) {
    if (logger.isDebugEnabled()) {
      logger.debug("Updating email template: " + mailTemplate.getSubject()
          + mailTemplate.getLanguage());
    }
    getEmailTemplateDao().updateEmailTemplate(mailTemplate);
  }

  public ConfigurationDao getXlraConfigurationDao() {
    return xlraConfigurationDao;
  }

  public void setXlraConfigurationDao(ConfigurationDao xlraConfigurationDao) {
    this.xlraConfigurationDao = xlraConfigurationDao;
  }

  public EmailTemplateDao getEmailTemplateDao() {
    return emailTemplateDao;
  }

  public void setEmailTemplateDao(EmailTemplateDao emailTemplateDao) {
    this.emailTemplateDao = emailTemplateDao;
  }

}
