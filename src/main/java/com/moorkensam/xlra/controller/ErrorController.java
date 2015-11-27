package com.moorkensam.xlra.controller;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

import com.moorkensam.xlra.service.util.ConfigurationLoader;

@RequestScoped
@ManagedBean
public class ErrorController {

	private ConfigurationLoader configLoader;

	@PostConstruct
	public void initialize() {
		configLoader = ConfigurationLoader.getInstance();
	}

	public String getApplicationBaseUrl() {
		return configLoader
				.getProperty(ConfigurationLoader.APPLICATION_BASE_URL);
	}

}
