package com.moorkensam.xlra.model.error;

public class IntervalOverlapException extends Exception {

  private static final long serialVersionUID = 5043856493814750689L;

  private String businessException;

  public IntervalOverlapException(String businessException, String msg, Throwable exc) {
    super(msg, exc);
    this.businessException = businessException;
  }

  public IntervalOverlapException(String businessException, Throwable exc) {
    super(exc);
    this.businessException = businessException;
  }

  public IntervalOverlapException(String businessException) {
    this.businessException = businessException;
  }

  public String getBusinessException() {
    return businessException;
  }

  public void setBusinessException(String businessException) {
    this.businessException = businessException;
  }
}
