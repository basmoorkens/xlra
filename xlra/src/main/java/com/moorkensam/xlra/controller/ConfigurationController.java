package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.model.DieselRate;
import com.moorkensam.xlra.service.ApplicationConfigurationService;

@ManagedBean
@ViewScoped
public class ConfigurationController {

	@Inject
	private ApplicationConfigurationService applicationConfigurationService;
	
	private List<DieselRate> dieselRates; 
	
	@PostConstruct
	public void initPage() {
		refreshDieselRates();
	}
	
	public void refreshDieselRates() {
		dieselRates = applicationConfigurationService.getAllDieselRates();
	}

	public List<DieselRate> getDieselRates() {
		return dieselRates;
	}

	public void setDieselRates(List<DieselRate> dieselRates) {
		this.dieselRates = dieselRates;
	}
}
