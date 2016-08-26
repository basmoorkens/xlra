package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.translation.TranslationKey;

import java.util.List;

/**
 * Interface with conditions frontend methods.
 * 
 * @author bas
 *
 */
interface ConditionsControllerInterface {

  boolean isHasRateFileSelected();

  boolean isCollapseConditionsDetailGrid();

  void setupEditCondition(Condition condition);

  void deleteCondition(Condition condition);

  void setupAddCondition();

  String getConditionDialogTitle();

  boolean isEditConditionMode();

  Condition getSelectedCondition();

  List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile();

  void loadConditionBasedOnKey();

  void saveEditCondition();

}
