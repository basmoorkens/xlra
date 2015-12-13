package com.moorkensam.xlra.model.rate;

public enum TransportType {

  IMPORT("Import"), EXPORT("Export");

  private String description;

  TransportType(String descr) {
    this.description = descr;
  }

  public String getDescription() {
    return description;
  }

}
