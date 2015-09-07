package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CurrencyRateDAO;
import com.moorkensam.xlra.dao.DieselRateDAO;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.XlraConfigurationDao;
import com.moorkensam.xlra.model.CurrencyRate;
import com.moorkensam.xlra.model.DieselRate;
import com.moorkensam.xlra.model.MailTemplate;
import com.moorkensam.xlra.model.XlraConfiguration;
import com.moorkensam.xlra.service.XlraConfigurationService;

//TODO refactor name to configurationservice
@Stateless
public class XlraConfigurationServiceImpl implements XlraConfigurationService {

	@Inject
	private XlraConfigurationDao xlraConfigurationDAO;
	
	@Inject
	private DieselRateDAO dieselRateDAO;
	
	@Inject
	private CurrencyRateDAO currencyRateDAO;
	
	@Inject
	private EmailTemplateDAO emailTemplateDAO;
	
	@Override
	public void updateXlraConfiguration(XlraConfiguration xlraConfiguration) {
		xlraConfigurationDAO.updateXlraConfiguration(xlraConfiguration);
	}

	@Override
	public XlraConfiguration getXlraConfiguration() {
		return xlraConfigurationDAO.getXlraConfiguration();
	}

	@Override
	public void updateDieselRate(DieselRate dieselRate) {
		dieselRateDAO.updateDieselRate(dieselRate);
	}

	@Override
	public void createDieselRate(DieselRate dieselRate) {
		dieselRateDAO.createDieselRate(dieselRate);
	}

	@Override
	public List<DieselRate> getAllDieselRates() {
		return dieselRateDAO.getAllDieselRates();
	}

	@Override
	public void updateCurrencyRate(CurrencyRate currencyRate) {
		currencyRateDAO.updateCurrencyRate(currencyRate);
	}

	@Override
	public void createCurrencyRate(CurrencyRate currencyRate) {
		currencyRateDAO.createCurrencyRate(currencyRate);
	}

	@Override
	public List<CurrencyRate> getAllCurrencyRates() {
		return currencyRateDAO.getAllCurrencyRates();
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
