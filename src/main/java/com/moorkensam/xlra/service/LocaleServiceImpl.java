package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;

@Stateless
public class LocaleServiceImpl implements LocaleService {

  private static final Logger logger = LogManager.getLogger();

  @Override
  public List<Language> getSupportedLanguages() {
    List<Locale> supportedLocales = getSupportedLocales();
    List<Language> supportedLanguages = new ArrayList<Language>();
    Language lang = null;
    for (Locale locale : supportedLocales) {
      lang = parseLocaleToLanguage(locale);
      if (lang != null) {
        supportedLanguages.add(lang);
      }
    }
    return supportedLanguages;
  }

  private List<Locale> getSupportedLocales() {
    List<Locale> locales = new ArrayList<Locale>();
    String supportedLocalesString =
        ConfigurationLoader.getInstance().getProperty(ConfigurationLoader.SUPPORTED_LOCALES);
    String[] localesSplit = supportedLocalesString.split(",");
    for (String localeString : localesSplit) {
      locales.add(new Locale(localeString));
    }
    return locales;
  }

  private Language parseLocaleToLanguage(Locale locale) {
    Language language = null;
    try {
      language = Language.valueOf(locale.getLanguage().toUpperCase());
    } catch (Exception e) {
      logger.error("Could not parse " + locale.getLanguage()
          + " to a language in the Language enum.");
    }
    return language;
  }


  @Override
  public Language getDefaultUserLanguage() {
    String localeString =
        ConfigurationLoader.getInstance().getProperty(ConfigurationLoader.DEFAULT_LOCALE);
    Locale locale = new Locale(localeString);
    return parseLocaleToLanguage(locale);
  }
}
