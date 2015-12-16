package com.moorkensam.xlra.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class IdentityService {

  private static final Logger logger = LogManager.getLogger();

  private static IdentityService instance;

  /**
   * get the instance.
   * 
   * @return the instance
   */
  public static IdentityService getInstance() {
    if (instance == null) {
      instance = new IdentityService();
    }
    return instance;
  }

  private IdentityService() {

  }

  /**
   * get the next identifier from the generator.
   * 
   * @return the generated identifier.
   */
  public String getNextIdentifier() {
    if (logger.isDebugEnabled()) {
      logger.debug("Generating uq identifier for offerte");
    }

    ensureUniqueness();
    String identifier = "";
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    int year = calendar.get(Calendar.YEAR);
    identifier = year + "-";
    String uqPart = calendar.getTime().getTime() + "";
    identifier += uqPart.substring(2, uqPart.length() - 3);

    if (logger.isDebugEnabled()) {
      logger.debug("Generated " + identifier);
    }
    return identifier;
  }

  private void ensureUniqueness() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      logger.error("Error whilst sleeping in identifier generation");
    }
  }
}
