package com.moorkensam.xlra.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.service.XlraConfigurationService;

@ManagedBean
@ViewScoped
public class ConfigurationController {

	@Inject
	private XlraConfigurationService xlraConfigurationService;
}
