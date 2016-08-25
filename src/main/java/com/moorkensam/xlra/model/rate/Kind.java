package com.moorkensam.xlra.model.rate;

public enum Kind implements I8nEnum {

  NORMAL("message.kind.normal"), EXPRES("message.kind.expres"), FRIDGE("message.kind.fridge");

  private String description;

  private String i8nKey;

  public String getDescription() {
    return this.description;
  }

  Kind(String i8nKey) {
    this.i8nKey = i8nKey;
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
