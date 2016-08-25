package com.moorkensam.xlra.model.error;

/**
 * Ratefile specific exception class.
 * 
 * @author bas
 *
 */
public class RateFileException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;

  private String i8nKey;

  public RateFileException(String businessException) {
    super(businessException);
  }

  public String getI8nKey() {
    return i8nKey;
  }

  public void setI8nKey(String i8nKey) {
    this.i8nKey = i8nKey;
  }

}
