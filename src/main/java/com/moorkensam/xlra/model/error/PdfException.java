package com.moorkensam.xlra.model.error;

/**
 * Pdf specific exception.
 * 
 * @author bas
 *
 */
public class PdfException extends XlraBaseException {

  private static final long serialVersionUID = -4145698228958030600L;

  public PdfException(String businessException) {
    super(businessException);
  }

}
