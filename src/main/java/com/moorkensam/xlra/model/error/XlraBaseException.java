package com.moorkensam.xlra.model.error;

/**
 * Base exception class for application specific exceptiosn.
 * 
 * @author bas
 *
 */
public abstract class XlraBaseException extends Exception {
  private static final long serialVersionUID = -4145698228958030600L;

  private String businessException;

  public XlraBaseException(String businessException, String msg, Throwable exc) {
    super(msg, exc);
    this.businessException = businessException;
  }

  public XlraBaseException(String businessException, Throwable exc) {
    super(exc);
    this.businessException = businessException;
  }

  public XlraBaseException(String businessException) {
    this.businessException = businessException;
  }

  public String getBusinessException() {
    return businessException;
  }

  public void setBusinessException(String businessException) {
    this.businessException = businessException;
  }
}
