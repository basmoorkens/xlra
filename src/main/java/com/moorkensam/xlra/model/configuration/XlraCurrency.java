package com.moorkensam.xlra.model.configuration;

public enum XlraCurrency {

  CHF("Swiss franc");

  private String displayName;

  XlraCurrency(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

}
