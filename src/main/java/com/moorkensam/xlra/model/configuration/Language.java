package com.moorkensam.xlra.model.configuration;

public enum Language {

  NL("Dutch"), EN("English"), FR("French"), DE("German");

  private String description;

  Language(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
