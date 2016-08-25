package com.moorkensam.xlra.model.configuration;

import com.moorkensam.xlra.model.rate.I8nEnum;


public enum Language implements I8nEnum {

  NL("message.language.nl"), EN("message.language.en"), FR("message.language.fr"), DE(
      "message.language.de");

  private String description;

  private String i8nKey;

  Language(String i8nKey) {
    this.i8nKey = i8nKey;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public String getI8nKey() {
    return i8nKey;
  }

}
