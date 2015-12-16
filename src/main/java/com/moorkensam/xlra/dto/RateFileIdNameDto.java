package com.moorkensam.xlra.dto;

import java.io.Serializable;

public class RateFileIdNameDto implements Serializable {

  private static final long serialVersionUID = 7635324771963594755L;

  public RateFileIdNameDto(Long id, String name) {
    this.setId(id);
    this.name = name;
  }

  private Long id;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
