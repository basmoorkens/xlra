package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.LocaleUtil;
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

  private boolean collapseRateLinesDetailGrid = true;

  private List<String> columnHeaders = new ArrayList<String>();

  private TranslationKey keyToAdd;

  private boolean editable;

  private List<RateFileIdNameDto> autoCompleteItems;

  private LazyDataModel<RateFile> model;

  private LocaleUtil localeUtil;

  /**
   * Logic to initialize the controller.
   */
  @PostConstruct
  public void initializeController() {
    initI8n();
    editable = userSessionService.isLoggedInUserAdmin();
    resetSelectedRateFile();
    setZoneUtil(new ZoneUtil());
    autoCompleteItems = rateFileService.getRateFilesIdAndNamesForAutoComplete();
    initModel();
  }

  private void initI8n() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    localeUtil = new LocaleUtil();
    getAllKinds();
    getAllMeasurements();
    getAllTransportTypes();
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

  /**
   * Get all the measurements and fill in the translation for the frontend.
   * 
   * @return List of translated measurements
   */
  private List<Measurement> getAllMeasurements() {
    List<Measurement> measurements = Arrays.asList(Measurement.values());
    localeUtil.fillInMeasurementTranslations(measurements, messageBundle);
    return measurements;
  }

  /**
   * Get all the rateskinds and fill in the translation for the frontend
   * 
   * @return The list of translated ratekinds.
   */
  private List<Kind> getAllKinds() {
    List<Kind> kinds = Arrays.asList(Kind.values());
    localeUtil.fillInRateKindTranslations(kinds, messageBundle);
    return kinds;
  }

  /**
   * Get all the transporttypes translated in the correct language.
   * 
   * @return The list of transporttypes.
   */
  private List<TransportType> getAllTransportTypes() {
    List<TransportType> transportTypes = Arrays.asList(TransportType.values());
    localeUtil.fillInTransportTypeTranslations(transportTypes, messageBundle);
    return transportTypes;
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

  protected void showZoneDetailDialog() {
    if (isEditZoneMode()) {
      setZoneDialogTitle(messageUtil.lookupI8nStringAndInjectParams("zonedetail.edit.header.edit",
          getSelectedZone().getName()));
    } else {
      setZoneDialogTitle(messageUtil.lookupI8nStringAndInjectParams(
          "zonedetail.edit.header.create", ""));
    }
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editZoneDialog').show();");
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
   * Check if the postal codes in the zone are valid, if so save it.
   */
  public void saveZone() {
    if (isSelectedZoneIsValid()) {
      if (!editZoneMode) {
        getSelectedRateFile().addZone(getSelectedZone());
      } else {
        getOriginalSelectedZone().fillInValuesFromZone(getSelectedZone());
      }
      updateRateFile();
      messageUtil.addMessage("message.ratefile.zone.updated",
          "message.ratefile.zone.updated.detail", getSelectedZone().getName());
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

  protected boolean validateNumericalPostalCodes(String numericalPostalCodeString) {
    try {
      getZoneUtil().convertNumericalPostalCodeStringToList(numericalPostalCodeString);
      return true;
    } catch (Exception e) {
      messageUtil.addErrorMessage("message.invalid.postal.codes",
          "message.invalid.postal.codes.numeric.detail");
    }
    return false;
  }

  protected boolean validateAlphanumericalPostalCodes(String alphaNumericalPostalCodeString) {
    try {
      getZoneUtil().convertAlphaNumericPostalCodeStringToList(alphaNumericalPostalCodeString);
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

  public LazyDataModel<RateFile> getModel() {
    return model;
  }

  public void setModel(LazyDataModel<RateFile> model) {
    this.model = model;
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
