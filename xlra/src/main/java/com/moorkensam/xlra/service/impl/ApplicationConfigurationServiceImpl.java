package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.LogRecord;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.util.LogRecordFactory;

//TODO Implement diesel and chf 4 ciphers after.

@Stateless
public class ApplicationConfigurationServiceImpl implements
		ApplicationConfigurationService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private ConfigurationDao xlraConfigurationDAO;

	@Inject
	private EmailTemplateDAO emailTemplateDAO;

	@Override
	public void updateXlraConfiguration(Configuration xlraConfiguration) {
		xlraConfigurationDAO.updateXlraConfiguration(xlraConfiguration);
	}

	@Override
	public Configuration getConfiguration() {
		Configuration config = xlraConfigurationDAO.getXlraConfiguration();
		Collections.sort(config.getTranslations());
		return config;
	}

	@Override
	public List<MailTemplate> getAllEmailTemplates() {
		return emailTemplateDAO.getAllTemplates();
	}

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		emailTemplateDAO.updateEmailTemplate(mailTemplate);
	}

}
