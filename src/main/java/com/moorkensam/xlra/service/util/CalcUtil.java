package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcUtil {

  private static CalcUtil instance;

  private CalcUtil() {}

  /**
   * gets the instance from this class.
   * 
   * @return the instance.
   */
  public static CalcUtil getInstance() {
    if (instance == null) {
      instance = new CalcUtil();
    }
    return instance;
  }

  /**
   * convert a percentage to a base multiplier.
   * 
   * @param percentage the percentage to convert
   * @return the base multiplier.
   */
  public BigDecimal convertPercentageToBaseMultiplier(double percentage) {
    BigDecimal bd = new BigDecimal((double) percentage / 100d);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd;
  }

  public BigDecimal roundBigDecimal(BigDecimal number) {
    number = number.setScale(2, RoundingMode.HALF_UP);
    return number;
  }
}
