package com.moorkensam.xlra.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import com.moorkensam.xlra.service.util.ConfigurationLoader;

@RequestScoped
@Named
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
