package com.moorkensam.xlra.model.security;

import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class TokenInfo {

  private String verificationToken;

  private Date validTo;

  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  public String getVerificationToken() {
    return verificationToken;
  }

  public void setVerificationToken(String verificationToken) {
    this.verificationToken = verificationToken;
  }
}
