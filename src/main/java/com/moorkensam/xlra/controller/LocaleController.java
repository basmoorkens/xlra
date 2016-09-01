package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.service.LocaleService;
import com.moorkensam.xlra.service.UserSessionService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean(name = "localeController")
@SessionScoped
public class LocaleController implements Serializable {

  private static final long serialVersionUID = -955974713246228372L;

  @Inject
  private UserSessionService userSessionService;

  @Inject
  private LocaleService localeService;

  private Language language;

  private Locale locale;

  private ResourceBundle messageBundle;

  private List<Language> supportedLanguages;

  private List<Measurement> measurements;

  private List<Kind> kinds;

  /**
   * Load defaults after constructing the bean.
   */
  @PostConstruct
  public void init() {
    loadUserLocale(false);
    loadSupportedLanguages();
    measurements = Arrays.asList(Measurement.values());
    kinds = Arrays.asList(Kind.values());
  }

  private void loadSupportedLanguages() {
    this.supportedLanguages = localeService.getSupportedLanguages();
  }

  /**
   * Load the user locale.
   */
  public void loadUserLocale(boolean forceRefresh) {
    refreshLoggedInUserIfForced(forceRefresh);
    locale = fetchLocale();
    setLocaleToFacesContext(locale);
    language = Language.valueOf(locale.getLanguage().toUpperCase());
  }

  private void refreshLoggedInUserIfForced(boolean forceRefresh) {
    if (forceRefresh) {
      userSessionService.refreshUser();
    }
  }

  private Locale fetchLocale() {
    Locale locale = new Locale(userSessionService.getLoggedInUser().getLanguage().toString());
    return locale;
  }


  public void setLocaleToFacesContext(Locale locale) {
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

  public List<Kind> getKinds() {
    return kinds;
  }

  public void setKinds(List<Kind> kinds) {
    this.kinds = kinds;
  }

  public List<Measurement> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(List<Measurement> measurements) {
    this.measurements = measurements;
  }

  public List<Language> getSupportedLanguages() {
    return supportedLanguages;
  }

  public void setSupportedLanguages(List<Language> supportedLanguages) {
    this.supportedLanguages = supportedLanguages;
  }

  public LocaleService getLocaleService() {
    return localeService;
  }

  public void setLocaleService(LocaleService localeService) {
    this.localeService = localeService;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }
}
