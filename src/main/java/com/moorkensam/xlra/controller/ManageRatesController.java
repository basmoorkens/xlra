package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.RateUtil;
import com.moorkensam.xlra.service.util.TranslationUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@ManagedBean(name = "manageRatesController")
@ViewScoped
public class ManageRatesController {

  @Inject
  private RateFileService rateFileService;

  @Inject
  private UserSessionController sessionController;

  private TranslationUtil translationUtil;

  private ConditionFactory conditionFactory;

  private boolean collapseConditionsDetailGrid = true;

  private boolean collapseRateLinesDetailGrid = true;

  private boolean collapseZonesDetailGrid = true;

  private RateFile selectedRateFile;

  private List<String> columnHeaders = new ArrayList<String>();

  private boolean hasRateFileSelected = false;

  private TranslationKey keyToAdd;

  private boolean editable;

  private Condition selectedCondition;

  private List<RateFileIdNameDto> autoCompleteItems;

  private boolean editMode = false;

  /**
   * Logic to initialize the controller.
   */
  @PostConstruct
  public void initializeController() {
    editable = sessionController.isAdmin();
    resetSelectedRateFile();
    translationUtil = new TranslationUtil();
    conditionFactory = new ConditionFactory();
    autoCompleteItems = rateFileService.getRateFilesIdAndNamesForAutoComplete();
  }

  private void resetSelectedRateFile() {
    selectedRateFile = new RateFile();
  }

  public void saveRateFile() {
    rateFileService.updateRateFile(selectedRateFile);
    resetPage();
  }

  private void resetPage() {
    resetSelectedRateFile();
    collapseConditionsDetailGrid = true;
    collapseRateLinesDetailGrid = true;
    collapseZonesDetailGrid = true;
    completeRateName("");
    hasRateFileSelected = false;
    columnHeaders = new ArrayList<String>();
  }

  /**
   * Method that executes when a ratelinecell gets edited.
   * 
   * @param event The event that triggered this.
   */
  public void onRateLineCellEdit(CellEditEvent event) {
    RateUtil.onRateLineCellEdit(event);
    updateRateFile();
  }

  /**
   * Setup the page to edit a condition.
   * 
   * @param condition The condition to edit.
   */
  public void setupEditCondition(Condition condition) {
    this.selectedCondition = condition;
    editMode = true;
    showConditionDetailDialog();
  }

  private void showConditionDetailDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editConditionDialog').show();");
  }

  /**
   * Load a condition based on the key that was selected.
   */
  public void loadConditionBasedOnKey() {
    if (selectedCondition.getConditionKey() == null) {
      selectedCondition = conditionFactory.createEmptyCondition();
    } else {
      selectedCondition = conditionFactory.createCondition(selectedCondition.getConditionKey(), "");
    }
    showConditionDetailDialog();
  }

  /**
   * Setup the page to add a new condition.
   */
  public void setupAddCondition() {
    selectedCondition = new Condition();
    editMode = false;
    showConditionDetailDialog();
  }

  /**
   * Save the edited condition.
   */
  public void saveEditCondition() {
    selectedRateFile.addCondition(selectedCondition);
    updateRateFile();
    MessageUtil.addMessage("Condition updated", "Your changes were saved.");
    selectedCondition = null;
  }

  /**
   * Check if the selected ratefile has numericalzones.
   * 
   * @return True when numerical, false otherwise.
   */
  public boolean isNumericRateFileZone() {
    if (selectedRateFile != null) {
      return selectedRateFile.isNumericalZoneRateFile();
    }
    return false;
  }

  /**
   * Check if the selected ratefile has alpha numericalzones.
   * 
   * @return True when alphanumerical, false otherwise.
   */
  public boolean isAlphaNumericRateFileZone() {
    if (selectedRateFile != null) {
      return selectedRateFile.isAlphaNumericalZoneRateFile();
    }
    return false;
  }

  /**
   * Triggered on zone edit event.
   * 
   * @param event The event that triggered this.
   */
  public void onZoneEdit(RowEditEvent event) {
    Zone zone = (Zone) event.getObject();
    MessageUtil.addMessage("Zone update", zone.getName() + " successfully updated.");
    updateRateFile();
  }

  public void deleteZone(Zone zone) {
    selectedRateFile = rateFileService.deleteZone(zone);
  }

  /**
   * Delete a condition.
   * 
   * @param condition The condition to delete.
   */
  public void deleteCondition(Condition condition) {
    MessageUtil.addMessage("condition removed", condition.getTranslatedKey()
        + " was successfully removed.");
    selectedRateFile.getConditions().remove(condition);
    condition.setRateFile(null);
    updateRateFile();
  }

  /**
   * Fetches the details of the selected ratefile.
   * 
   * @param event The triggering event.
   */
  public void fetchDetailsOfRatefile(SelectEvent event) {
    selectedRateFile = rateFileService.getFullRateFile(((RateFile) event.getObject()).getId());
    collapseConditionsDetailGrid = false;
    collapseRateLinesDetailGrid = false;
    collapseZonesDetailGrid = false;
    hasRateFileSelected = true;
    refreshRateLineColumns();
    translationUtil.fillInTranslations(selectedRateFile.getConditions());
  }

  /**
   * Auto complete method.
   * 
   * @param input The input string.
   * @return List of ratefiles that match the input.
   */
  public List<RateFile> completeRateName(String input) {
    List<Long> filterIds = new ArrayList<Long>();
    for (RateFileIdNameDto dto : autoCompleteItems) {
      if (dto.getName().toLowerCase().contains(input.toLowerCase())) {
        filterIds.add(dto.getId());
      }
    }

    List<RateFile> filteredRateFiles = rateFileService.getRateFilesByIdList(filterIds);
    return filteredRateFiles;
  }

  public RateFileService getRateFileService() {
    return rateFileService;
  }

  public void setRateFileService(RateFileService rateFileService) {
    this.rateFileService = rateFileService;
  }

  public RateFile getSelectedRateFile() {
    return selectedRateFile;
  }

  public void setSelectedRateFile(RateFile selectedRateFile) {
    this.selectedRateFile = selectedRateFile;
  }

  public RateFile getRateFileById(long id) {
    return rateFileService.getRateFileById(id);
  }

  public boolean isCollapseConditionsDetailGrid() {
    return collapseConditionsDetailGrid;
  }

  public void setCollapseConditionsDetailGrid(boolean collapseConditionsDetailGrid) {
    this.collapseConditionsDetailGrid = collapseConditionsDetailGrid;
  }

  public boolean isCollapseRateLinesDetailGrid() {
    return collapseRateLinesDetailGrid;
  }

  public void setCollapseRateLinesDetailGrid(boolean collapseRateLinesDetailGrid) {
    this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
  }

  public boolean isHasRateFileSelected() {
    return hasRateFileSelected;
  }

  public void setHasRateFileSelected(boolean hasRateFileSelected) {
    this.hasRateFileSelected = hasRateFileSelected;
  }

  private List<String> refreshRateLineColumns() {
    columnHeaders = new ArrayList<String>();
    for (RateLine rl : selectedRateFile.getRateLines()) {
      columnHeaders.add(rl.getZone().getName());
    }
    return columnHeaders;
  }

  /**
   * Get the title for the measurement.
   * 
   * @return The title string.
   */
  public String getMeasurementTitle() {
    if (selectedRateFile.getId() != 0) {
      return selectedRateFile.getMeasurement().getDescription();
    } else {
      return "Measurement";
    }
  }

  public List<String> getColumnHeaders() {
    return columnHeaders;
  }

  public void setColumnHeaders(List<String> columnHeaders) {
    this.columnHeaders = columnHeaders;
  }

  public TranslationKey getKeyToAdd() {
    return keyToAdd;
  }

  public void setKeyToAdd(TranslationKey keyToAdd) {
    this.keyToAdd = keyToAdd;
  }

  /**
   * Create a condition for the selected translation key.
   * 
   * @param event The creation event.
   */
  public void createConditionForSelectedTranslationKey(ActionEvent event) {
    Condition createCondition = conditionFactory.createCondition(keyToAdd, "");
    selectedRateFile.addCondition(createCondition);
    keyToAdd = null;
    updateRateFile();
  }

  private void updateRateFile() {
    selectedRateFile = rateFileService.updateRateFile(selectedRateFile);
    translationUtil.fillInTranslations(selectedRateFile.getConditions());
  }

  public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
    return translationUtil.getAvailableTranslationKeysForSelectedRateFile(selectedRateFile);
  }

  public boolean isCollapseZonesDetailGrid() {
    return collapseZonesDetailGrid;
  }

  public void setCollapseZonesDetailGrid(boolean collapseZonesDetailGrid) {
    this.collapseZonesDetailGrid = collapseZonesDetailGrid;
  }

  public boolean isCanEdit() {
    return editable;
  }

  public Condition getSelectedCondition() {
    return selectedCondition;
  }

  public void setSelectedCondition(Condition selectedCondition) {
    this.selectedCondition = selectedCondition;
  }

  public List<Language> getLanguages() {
    return Arrays.asList(Language.values());
  }

  public List<RateFileIdNameDto> getAutoCompleteItems() {
    return autoCompleteItems;
  }

  public void setAutoCompleteItems(List<RateFileIdNameDto> autoCompleteItems) {
    this.autoCompleteItems = autoCompleteItems;
  }

  public boolean isEditMode() {
    return editMode;
  }

  public void setEditMode(boolean editMode) {
    this.editMode = editMode;
  }

}
