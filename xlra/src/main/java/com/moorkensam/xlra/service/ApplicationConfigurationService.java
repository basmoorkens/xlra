package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.Configuration;

public interface ApplicationConfigurationService {

	void updateXlraConfiguration(Configuration xlraConfiguration);
	
	Configuration getConfiguration();
	
	void updateDieselRate(DieselRate dieselRate);
	
	void createDieselRate(DieselRate dieselRate);
	
	List<DieselRate> getAllDieselRates();
	
	void updateCurrencyRate(CurrencyRate currencyRate);
	
	void createCurrencyRate(CurrencyRate currencyRate);
	
	List<CurrencyRate> getAllCurrencyRates();
	
	List<MailTemplate> getAllEmailTemplates();
	
	void updateEmailTemplate(MailTemplate mailTemplate);
	
	List<CurrencyRate> getAllChfRates();
	
	void updateCurrentChfValue(double value);
	
	void updateCurrentDieselValue(double value);
}
