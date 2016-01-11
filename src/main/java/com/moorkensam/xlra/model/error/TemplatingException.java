package com.moorkensam.xlra.model.error;

/**
 * Template specific exception.
 * 
 * @author bas
 *
 */
public class TemplatingException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;

  public TemplatingException(String msg, Throwable exc) {
    super(msg, exc);
  }

}
