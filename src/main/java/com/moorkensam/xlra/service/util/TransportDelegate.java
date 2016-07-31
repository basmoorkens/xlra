package com.moorkensam.xlra.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

/**
 * This class is just a delegate so we can mock away the static call to transport.send
 * 
 * @author bas
 *
 */
public class TransportDelegate {

  private static TransportDelegate instance;

  private ConfigurationLoader configLoader;

  private static final Logger logger = LogManager.getLogger();

  /**
   * Gets an instance of the clas.
   * 
   * @return the class instance.
   */
  public static TransportDelegate getInstance() {
    if (instance == null) {
      instance = new TransportDelegate();
    }
    return instance;
  }

  private TransportDelegate() {
    configLoader = ConfigurationLoader.getInstance();
  }

  /**
   * Sends an actual email.
   * 
   * @param msg The msg to sent.
   * @throws MessagingException Thrown when there was a problem sending the message.
   */
  public void send(final Message msg) throws MessagingException {
    Boolean emailEnabled =
        Boolean.valueOf(configLoader.getProperty(ConfigurationLoader.EMAIL_ENABLED));
    if (emailEnabled) {
      Transport.send(msg);
    } else {
      logger.warn("Not sending out email coz emailing is disabled");
    }
  }

  public ConfigurationLoader getConfigLoader() {
    return configLoader;
  }

  public void setConfigLoader(ConfigurationLoader configLoader) {
    this.configLoader = configLoader;
  }
}
