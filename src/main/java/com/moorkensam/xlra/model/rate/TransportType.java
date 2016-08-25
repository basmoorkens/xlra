package com.moorkensam.xlra.model.rate;


public enum TransportType implements I8nEnum {

  IMPORT("message.transport.type.import"), EXPORT("message.transport.type.export");

  private String description;

  private String i8nKey;

  TransportType(String i8nKey) {
    this.i8nKey = i8nKey;
  }

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
