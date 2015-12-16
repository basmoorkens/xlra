package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.service.util.ConfigurationLoader;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class ErrorController {

  private ConfigurationLoader configLoader;

  @PostConstruct
  public void initialize() {
    configLoader = ConfigurationLoader.getInstance();
  }

  public String getApplicationBaseUrl() {
    return configLoader.getProperty(ConfigurationLoader.APPLICATION_BASE_URL);
  }

}
