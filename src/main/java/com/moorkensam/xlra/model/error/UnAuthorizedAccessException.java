package com.moorkensam.xlra.model.error;

public class UnAuthorizedAccessException extends XlraBaseException {

  private static final long serialVersionUID = 5925052767735525581L;


  public UnAuthorizedAccessException(String businessException) {
    super(businessException);
  }
}
