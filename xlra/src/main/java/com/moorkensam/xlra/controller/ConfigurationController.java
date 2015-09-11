package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.service.ApplicationConfigurationService;

@ManagedBean
@ViewScoped
public class ConfigurationController {

	@Inject
	private ApplicationConfigurationService applicationConfigurationService;

	private List<DieselRate> dieselRates;

	private DieselRate selectedDieselRate;

	private List<CurrencyRate> chfRates;

	private CurrencyRate selectedChfRate;

	private Configuration configuration;

	private double currentDieselValue, currentChfValue;

	@PostConstruct
	public void initPage() {
		refreshData();
	}

	private void refreshData() {
		refreshDieselRates();
		refreshCurrencyRates();
		setupCurrentRates();
	}

	private void setupCurrentRates() {
		configuration = applicationConfigurationService.getConfiguration();
		currentDieselValue = configuration.getCurrentDieselPrice();
		currentChfValue = configuration.getCurrentChfValue();
	}

	private void refreshCurrencyRates() {
		chfRates = applicationConfigurationService.getAllChfRates();
	}

	private void refreshDieselRates() {
		dieselRates = applicationConfigurationService.getAllDieselRates();
	}

	private void updateDieselRate(DieselRate rate) {
		applicationConfigurationService.updateDieselRate(rate);
	}

	private void updateChfRate(CurrencyRate rate) {
		applicationConfigurationService.updateCurrencyRate(rate);
	}

	public void updateCurrentDieselRate() {
		if (configuration.getCurrentDieselPrice() != getCurrentDieselValue()) {
			applicationConfigurationService
					.updateCurrentDieselValue(getCurrentDieselValue());
			MessageUtil.addMessage("Current diesel price",
					"Updated current diesel price to "
							+ getCurrentDieselValue());
			setupCurrentRates();
		} else {
			MessageUtil.addMessage("Current diesel price",
					"The new price is the same as the old, not updating.");
		}
	}

	public void updateCurrentChfRate() {
		if (configuration.getCurrentChfValue() != getCurrentChfValue()) {
			applicationConfigurationService
					.updateCurrentChfValue(getCurrentChfValue());
			MessageUtil.addMessage("Current swiss franc price",
					"Updated current swiss franc price to "
							+ getCurrentChfValue());
			setupCurrentRates();
		} else {
			MessageUtil.addMessage("Current swiss franc price",
					"The new price is the same as the old, not updating.");
		}
	}

	public void onChfCurrencyRateRowEdit(RowEditEvent event) {
		CurrencyRate newRate = (CurrencyRate) event.getObject();

		updateChfRate(newRate);
		MessageUtil.addMessage("Swiss franc rate updated",
				"Updated swiss franc rate for " + newRate.getInterval()
						+ " to " + newRate.getMultiplier());
		refreshCurrencyRates();
	}

	public void onDieselChargeRowEdit(RowEditEvent event) {
		DieselRate newRate = (DieselRate) event.getObject();

		updateDieselRate(newRate);
		MessageUtil.addMessage("Diesel rate updated",
				"Updated diesel rate for " + newRate.getInterval() + " to "
						+ newRate.getMultiplier());
		refreshDieselRates();
	}

	public List<DieselRate> getDieselRates() {
		return dieselRates;
	}

	public void setDieselRates(List<DieselRate> dieselRates) {
		this.dieselRates = dieselRates;
	}

	public DieselRate getSelectedDieselRate() {
		return selectedDieselRate;
	}

	public void setSelectedDieselRate(DieselRate selectedDieselRate) {
		this.selectedDieselRate = selectedDieselRate;
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

	public double getCurrentDieselValue() {
		return currentDieselValue;
	}

	public void setCurrentDieselValue(double currentDieselValue) {
		this.currentDieselValue = currentDieselValue;
	}
}
