package com.moorkensam.xlra.model.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Base exception class for application specific exceptiosn.
 * 
 * @author bas
 *
 */
public abstract class XlraBaseException extends Exception {
  private static final long serialVersionUID = -4145698228958030600L;

  private List<String> extraArguments;

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

  /**
   * Add an extra argument to the exception.
   * 
   * @param argument The argument to add.
   */
  public void addExtraArgument(String argument) {
    if (extraArguments == null) {
      extraArguments = new ArrayList<String>();
    }
    extraArguments.add(argument);
  }

  public String getBusinessException() {
    return businessException;
  }

  public void setBusinessException(String businessException) {
    this.businessException = businessException;
  }

  public List<String> getExtraArguments() {
    return extraArguments;
  }

  public void setExtraArguments(List<String> extraArguments) {
    this.extraArguments = extraArguments;
  }
}
