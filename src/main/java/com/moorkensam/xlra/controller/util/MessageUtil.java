package com.moorkensam.xlra.controller.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Utility class for adding messages to a page.
 * 
 * @author bas
 *
 */
public class MessageUtil {
  public static void addMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  public static void addErrorMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

}
