package com.moorkensam.xlra.model.rate;

public enum Measurement {

  LDM("Laadmeter"), KILO("Kilo"), PALET("Palet");

  private String description;

  public String getDescription() {
    return description;
  }

  Measurement(String description) {
    this.description = description;
  }

}
