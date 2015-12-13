package com.moorkensam.xlra.model.error;

public class RateFileException extends Exception {
  private static final long serialVersionUID = -4145698228958030600L;

  private String businessException;

  public RateFileException(String businessException, String msg, Throwable exc) {
    super(msg, exc);
    this.businessException = businessException;
  }

  public RateFileException(String businessException, Throwable exc) {
    super(exc);
    this.businessException = businessException;
  }

  public RateFileException(String businessException) {
    this.businessException = businessException;
  }

  public String getBusinessException() {
    return businessException;
  }

  public void setBusinessException(String businessException) {
    this.businessException = businessException;
  }
}
