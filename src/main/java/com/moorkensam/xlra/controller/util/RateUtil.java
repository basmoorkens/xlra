package com.moorkensam.xlra.controller.util;

import com.moorkensam.xlra.model.configuration.Language;

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
}
