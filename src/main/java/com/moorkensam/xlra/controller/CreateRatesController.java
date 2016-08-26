package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.LocaleUtil;
import com.moorkensam.xlra.service.util.RateUtil;
import com.moorkensam.xlra.service.util.TranslationUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@ViewScoped
@ManagedBean
public class CreateRatesController extends AbstractRateController {

  @Inject
  private RateFileService rateFileService;

  @Inject
  private CountryService countryService;

  @ManagedProperty("#{localeController}")
  private LocaleController localeController;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private LocaleUtil localeUtil;

  private MessageUtil messageUtil;

  @Inject
  private CustomerService customerService;

  private TranslationUtil translationUtil;

  private ConditionFactory conditionFactory;

  private List<Country> countries;

  private TranslationKey keyToAdd;

  private RateFileSearchFilter filter;

  private boolean hasRateFileSelected = false;

  private double startQuantity;

  private double endQuantity;

  private double increment;

  /** Collapsed for panels of the create rates page. */
  private boolean collapseBasicInfoGrid = false;

  private boolean collapseGenerateRatesGrid = true;

  private boolean collapseRateLinesDetailGrid = false;

  private boolean collapseRateLineEditor = true;

  private boolean collapseSummary = true;

  private boolean editMode = false;

  /**
   * The init method for this controller.
   */
  @PostConstruct
  public void init() {
    localeUtil = new LocaleUtil();
    messageUtil = MessageUtil.getInstance(messageBundle);
    filter = new RateFileSearchFilter();
    selectedRateFile = new RateFile();
    countries = countryService.getAllCountries();
    localeUtil.fillInCountryi8nNameByLanguage(countries, localeController.getLanguage());
    conditionFactory = new ConditionFactory();
    translationUtil = new TranslationUtil();
    hasRateFileSelected = true;
  }

  /**
   * Copys a ratefile and then sets some basic values for it.
   */
  public void generateCopyOfRateFileForFilter() {
    RateFile copiedFile;
    try {
      copiedFile = rateFileService.generateCustomerRateFileForFilterAndCustomer(filter);
      selectedRateFile = copiedFile;
      translationUtil.fillInTranslations(selectedRateFile.getConditions());
      showRateLineEditor();
    } catch (RateFileException e) {
      messageUtil.addErrorMessage("message.ratefile.create.failed", e.getBusinessException());
    }
  }

  /**
   * Reset the generateFRee rates panel and show the basic info panel.
   */
  public void backToBasicInfoFromGenerateFreeRates() {
    startQuantity = 0.0d;
    endQuantity = 0.0d;
    increment = 0.0d;
    showBasicInfoGrid();
  }

  public void generateFreeCreateRateFile() {
    // TODO generate rates based on given quantitys and zones.
  }

  public void showGenerateRatesPanel() {
    collapseAll();
    collapseGenerateRatesGrid = false;
  }

  private void collapseAll() {
    collapseBasicInfoGrid = true;
    collapseGenerateRatesGrid = true;
    collapseRateLineEditor = true;
    collapseConditionsDetailGrid = true;
    collapseSummary = true;
  }

  /**
   * Show the rateline editor.
   */
  public void showRateLineEditor() {
    collapseAll();
    collapseRateLineEditor = false;
  }

  /**
   * Show the summary grid.
   */
  public void showSummary() {
    collapseAll();
    collapseSummary = false;
  }

  /**
   * Show basic info grid.
   */
  public void showBasicInfoGrid() {
    collapseAll();
    collapseBasicInfoGrid = false;
  }

  /**
   * Show conditions editor.
   */
  public void goToConditionsEditor() {
    collapseAll();
    collapseConditionsDetailGrid = false;
  }

  /**
   * Launched when a rate cell is updated.
   * 
   * @param event The update event.
   */
  public void onRateLineCellEdit(CellEditEvent event) {
    Object newValue = event.getNewValue();
    Object oldValue = event.getOldValue();

    if (newValue != null && !newValue.equals(oldValue)) {
      messageUtil.addMessage("message.ratefile.update.title", "message.ratefile.update.detail",
          newValue + "");
    }
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
  }

  /**
   * Create a condition for the current selected translation key.
   * 
   * @param event The event that triggered this.
   */
  public void createConditionForSelectedTranslationKey(ActionEvent event) {
    Condition createCondition = conditionFactory.createCondition(getKeyToAdd(), "");
    selectedRateFile.addCondition(createCondition);
    setKeyToAdd(null);
  }

  public RateFileService getRateFileService() {
    return rateFileService;
  }

  public void setRateFileService(RateFileService rateFileService) {
    this.rateFileService = rateFileService;
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  /**
   * Auto complete method for the customer name.
   * 
   * @param input The input string.
   * @return A list of customers whose names match the input.
   */
  public List<Customer> completeCustomerName(String input) {
    List<Customer> customers = customerService.findCustomersLikeName(input);
    return customers;
  }

  /**
   * Save the new ratefile.
   * 
   * @return The page to redirect to.
   */
  public String saveNewRateFile() {
    rateFileService.createRateFile(selectedRateFile);
    messageUtil.addMessage("message.ratefile.created.title", "message.ratefile.created.detail",
        selectedRateFile.getName());
    return "views/rate/admin/manageRates.xhtml";
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

  /**
   * Save the edited condition.
   */
  public void saveEditCondition() {
    if (selectedCondition.getConditionKey() != null) {
      if (editConditionMode) {
        messageUtil.addMessage("message.ratefile.condition.updated.title",
            "message.ratefile.condition.updated.detail", selectedCondition.getTranslatedKey());
      } else {
        addConditionToRateFile();
      }
    } else {
      showConditionDetailDialog();
      messageUtil.addErrorMessage("message.ratefile.condition.empty",
          "message.ratefile.condition.empty.detail");
    }

  }

  private void addConditionToRateFile() {
    selectedRateFile.addCondition(selectedCondition);
    messageUtil.addMessage("message.ratefile.condition.added",
        "message.ratefile.condition.added.detail", selectedCondition.getTranslatedKey(),
        selectedRateFile.getName());
  }

  public List<Language> getLanguages() {
    return RateUtil.getLanguages();
  }

  public RateFileSearchFilter getFilter() {
    return filter;
  }

  public void setFilter(RateFileSearchFilter filter) {
    this.filter = filter;
  }

  /**
   * Get all the measurements and fill in the translation for the frontend.
   * 
   * @return List of translated measurements
   */
  public List<Measurement> getAllMeasurements() {
    List<Measurement> measurements = Arrays.asList(Measurement.values());
    localeUtil.fillInMeasurementTranslations(measurements, messageBundle);
    return measurements;
  }

  /**
   * Get all the rateskinds and fill in the translation for the frontend
   * 
   * @return The list of translated ratekinds.
   */
  public List<Kind> getAllKinds() {
    List<Kind> kinds = Arrays.asList(Kind.values());
    localeUtil.fillInRateKindTranslations(kinds, messageBundle);
    return kinds;
  }

  /**
   * Get all the transport types with description filled in in the user locale.
   * 
   * @return The list of transport types.
   */
  public List<TransportType> getAllTransportTypes() {
    List<TransportType> transportTypes = Arrays.asList(TransportType.values());
    getLocaleUtil().fillInTransportTypeTranslations(transportTypes, messageBundle);
    return transportTypes;
  }

  public boolean isCollapseRateLineEditor() {
    return collapseRateLineEditor;
  }

  public void setCollapseRateLineEditor(boolean collapseRateLineEditor) {
    this.collapseRateLineEditor = collapseRateLineEditor;
  }

  public boolean isCollapseBasicInfoGrid() {
    return collapseBasicInfoGrid;
  }

  public void setCollapseBasicInfoGrid(boolean collapseBasicInfoGrid) {
    this.collapseBasicInfoGrid = collapseBasicInfoGrid;
  }

  public boolean isCollapseSummary() {
    return collapseSummary;
  }

  public void setCollapseSummary(boolean collapseSummary) {
    this.collapseSummary = collapseSummary;
  }

  public TranslationKey getKeyToAdd() {
    return keyToAdd;
  }

  public void setKeyToAdd(TranslationKey keyToAdd) {
    this.keyToAdd = keyToAdd;
  }

  public ConditionFactory getConditionFactory() {
    return conditionFactory;
  }

  public void setConditionFactory(ConditionFactory conditionFactory) {
    this.conditionFactory = conditionFactory;
  }

  public boolean isEditMode() {
    return editMode;
  }

  public void setEditMode(boolean editMode) {
    this.editMode = editMode;
  }

  public boolean isCollapseRateLinesDetailGrid() {
    return collapseRateLinesDetailGrid;
  }

  public void setCollapseRateLinesDetailGrid(boolean collapseRateLinesDetailGrid) {
    this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
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

  public LocaleController getLocaleController() {
    return localeController;
  }

  public void setLocaleController(LocaleController localeController) {
    this.localeController = localeController;
  }

  public LocaleUtil getLocaleUtil() {
    return localeUtil;
  }

  public void setLocaleUtil(LocaleUtil localeUtil) {
    this.localeUtil = localeUtil;
  }

  public double getStartQuantity() {
    return startQuantity;
  }

  public void setStartQuantity(double startQuantity) {
    this.startQuantity = startQuantity;
  }

  public double getEndQuantity() {
    return endQuantity;
  }

  public void setEndQuantity(double endQuantity) {
    this.endQuantity = endQuantity;
  }

  public double getIncrement() {
    return increment;
  }

  public void setIncrement(double increment) {
    this.increment = increment;
  }

  public boolean isCollapseGenerateRatesGrid() {
    return collapseGenerateRatesGrid;
  }

  public void setCollapseGenerateRatesGrid(boolean collapseGenerateRatesGrid) {
    this.collapseGenerateRatesGrid = collapseGenerateRatesGrid;
  }

}
