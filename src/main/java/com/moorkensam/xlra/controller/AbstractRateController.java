package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.TranslationUtil;
import com.moorkensam.xlra.service.util.ZoneUtil;

import org.primefaces.context.RequestContext;

import java.util.List;

public abstract class AbstractRateController implements ConditionsControllerInterface,
    ZoneControllerInterface {

  protected RateFile selectedRateFile;

  protected boolean hasRateFileSelected = false;

  protected boolean collapseConditionsDetailGrid = true;

  protected Condition selectedCondition;

  protected boolean editConditionMode = false;

  protected boolean editZoneMode = false;

  protected String conditionDialogTitle;

  protected TranslationUtil translationUtil = new TranslationUtil();

  protected ConditionFactory conditionFactory = new ConditionFactory();

  protected ZoneUtil zoneUtil = new ZoneUtil();

  protected boolean collapseZonesDetailGrid = true;

  protected Zone selectedZone;

  protected Zone originalSelectedZone;

  protected String zoneDialogTitle;

  protected abstract void showConditionDetailDialog();

  protected abstract void showZoneDetailDialog();

  public abstract void deleteCondition(Condition condition);

  public abstract void deleteZone(Zone zone);

  public abstract void saveEditCondition();

  public abstract void saveZone();

  protected abstract boolean validateAlphanumericalPostalCodes(String codes);

  protected abstract boolean validateNumericalPostalCodes(String codes);

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

  @Override
  public boolean isCollapseZonesDetailGrid() {
    return collapseZonesDetailGrid;
  }

  public void setCollapseZonesDetailGrid(boolean collapseZonesDetailGrid) {
    this.collapseZonesDetailGrid = collapseZonesDetailGrid;
  }

  /**
   * Check if the selected ratefile has numericalzones.
   * 
   * @return True when numerical, false otherwise.
   */
  @Override
  public boolean isNumericRateFileZone() {
    if (getSelectedRateFile() != null) {
      return getSelectedRateFile().isNumericalZoneRateFile();
    }
    return false;
  }

  /**
   * Check if the selected ratefile has alpha numericalzones.
   * 
   * @return True when alphanumerical, false otherwise.
   */
  @Override
  public boolean isAlphaNumericRateFileZone() {
    if (getSelectedRateFile() != null) {
      return getSelectedRateFile().isAlphaNumericalZoneRateFile();
    }
    return false;
  }

  @Override
  public void setupEditZone(Zone zone) {
    originalSelectedZone = zone;
    selectedZone = zone.deepCopy();
    selectedZone.setId(originalSelectedZone.getId());
    setEditZoneMode(true);
    showZoneDetailDialog();
  }

  /**
   * Setup the page to add a new condition.
   */
  @Override
  public void setupAddZone() {
    setSelectedZone(new Zone());
    getSelectedZone().setZoneType(getSelectedRateFile().getCountry().getZoneType());
    setEditZoneMode(false);
    showZoneDetailDialog();
  }

  @Override
  public String getZoneDialogTitle() {
    return zoneDialogTitle;
  }

  @Override
  public Zone getSelectedZone() {
    return selectedZone;
  }

  /**
   * Cancels the editing of a zone.
   */
  @Override
  public void cancelEditZone() {
    resetZoneEdit();
    hideZoneDetailDialog();
  }

  protected boolean isSelectedZoneIsValid() {
    if (getSelectedZone() == null) {
      return false;
    }
    if (getSelectedRateFile().isAlphaNumericalZoneRateFile()) {
      return validateAlphanumericalPostalCodes(getSelectedZone()
          .getAlphaNumericPostalCodesAsString());
    }
    if (getSelectedRateFile().isNumericalZoneRateFile()) {
      return validateNumericalPostalCodes(getSelectedZone().getNumericalPostalCodesAsString());
    }
    return false;
  }

  private void hideZoneDetailDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editZoneDialog').hide();");
  }

  protected void resetZoneEdit() {
    setSelectedZone(null);
    setOriginalSelectedZone(null);
  }

  public Zone getOriginalSelectedZone() {
    return originalSelectedZone;
  }

  public void setOriginalSelectedZone(Zone originalSelectedZone) {
    this.originalSelectedZone = originalSelectedZone;
  }

  public void setSelectedZone(Zone selectedZone) {
    this.selectedZone = selectedZone;
  }

  public boolean isEditZoneMode() {
    return editZoneMode;
  }

  public void setEditZoneMode(boolean editZoneMode) {
    this.editZoneMode = editZoneMode;
  }

  public void setZoneDialogTitle(String zoneDialogTitle) {
    this.zoneDialogTitle = zoneDialogTitle;
  }

  public ZoneUtil getZoneUtil() {
    return zoneUtil;
  }

  public void setZoneUtil(ZoneUtil zoneUtil) {
    this.zoneUtil = zoneUtil;
  }
}
