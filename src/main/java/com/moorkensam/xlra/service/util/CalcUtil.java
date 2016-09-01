package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.rate.RateLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

  /**
   * Rounds out the input quantity to the first bigger element if the exact quantity isnot found in
   * the ratelines.
   * 
   * @param toSearchIn The ratelines to look in
   * @param toFind The measurement to find
   * @return The rounded value if it was rounded
   */
  public BigDecimal roundQuantityToUpperIfExactQuantityNotFound(final List<RateLine> toSearchIn,
      final BigDecimal toFind) {
    Set<Double> distinctQuantitys = buildDistinctMeasurementSet(toSearchIn);
    if (distinctQuantitys.contains(toFind.doubleValue())) {
      return roundBigDecimal(toFind);
    }
    // find the first larger quantity then the input and return that
    for (Double quantity : distinctQuantitys) {
      if (quantity > toFind.doubleValue()) {
        return roundBigDecimal(new BigDecimal(quantity));
      }
    }
    return null;
  }

  private Set<Double> buildDistinctMeasurementSet(final List<RateLine> toSearchIn) {
    Set<Double> quantitys = new TreeSet<Double>();
    for (RateLine rl : toSearchIn) {
      quantitys.add(rl.getMeasurement());
    }
    return quantitys;
  }
}
