package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.TranslationUtil;

import java.util.List;

public abstract class AbstractRateController implements ConditionsControllerInterface {

  protected RateFile selectedRateFile;

  protected boolean hasRateFileSelected = false;

  protected boolean collapseConditionsDetailGrid = true;

  protected Condition selectedCondition;

  protected boolean editConditionMode = false;

  protected String conditionDialogTitle;

  protected TranslationUtil translationUtil = new TranslationUtil();

  protected ConditionFactory conditionFactory = new ConditionFactory();

  protected abstract void showConditionDetailDialog();

  public abstract void deleteCondition(Condition condition);

  public abstract void saveEditCondition();

  @Override
  public boolean isHasRateFileSelected() {
    return hasRateFileSelected;
  }

  public void setHasRateFileSelected(boolean hasRateFileSelected) {
    this.hasRateFileSelected = hasRateFileSelected;
  }

  @Override
  public boolean isCollapseConditionsDetailGrid() {
    return collapseConditionsDetailGrid;
  }

  public void setCollapseConditionsDetailGrid(boolean collapseConditionsDetailGrid) {
    this.collapseConditionsDetailGrid = collapseConditionsDetailGrid;
  }

  /**
   * Setup the page to edit a condition.
   * 
   * @param condition The condition to edit.
   */
  @Override
  public void setupEditCondition(Condition condition) {
    this.selectedCondition = condition;
    setEditConditionMode(true);
    showConditionDetailDialog();
  }

  /**
   * Setup the page to add a conditon.
   */
  public void setupAddCondition() {
    selectedCondition = new Condition();
    setEditConditionMode(false);
    showConditionDetailDialog();
  }

  public void setEditConditionMode(boolean editConditionMode) {
    this.editConditionMode = editConditionMode;
  }

  public void setSelectedCondition(Condition selectedCondition) {
    this.selectedCondition = selectedCondition;
  }

  @Override
  public String getConditionDialogTitle() {
    return conditionDialogTitle;
  }

  @Override
  public boolean isEditConditionMode() {
    return editConditionMode;
  }

  @Override
  public Condition getSelectedCondition() {
    return selectedCondition;
  }

  @Override
  public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
    return getTranslationUtil().getAvailableTranslationKeysForSelectedRateFile(
        getSelectedRateFile());
  }

  /**
   * Load a condition based on the key that was selected.
   */

  @Override
  public void loadConditionBasedOnKey() {
    if (selectedCondition.getConditionKey() == null) {
      selectedCondition = conditionFactory.createEmptyCondition();
    } else {
      selectedCondition = conditionFactory.createCondition(selectedCondition.getConditionKey(), "");
    }
    showConditionDetailDialog();
  }

  public void setConditionDialogTitle(String conditionDialogTitle) {
    this.conditionDialogTitle = conditionDialogTitle;
  }

  public TranslationUtil getTranslationUtil() {
    return translationUtil;
  }

  public void setTranslationUtil(TranslationUtil translationUtil) {
    this.translationUtil = translationUtil;
  }

  public RateFile getSelectedRateFile() {
    return selectedRateFile;
  }

  public void setSelectedRateFile(RateFile selectedRateFile) {
    this.selectedRateFile = selectedRateFile;
  }

}
