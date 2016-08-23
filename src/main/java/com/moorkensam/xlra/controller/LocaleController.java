package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.service.UserSessionService;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean(name = "localeController")
@SessionScoped
public class LocaleController implements Serializable {

  private static final long serialVersionUID = -955974713246228372L;

  @Inject
  private UserSessionService userSessionService;

  private Locale locale;

  @PostConstruct
  public void init() {
    loadUserLocale(false);
  }

  /**
   * Load the user locale.
   */
  public void loadUserLocale(boolean forceRefresh) {
    if (forceRefresh) {
      userSessionService.refreshUser();
    }
    locale = new Locale(userSessionService.getLoggedInUser().getLanguage().toString());
    if (locale == null) {
      locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
  }

  public String getLanguage() {
    return locale.getLanguage();
  }

  public void setLanguage(String language) {
    locale = new Locale(language);
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

}
