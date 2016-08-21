package com.moorkensam.xlra.controller.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
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

  /**
   * Add a message to the facescontext.
   * 
   * @param summary Summary of the message
   * @param detail Detail of the message
   * @param arguments The arguments to inject into the message
   */
  public void addMessage(final String summary, final String detail, final String... arguments) {
    FacesMessage message =
        new FacesMessage(FacesMessage.SEVERITY_INFO, lookupI8nStringAndInjectParams(summary,
            arguments), lookupI8nStringAndInjectParams(detail, arguments));
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  private String lookupI8nStringAndInjectParams(final String key, final Object... params) {
    String unformattedString = messageBundle.getString(key);
    if (params != null) {
      String formattedResult = MessageFormat.format(unformattedString, params);
      return formattedResult;
    }
    return unformattedString;
  }

  /**
   * Add an error message to the facescontext.
   * 
   * @param summary Summary of the error message
   * @param detail Detailed message
   * @param arguments The arguments to pass along
   */
  public void addErrorMessage(final String summary, final String detail, final String... arguments) {
    FacesMessage message =
        new FacesMessage(FacesMessage.SEVERITY_ERROR, lookupI8nStringAndInjectParams(summary,
            arguments), lookupI8nStringAndInjectParams(detail, arguments));
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

}
