package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Language;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TranslationConfigurationLoader {

  private static TranslationConfigurationLoader instance;

  private static final Logger logger = LogManager.getLogger();

  private Map<Language, Properties> i8nProperties;

  private TranslationConfigurationLoader() {
    i8nProperties = new HashMap<Language, Properties>();
    filli8nMap();
    loadProperties();
  }

  private void filli8nMap() {
    for (Language language : Language.values()) {
      i8nProperties.put(language, new Properties());
    }
  }

  /**
   * Get an instance of this class.
   * 
   * @return The instance.
   */
  public static TranslationConfigurationLoader getInstance() {
    if (instance == null) {
      instance = new TranslationConfigurationLoader();
    }
    return instance;
  }

  public String getProperty(String propertyToFind, Language language) {
    return i8nProperties.get(language).getProperty(propertyToFind);
  }

  public void reloadProperties() {
    loadProperties();
  }

  private void loadProperties() {
    for (Language l : i8nProperties.keySet()) {
      try {
        Properties properties = i8nProperties.get(l);
        String fileName = "/translations_" + l + ".properties";
        InputStream in = ConfigurationLoader.class.getResourceAsStream(fileName.toLowerCase());
        properties.load(in);
        in.close();
      } catch (IOException e) {
        logger.error("Exception loading in translations for " + l.getDescription());
      }
    }
  }

}
