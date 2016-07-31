package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.service.util.ConfigurationLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class LogoutController {

  private static final Logger logger = LogManager.getLogger();

  private ConfigurationLoader configLoader;

  @Inject
  private UserSessionController sessionController;

  @PostConstruct
  public void init() {
    configLoader = ConfigurationLoader.getInstance();
  }

  /**
   * Logs out a user by destroying the session.
   * 
   * @throws IOException When the baseurl for the applicaiton couldnt be loaded.
   */
  public void logout() throws IOException {
    logger.info("Logging out " + sessionController.getLoggedInUser().getEmail());
    FacesContext fc = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
    session.invalidate();
    FacesContext.getCurrentInstance().getExternalContext()
        .redirect(configLoader.getProperty(ConfigurationLoader.APPLICATION_BASE_URL));
  }

}
