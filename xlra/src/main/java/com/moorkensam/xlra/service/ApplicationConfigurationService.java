package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.CurrencyRate;
import com.moorkensam.xlra.model.DieselRate;
import com.moorkensam.xlra.model.MailTemplate;
import com.moorkensam.xlra.model.XlraConfiguration;

public interface ApplicationConfigurationService {

	void updateXlraConfiguration(XlraConfiguration xlraConfiguration);
	
	XlraConfiguration getXlraConfiguration();
	
	void updateDieselRate(DieselRate dieselRate);
	
	void createDieselRate(DieselRate dieselRate);
	
	List<DieselRate> getAllDieselRates();
	
	void updateCurrencyRate(CurrencyRate currencyRate);
	
	void createCurrencyRate(CurrencyRate currencyRate);
	
	List<CurrencyRate> getAllCurrencyRates();
	
	List<MailTemplate> getAllEmailTemplates();
	
	void updateEmailTemplate(MailTemplate mailTemplate);
	
}
