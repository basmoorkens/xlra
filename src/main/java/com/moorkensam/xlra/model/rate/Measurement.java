package com.moorkensam.xlra.model.rate;

public enum Measurement implements I8nEnum {

  LDM("message.measurement.ldm"), KILO("message.measurement.kilo"), PALET(
      "message.measurement.palet");

  private String i8nKey;

  private String description;

  Measurement(String i8nKey) {
    this.i8nKey = i8nKey;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getI8nKey() {
    return i8nKey;
  }

}
