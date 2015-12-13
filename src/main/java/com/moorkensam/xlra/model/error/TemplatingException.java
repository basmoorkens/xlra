package com.moorkensam.xlra.model.error;

public class TemplatingException extends Exception {

  private static final long serialVersionUID = -4145698228958030600L;

  public TemplatingException(String msg, Throwable exc) {
    super(msg, exc);
  }

}
