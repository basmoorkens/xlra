package com.moorkensam.xlra.model.error;

/**
 * User specific exception.
 * 
 * @author bas
 *
 */
public class UserException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;


  public UserException(String businessException) {
    super(businessException);
  }
}
