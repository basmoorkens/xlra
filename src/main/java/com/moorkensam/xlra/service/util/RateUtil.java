package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.AbstractRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.IntervalOverlapException;

import org.primefaces.event.CellEditEvent;

import java.util.Arrays;
import java.util.List;

public class RateUtil {

  /**
   * Handles a rateline cell edit event.
   * 
   * @param event The evnet that triggered this.
   */
  public static void onRateLineCellEdit(CellEditEvent event) {
    Object newValue = event.getNewValue();
    Object oldValue = event.getOldValue();

    if (newValue != null && !newValue.equals(oldValue)) {
      MessageUtil.addMessage("Rate updated", "Rate value updated to " + newValue);
    }
  }

  /**
   * Handles a condition cell edit event.
   * 
   * @param event The triggering event.
   */
  public static void onConditionCellEdit(CellEditEvent event) {
    Object newValue = event.getNewValue();
    Object oldValue = event.getOldValue();

    if (newValue != null && !newValue.equals(oldValue)) {
      MessageUtil.addMessage("Condition updated", "Condition updated to" + newValue);
    }
  }

  public static List<Language> getLanguages() {
    return Arrays.asList(Language.values());
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
