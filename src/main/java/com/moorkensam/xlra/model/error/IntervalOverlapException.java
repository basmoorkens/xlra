package com.moorkensam.xlra.model.error;

/**
 * Interval specific exceptions.
 * 
 * @author bas
 *
 */
public class IntervalOverlapException extends XlraBaseException {

  public IntervalOverlapException(String businessException) {
    super(businessException);
  }

  private static final long serialVersionUID = 5043856493814750689L;
}
