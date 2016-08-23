package com.moorkensam.xlra.service.util;


import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.TokenInfo;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;

public class UserUtil {

  private static final Logger logger = LogManager.getLogger();

  private static UserUtil instance;

  private List<Language> supportedLanguages;

  private ResourceBundle messageBundle;

  /**
   * Get the singleton instance.
   * 
   * @return The instance.
   */
  public static UserUtil getInstance(ResourceBundle messageBundle) {
    if (instance == null) {
      instance = new UserUtil();
      instance.messageBundle = messageBundle;
      instance.fillInTranslations();
    }
    return instance;
  }

  private UserUtil() {
    calculateSupportedLanguages();
  }

  protected void fillInTranslations() {
    for (Language l : supportedLanguages) {
      l.setDescription(messageBundle.getString(l.getI8nKey()));
    }
  }

  /**
   * Create a empty new user with preloaded defaults.
   * 
   * @return The created user.
   */
  public User getNewUser() {
    User user = new User();
    user.setTokenInfo(new TokenInfo());
    user.setUserStatus(UserStatus.FIRST_TIME_LOGIN);
    user.setRoles(new ArrayList<Role>());
    user.setLanguage(getDefaultUserLanguage());
    return user;
  }

  private void calculateSupportedLanguages() {
    List<Locale> supportedLocales = getSupportedLocales();
    supportedLanguages = new ArrayList<Language>();
    Language lang = null;
    for (Locale locale : supportedLocales) {
      lang = parseLocaleToLanguage(locale);
      if (lang != null) {
        supportedLanguages.add(lang);
      }
    }
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

  /**
   * Get the supported languages.
   * 
   * @return Languages supported
   */
  public List<Language> getSupportedLanguages() {
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

  /**
   * Fetch the default userlanguage.
   * 
   * @return The language.
   */
  public Language getDefaultUserLanguage() {
    String localeString =
        ConfigurationLoader.getInstance().getProperty(ConfigurationLoader.DEFAULT_LOCALE);
    Locale locale = new Locale(localeString);
    Language language = null;
    return parseLocaleToLanguage(locale);
  }
}
