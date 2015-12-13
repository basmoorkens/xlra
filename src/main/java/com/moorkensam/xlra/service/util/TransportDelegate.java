package com.moorkensam.xlra.service.util;

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

  }

  public void send(Message msg) throws MessagingException {
    Transport.send(msg);
  }
}
