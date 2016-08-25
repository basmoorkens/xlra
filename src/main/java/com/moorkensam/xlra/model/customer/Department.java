package com.moorkensam.xlra.model.customer;

import com.moorkensam.xlra.model.rate.I8nEnum;

public enum Department implements I8nEnum {

  STANDARD("message.department.standard"), FINANCIAL("message.department.financial"), OPERATIONS(
      "message.department.operations"), MANAGEMENT("message.department.management");

  private String i8nKey;

  private String description;

  Department(String i8nKey) {
    this.i8nKey = i8nKey;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getI8nKey() {
    return i8nKey;
  }

}
