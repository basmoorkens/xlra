package com.moorkensam.xlra.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

  private static final Logger logger = LogManager.getLogger();

  public static final String MAIL_SENDER = "email.from";

  public static final String APPLICATION_PASSWORD_SETUP_URL = "application.password.setup.url";

  public static final String APPLICAITON_PASSWORD_RESET_URL = "application.password.reset.url";

  public static final String APPLICATION_BASE_URL = "application.base.url";

  public static final String PDF_GENERATION_PATH = "pdf.generate.path";

  public static final String PDF_TEMPORARY_PATH = "pdf.temp.path";

  public static final String PDF_AUTHOR = "pdf.author";

  public static final String PDF_TITLE_PREFIX = "pdf.title.prefix";

  public static final String IMAGE_PATH = "image.path";

  public static final String EMAIL_ENABLED = "email.enabled";

  public static ConfigurationLoader instance;

  private Properties properties;

  private ConfigurationLoader() {
    try {
      loadProperties();
    } catch (IOException e) {
      logger.error("Failed to read in properties file " + e);
    }
  }

  /**
   * gets the instance.
   * 
   * @return the instance.
   */
  public static ConfigurationLoader getInstance() {
    if (instance == null) {
      instance = new ConfigurationLoader();
    }
    return instance;
  }

  private void loadProperties() throws IOException {
    properties = new Properties();
    InputStream in = ConfigurationLoader.class.getResourceAsStream("/configuration.properties");
    properties.load(in);
    in.close();
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public String getProperty(String propertyToFind) {
    return properties.getProperty(propertyToFind);
  }

}
