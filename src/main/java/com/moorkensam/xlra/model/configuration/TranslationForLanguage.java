package com.moorkensam.xlra.model.configuration;

import javax.persistence.Embeddable;

@Embeddable
public class TranslationForLanguage {

  public TranslationForLanguage(Language lang, String value) {
    this.language = lang;
    this.translation = value;
  }

  public TranslationForLanguage() {

  }

  private String translation;

  private Language language;

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getTranslation() {
    return translation;
  }

  public void setTranslation(String translation) {
    this.translation = translation;
  }

  /**
   * deep copy the translation.
   * 
   * @return the deep copy.
   */
  public TranslationForLanguage deepCopy() {
    TranslationForLanguage copy = new TranslationForLanguage();
    copy.setLanguage(this.language);
    copy.setTranslation(this.translation);
    return copy;
  }

}
