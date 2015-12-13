package com.moorkensam.xlra.model.rate;

public enum Kind {

  NORMAL("Normal"), EXPRES("Expres"), FRIDGE("Frigo");

  private String description;

  public String getDescription() {
    return this.description;
  }

  Kind(String description) {
    this.description = description;
  }

}
