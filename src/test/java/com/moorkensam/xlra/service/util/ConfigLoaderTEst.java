package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

public class ConfigLoaderTEst extends UnitilsJUnit4 {

  @TestedObject
  private ConfigurationLoader configLoader;

  @Test
  public void testGetInstance() {
    configLoader = ConfigurationLoader.getInstance();
    Assert.assertNotNull(configLoader);
  }

  @Test
  public void testGetEmailProperty() {
    configLoader = ConfigurationLoader.getInstance();
    String mailFrom = configLoader.getProperty(ConfigurationLoader.MAIL_SENDER);
    Assert.assertNotNull(mailFrom);
    Assert.assertEquals("test@test.com", mailFrom);
  }

}
