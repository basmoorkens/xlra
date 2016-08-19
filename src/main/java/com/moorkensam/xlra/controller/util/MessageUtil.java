package com.moorkensam.xlra.controller.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 * Utility class for adding messages to a page.
 * 
 * @author bas
 *
 */
public class MessageUtil {

  private ResourceBundle messageBundle;

  private static MessageUtil instance;

  private MessageUtil() {

  }

  /**
   * Gets the active instance of this class.Creates one if it does not exist yet.
   * 
   * @param messageBundle The messagebudnle to lookup translations from
   * @return The active instance.
   */
  public static MessageUtil getInstance(ResourceBundle messageBundle) {
    if (instance == null) {
      instance = new MessageUtil();
      instance.setMessageBundle(messageBundle);
    }
    return instance;
  }

  public void addMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  public void addErrorMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

}
