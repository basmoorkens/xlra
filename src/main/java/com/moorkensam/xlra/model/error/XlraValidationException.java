package com.moorkensam.xlra.model.error;

/**
 * Specific validation exception.
 * 
 * @author bas
 *
 */
public class XlraValidationException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;

  public XlraValidationException(String businessException) {
    super(businessException);
  }

}
