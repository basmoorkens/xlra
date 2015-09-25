package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.CurrencyService;

@ManagedBean
@ViewScoped
public class ChfController {

	@Inject
	private ApplicationConfigurationService applicationConfigurationService;

	@Inject
	private CurrencyService currencyService;

	private List<CurrencyRate> chfRates;

	private CurrencyRate selectedChfRate;

	private double currentChfValue;

	private Configuration configuration;

	@PostConstruct
	public void initPage() {
		refreshCurrencyRates();
	}

	private void refreshCurrencyRates() {
		chfRates = currencyService.getAllChfRates();
		configuration = applicationConfigurationService.getConfiguration();
		setupCurrentRates();
	}

	public void updateCurrentChfRate() {
		if (configuration.getCurrentChfValue() != getCurrentChfValue()) {
			currencyService.updateCurrentChfValue(getCurrentChfValue());
			MessageUtil.addMessage("Current swiss franc price",
					"Updated current swiss franc price to "
							+ getCurrentChfValue());
			setupCurrentRates();
		} else {
			MessageUtil.addMessage("Current swiss franc price",
					"The new price is the same as the old, not updating.");
		}
	}

	private void setupCurrentRates() {
		currentChfValue = configuration.getCurrentChfValue();
	}

	public void onChfCurrencyRateRowEdit(RowEditEvent event) {
		CurrencyRate newRate = (CurrencyRate) event.getObject();

		updateChfRate(newRate);
		MessageUtil.addMessage("Swiss franc rate updated",
				"Updated swiss franc rate for " + newRate.getInterval()
						+ " to " + newRate.getMultiplier());
		refreshCurrencyRates();
	}

	private void updateChfRate(CurrencyRate rate) {
		currencyService.updateCurrencyRate(rate);
	}

	public List<CurrencyRate> getChfRates() {
		return chfRates;
	}

	public CurrencyRate getSelectedChfRate() {
		return selectedChfRate;
	}

	public void setSelectedChfRate(CurrencyRate selectedChfRate) {
		this.selectedChfRate = selectedChfRate;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public double getCurrentChfValue() {
		return currentChfValue;
	}

	public void setCurrentChfValue(double currentChfValue) {
		this.currentChfValue = currentChfValue;
	}

	public CurrencyService getCurrencyService() {
		return currencyService;
	}

	public void setCurrencyService(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}
}
