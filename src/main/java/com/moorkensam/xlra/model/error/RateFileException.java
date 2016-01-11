package com.moorkensam.xlra.model.error;

/**
 * Ratefile specific exception class.
 * 
 * @author bas
 *
 */
public class RateFileException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;

  public RateFileException(String businessException) {
    super(businessException);
  }

}
