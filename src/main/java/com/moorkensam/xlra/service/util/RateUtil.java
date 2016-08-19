package com.moorkensam.xlra.service.util;


import com.moorkensam.xlra.model.configuration.AbstractRate;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;

import java.util.Arrays;
import java.util.List;

public class RateUtil {

  public static List<Language> getLanguages() {
    return Arrays.asList(Language.values());
  }

  /**
   * Generates the name for a customer ratefile based on the filter and customer.
   * 
   * @param filter The filter to use.
   * @param customer The customer to use.
   * @return The generated name.
   */
  public static String generateNameForCustomerRateFile(RateFileSearchFilter filter,
      Customer customer) {
    StringBuilder builder = new StringBuilder();
    builder.append(customer.getName() + " - ");
    builder.append(filter.getCountry().getEnglishName() + " - ");
    builder.append(filter.getMeasurement().getDescription() + " - ");
    builder.append(filter.getRateKind() + " - ");
    builder.append(filter.getTransportationType());
    return builder.toString();
  }

  /**
   * Checks an interval with a list of given intervals to see if they overlap. also checks if the
   * interval end is greater then the interval start.
   * 
   * @param rate The new rate to check.
   * @param existingRates The list of existing rates.
   * @throws IntervalOverlapException Thrown when the rates overlap.
   */
  public static void validateRateInterval(AbstractRate rate,
      List<? extends AbstractRate> existingRates) throws IntervalOverlapException {
    if (rate.getInterval().getStart() >= rate.getInterval().getEnd()) {
      throw new IntervalOverlapException(
          "The start value of the rate should be smaller then the end value.");
    }
    for (AbstractRate existing : existingRates) {
      if (rate.getInterval().getStart() >= existing.getInterval().getStart()
          && rate.getInterval().getStart() < existing.getInterval().getEnd()) {
        throw new IntervalOverlapException(
            "The start value of this rate is already in another rate.");
      }
      if (rate.getInterval().getEnd() >= existing.getInterval().getStart()
          && rate.getInterval().getEnd() < existing.getInterval().getEnd()) {
        throw new IntervalOverlapException("The end value of the rate is already in another rate.");
      }
    }
  }
}
