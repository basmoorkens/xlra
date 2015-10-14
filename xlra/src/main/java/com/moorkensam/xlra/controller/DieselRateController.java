package com.moorkensam.xlra.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.DieselService;

@ManagedBean
@ViewScoped
public class DieselRateController {

	@Inject
	private ApplicationConfigurationService applicationConfigurationService;

	@Inject
	private DieselService dieselService;

	private List<DieselRate> dieselRates;

	private DieselRate selectedDieselRate;

	private Configuration configuration;

	private BigDecimal currentDieselValue;

	@PostConstruct
	public void initPage() {
		refreshDieselRates();
		configuration = getApplicationConfigurationService().getConfiguration();
		setupCurrentRates();
	}

	private void setupCurrentRates() {
		currentDieselValue = configuration.getCurrentDieselPrice();
	}

	private void refreshDieselRates() {
		dieselRates = dieselService.getAllDieselRates();
	}

	private void updateDieselRate(DieselRate rate) {
		dieselService.updateDieselRate(rate);
	}

	public void updateCurrentDieselRate() {
		if (configuration.getCurrentDieselPrice() != getCurrentDieselValue()) {
			dieselService.updateCurrentDieselValue(getCurrentDieselValue());
			MessageUtil.addMessage("Current diesel price",
					"Updated current diesel price to "
							+ getCurrentDieselValue());
			setupCurrentRates();
		} else {
			MessageUtil.addMessage("Current diesel price",
					"The new price is the same as the old, not updating.");
		}
	}

	public void onDieselChargeRowEdit(RowEditEvent event) {
		DieselRate newRate = (DieselRate) event.getObject();

		updateDieselRate(newRate);
		MessageUtil.addMessage("Diesel rate updated",
				"Updated diesel rate for " + newRate.getInterval() + " to "
						+ newRate.getSurchargePercentage());
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

	public BigDecimal getCurrentDieselValue() {
		return currentDieselValue;
	}

	public void setCurrentDieselValue(BigDecimal currentDieselValue) {
		this.currentDieselValue = currentDieselValue;
	}

	public ApplicationConfigurationService getApplicationConfigurationService() {
		return applicationConfigurationService;
	}

	public void setApplicationConfigurationService(
			ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	public DieselService getDieselService() {
		return dieselService;
	}

	public void setDieselService(DieselService dieselService) {
		this.dieselService = dieselService;
	}
}
