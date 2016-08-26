package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.ZoneUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "manageRatesController")
@ViewScoped
public class ManageRatesController extends AbstractRateController {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  private RateFileService rateFileService;

  @Inject
  private UserSessionService userSessionService;

  @Inject
  private ExcelService excelService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private ZoneUtil zoneUtil;

  private boolean collapseRateLinesDetailGrid = true;

  private boolean collapseZonesDetailGrid = true;

  private List<String> columnHeaders = new ArrayList<String>();

  private TranslationKey keyToAdd;

  private boolean editable;

  private Zone selectedZone;

  private Zone originalSelectedZone;

  private List<RateFileIdNameDto> autoCompleteItems;

  private boolean editZoneMode = false;

  private LazyDataModel<RateFile> model;

  private String zoneDialogTitle;

  /**
   * Logic to initialize the controller.
   */
  @PostConstruct
  public void initializeController() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    editable = userSessionService.isLoggedInUserAdmin();
    resetSelectedRateFile();
    zoneUtil = new ZoneUtil();
    autoCompleteItems = rateFileService.getRateFilesIdAndNamesForAutoComplete();
    initModel();
  }

  private void initModel() {
    model = new LazyDataModel<RateFile>() {

      private static final long serialVersionUID = -7346727256149263406L;

      @Override
      public List<RateFile> load(int first, int pageSize, String sortField, SortOrder sortOrder,
          Map<String, Object> filters) {
        List<RateFile> lazyRateFiles =
            rateFileService.getLazyRateFiles(first, pageSize, sortField, sortOrder, filters);
        model.setRowCount(rateFileService.countRateFiles());
        return lazyRateFiles;
      }
    };
  }

  protected void showConditionDetailDialog() {
    if (editConditionMode) {
      conditionDialogTitle =
          messageUtil.lookupI8nStringAndInjectParams("label.terms.and.conditions.title.edit",
              selectedCondition.getTranslatedKey());
    } else {
      conditionDialogTitle =
          messageUtil.lookupI8nStringAndInjectParams("label.terms.and.conditions.title.create",
              null);
    }
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editConditionDialog').show();");
  }

  private void resetSelectedRateFile() {
    setSelectedRateFile(new RateFile());
  }

  public void saveRateFile() {
    rateFileService.updateRateFile(getSelectedRateFile());
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
    Object newValue = event.getNewValue();
    Object oldValue = event.getOldValue();

    if (newValue != null && !newValue.equals(oldValue)) {
      messageUtil.addMessage("message.ratefile.update.title", "message.ratefile.update.detail",
          newValue + "");
    }
    updateRateFile();
  }


  /**
   * Redirect to the create rates based on existing rates screen. This is for customer rates only.
   * 
   * @throws IOException thrown when the response cant be redirected
   */
  public void goToCreateRatesFromExistingRates() throws IOException {
    FacesContext
        .getCurrentInstance()
        .getExternalContext()
        .redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
                + "/views/admin/rate/createRateFileFromExisting.xhtml");
  }

  /**
   * Redirect to the create rates based on existing rates screen. This is for customer rates only.
   * 
   * @throws IOException thrown when the response cant be redirected
   */
  public void goToFreeCreateRates() throws IOException {
    FacesContext
        .getCurrentInstance()
        .getExternalContext()
        .redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
                + "/views/admin/rate/freeCreateRateFile.xhtml");
  }

  /**
   * Setup the page to edit a zone.
   * 
   * @param zone The zone to edit.
   */
  public void setupEditZone(Zone zone) {
    this.originalSelectedZone = zone;
    this.selectedZone = zone.deepCopy();
    editZoneMode = true;
    showZoneDetailDialog();
  }

  /**
   * Cancels the editing of a zone.
   */
  public void cancelEditZone() {
    resetZoneEdit();
    hideZoneDetailDialog();
  }

  private void resetZoneEdit() {
    selectedZone = null;
    originalSelectedZone = null;
  }

  private void showZoneDetailDialog() {
    if (editZoneMode) {
      zoneDialogTitle =
          messageUtil.lookupI8nStringAndInjectParams("zonedetail.edit.header.edit",
              selectedZone.getName());
    } else {
      zoneDialogTitle =
          messageUtil.lookupI8nStringAndInjectParams("zonedetail.edit.header.create", null);
    }
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editZoneDialog').show();");
  }

  private void hideZoneDetailDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editZoneDialog').hide();");
  }

  /**
   * Setup the page to add a new condition.
   */
  public void setupAddZone() {
    selectedZone = new Zone();
    selectedZone.setZoneType(getSelectedRateFile().getCountry().getZoneType());
    editZoneMode = false;
    showZoneDetailDialog();
  }

  /**
   * Save the edited condition.
   */
  public void saveEditCondition() {
    if (selectedCondition.getConditionKey() != null) {
      if (!editConditionMode) {
        getSelectedRateFile().addCondition(selectedCondition);
      }
      updateRateFile();
      messageUtil.addMessage("message.ratefile.condition.updated.title",
          "message.ratefile.condition.updated.detail", selectedCondition.getTranslatedKey());
      selectedCondition = null;
    } else {
      showConditionDetailDialog();
      messageUtil.addErrorMessage("message.ratefile.condition.empty",
          "message.ratefile.condition.empty.detail");
    }
  }

  /**
   * Check if the selected ratefile has numericalzones.
   * 
   * @return True when numerical, false otherwise.
   */
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
  public boolean isAlphaNumericRateFileZone() {
    if (getSelectedRateFile() != null) {
      return getSelectedRateFile().isAlphaNumericalZoneRateFile();
    }
    return false;
  }

  /**
   * Check if the postal codes in the zone are valid, if so save it.
   */
  public void saveZone() {
    if (isSelectedZoneIsValid()) {
      if (selectedZone.getId() > 0) {
        originalSelectedZone.fillInValuesFromZone(selectedZone);
      } else {
        getSelectedRateFile().addZone(selectedZone);
      }
      updateRateFile();
      messageUtil.addMessage("message.ratefile.zone.updated",
          "message.ratefile.zone.updated.detail", selectedZone.getName());
      resetZoneEdit();
    } else {
      showZoneDetailDialog();
    }
  }

  public void deleteZone(Zone zone) {
    setSelectedRateFile(rateFileService.deleteZone(zone));
  }

  /**
   * Delete a condition.
   * 
   * @param condition The condition to delete.
   */
  public void deleteCondition(Condition condition) {
    messageUtil.addMessage("message.ratefile.condition.removed",
        "message.ratefile.condition.removed.detail", condition.getTranslatedKey());
    getSelectedRateFile().getConditions().remove(condition);
    condition.setRateFile(null);
    updateRateFile();
  }



  /**
   * Fetch the details of from selectEvent.
   * 
   * @param selectEvent the selectevent that triggered this.
   */
  public void fetchDetailsOfRatefile(SelectEvent selectEvent) {
    RateFile fromFrontEnd = (RateFile) selectEvent.getObject();
    setSelectedRateFile(rateFileService.getFullRateFile(fromFrontEnd.getId()));
    refreshPageForRateFile();
  }

  /**
   * Fetch the details of the selected ratefile.
   * 
   * @param rateFileToLoad The ratefile to full load.
   */
  public void fetchDetailsOfRatefile(RateFile rateFileToLoad) {
    setSelectedRateFile(rateFileService.getFullRateFile(rateFileToLoad.getId()));
    refreshPageForRateFile();
  }

  private void refreshPageForRateFile() {
    collapseConditionsDetailGrid = false;
    collapseRateLinesDetailGrid = false;
    collapseZonesDetailGrid = false;
    hasRateFileSelected = true;
    refreshRateLineColumns();
    translationUtil.fillInTranslations(getSelectedRateFile().getConditions());
    messageUtil.addMessage("message.ratefile.rates.loaded", "message.ratefile.rates.loaded.detail",
        getSelectedRateFile().getName());
  }

  /**
   * Download a excel file.
   */
  public void downloadRateExcel() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpServletResponse response =
        (HttpServletResponse) facesContext.getExternalContext().getResponse();
    response.reset();
    response.setHeader("Content-Type", "application/vnd.ms-excel");
    response.setHeader("Content-disposition", "attachment; filename=test.xls");
    XSSFWorkbook exportRateFileToExcel = excelService.exportRateFileToExcel(getSelectedRateFile());
    try {
      OutputStream responseOutputStream = response.getOutputStream();
      exportRateFileToExcel.write(responseOutputStream);
      responseOutputStream.flush();
      responseOutputStream.close();
    } catch (IOException e) {
      logger.error(e);
      messageUtil.addErrorMessage("message.unexpected.error", "message.unexpected.excel.error");
    }
    facesContext.responseComplete();

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

  public RateFile getRateFileById(long id) {
    return rateFileService.getRateFileById(id);
  }

  public boolean isCollapseRateLinesDetailGrid() {
    return collapseRateLinesDetailGrid;
  }

  public void setCollapseRateLinesDetailGrid(boolean collapseRateLinesDetailGrid) {
    this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
  }

  private List<String> refreshRateLineColumns() {
    columnHeaders = new ArrayList<String>();
    for (RateLine rl : getSelectedRateFile().getRateLines()) {
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
    if (getSelectedRateFile().getId() != 0) {
      return getSelectedRateFile().getMeasurement().getDescription();
    } else {
      return "Measurement";
    }
  }

  private boolean isSelectedZoneIsValid() {
    if (selectedZone == null) {
      return false;
    }
    if (getSelectedRateFile().isAlphaNumericalZoneRateFile()) {
      return validateAlphanumericalPostalCodes(selectedZone.getAlphaNumericPostalCodesAsString());
    }
    if (getSelectedRateFile().isNumericalZoneRateFile()) {
      return validateNumericalPostalCodes(selectedZone.getNumericalPostalCodesAsString());
    }
    return false;
  }

  private boolean validateNumericalPostalCodes(String numericalPostalCodeString) {
    try {
      zoneUtil.convertNumericalPostalCodeStringToList(numericalPostalCodeString);
      return true;
    } catch (Exception e) {
      messageUtil.addErrorMessage("message.invalid.postal.codes",
          "message.invalid.postal.codes.numeric.detail");
    }
    return false;
  }

  private boolean validateAlphanumericalPostalCodes(String alphaNumericalPostalCodeString) {
    try {
      zoneUtil.convertAlphaNumericPostalCodeStringToList(alphaNumericalPostalCodeString);
      return true;
    } catch (Exception e) {
      messageUtil.addErrorMessage("message.invalid.postal.codes",
          "message.invalid.postal.codes.alphanumeric.detail");
      showZoneDetailDialog();
    }
    return false;
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
    getSelectedRateFile().addCondition(createCondition);
    keyToAdd = null;
    updateRateFile();
  }

  private void updateRateFile() {
    setSelectedRateFile(rateFileService.updateRateFile(getSelectedRateFile()));
    translationUtil.fillInTranslations(getSelectedRateFile().getConditions());
  }

  public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
    return translationUtil.getAvailableTranslationKeysForSelectedRateFile(getSelectedRateFile());
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

  public List<Language> getLanguages() {
    return Arrays.asList(Language.values());
  }

  public List<RateFileIdNameDto> getAutoCompleteItems() {
    return autoCompleteItems;
  }

  public void setAutoCompleteItems(List<RateFileIdNameDto> autoCompleteItems) {
    this.autoCompleteItems = autoCompleteItems;
  }

  public Zone getSelectedZone() {
    return selectedZone;
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

  public LazyDataModel<RateFile> getModel() {
    return model;
  }

  public void setModel(LazyDataModel<RateFile> model) {
    this.model = model;
  }

  public Zone getOriginalSelectedZone() {
    return originalSelectedZone;
  }

  public void setOriginalSelectedZone(Zone originalSelectedZone) {
    this.originalSelectedZone = originalSelectedZone;
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  public String getZoneDialogTitle() {
    return zoneDialogTitle;
  }

  public void setZoneDialogTitle(String zoneDialogTitle) {
    this.zoneDialogTitle = zoneDialogTitle;
  }
}
