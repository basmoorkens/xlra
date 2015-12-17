package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
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
import com.moorkensam.xlra.service.util.RateUtil;
import com.moorkensam.xlra.service.util.TranslationUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@ViewScoped
@ManagedBean
public class CreateRatesController {

  @Inject
  private RateFileService rateFileService;

  @Inject
  private CountryService countryService;

  @Inject
  private CustomerService customerService;

  private TranslationUtil translationUtil;

  private ConditionFactory conditionFactory;

  private List<Country> countries;

  private List<Measurement> measurements;

  private TranslationKey keyToAdd;

  private List<Kind> kindOfRates;

  private RateFile rateFile;

  private List<Customer> fullCustomers;

  private RateFileSearchFilter filter;

  private boolean hasRateFileSelected = false;

  private boolean collapseRateLinesDetailGrid = false;

  private boolean collapseConditionsDetailGrid = false;

  /** Collapsed for panels of the create rates page. */
  private boolean collapseBasicInfoGrid = false;

  private boolean collapseRateLineEditor = true;

  private boolean collapseConditionsEditor = true;

  private boolean collapseSummary = true;

  private Condition selectedCondition;

  /**
   * The init method for this controller.
   */
  @PostConstruct
  public void init() {
    filter = new RateFileSearchFilter();
    setRateFile(new RateFile());
    countries = countryService.getAllCountries();
    fullCustomers = customerService.getAllFullCustomers();
    measurements = Arrays.asList(Measurement.values());
    kindOfRates = Arrays.asList(Kind.values());
    conditionFactory = new ConditionFactory();
    translationUtil = new TranslationUtil();
  }

  /**
   * Copys a ratefile and then sets some basic values for it.
   */
  public void calculateCopyOfRateFileForFilter() {
    RateFile copiedFile = rateFileService.getCopyOfRateFileForFilter(filter);
    copiedFile.setCustomer(rateFile.getCustomer());
    copiedFile.setName(rateFile.getName());
    rateFile = copiedFile;
    translationUtil.fillInTranslations(rateFile.getConditions());
    showRateLineEditor();
  }

  /**
   * Show the rateline editor.
   */
  public void showRateLineEditor() {
    collapseBasicInfoGrid = true;
    collapseRateLineEditor = false;
    collapseConditionsEditor = true;
    collapseSummary = true;
  }

  /**
   * Show the summary grid.
   */
  public void showSummary() {
    collapseBasicInfoGrid = true;
    collapseRateLineEditor = true;
    collapseConditionsEditor = true;
    collapseSummary = false;
  }

  /**
   * Show basic info grid.
   */
  public void showBasicInfoGrid() {
    collapseBasicInfoGrid = false;
    collapseRateLineEditor = true;
    collapseConditionsEditor = true;
    collapseSummary = true;
  }

  /**
   * Show conditions editor.
   */
  public void goToConditionsEditor() {
    collapseBasicInfoGrid = true;
    collapseRateLineEditor = true;
    collapseConditionsEditor = false;
    collapseSummary = true;
  }

  public void onRateLineCellEdit(CellEditEvent event) {
    RateUtil.onRateLineCellEdit(event);
  }

  /**
   * Delete a condition.
   * 
   * @param condition The condition to delete.
   */
  public void deleteCondition(Condition condition) {
    MessageUtil.addMessage("condition removed", condition.getTranslatedKey()
        + " was successfully removed.");
    rateFile.getConditions().remove(condition);
    condition.setRateFile(null);
  }

  public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
    return translationUtil.getAvailableTranslationKeysForSelectedRateFile(rateFile);
  }

  /**
   * Create a condition for the current selected translation key.
   * 
   * @param event The event that triggered this.
   */
  public void createConditionForSelectedTranslationKey(ActionEvent event) {
    Condition createCondition = conditionFactory.createCondition(getKeyToAdd(), "");
    rateFile.addCondition(createCondition);
    setKeyToAdd(null);
  }

  public RateFileService getRateFileService() {
    return rateFileService;
  }

  public void setRateFileService(RateFileService rateFileService) {
    this.rateFileService = rateFileService;
  }

  public RateFile getRateFile() {
    return rateFile;
  }

  public void setRateFile(RateFile rateFile) {
    this.rateFile = rateFile;
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  public List<Measurement> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(List<Measurement> measurements) {
    this.measurements = measurements;
  }

  public List<Kind> getKindOfRates() {
    return kindOfRates;
  }

  public void setKindOfRates(List<Kind> kindOfRates) {
    this.kindOfRates = kindOfRates;
  }

  /**
   * Auto complete method for the customer name.
   * 
   * @param input The input string.
   * @return A list of customers whose names match the input.
   */
  public List<Customer> completeCustomerName(String input) {
    List<Customer> filteredCustomers = new ArrayList<Customer>();

    for (Customer fc : fullCustomers) {
      if (fc.getName().toLowerCase().contains(input.toLowerCase())) {
        filteredCustomers.add(fc);
      }
    }
    return filteredCustomers;
  }

  /**
   * Save the new ratefile.
   * 
   * @return The page to redirect to.
   */
  public String saveNewRateFile() {
    rateFileService.createRateFile(rateFile);
    MessageUtil.addMessage("Rates created", "Succesfully created rates for " + rateFile.getName());
    return "views/rate/admin/manageRates.xhtml";
  }

  /**
   * Setup the page to edit a condition.
   * 
   * @param condition The condition to edit.
   */
  public void setupEditCondition(Condition condition) {
    this.setSelectedCondition(condition);
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editConditionDialog').show();");
  }

  /**
   * Save the edited condition.
   */
  public void saveEditCondition() {
    MessageUtil.addMessage("Condition updated", "Updated " + selectedCondition.getTranslatedKey()
        + ".");
  }

  public List<Language> getLanguages() {
    return RateUtil.getLanguages();
  }

  public List<Customer> getFullCustomers() {
    return fullCustomers;
  }

  public void setFullCustomers(List<Customer> fullCustomers) {
    this.fullCustomers = fullCustomers;
  }

  public RateFileSearchFilter getFilter() {
    return filter;
  }

  public void setFilter(RateFileSearchFilter filter) {
    this.filter = filter;
  }

  public boolean isHasRateFileSelected() {
    return hasRateFileSelected;
  }

  public void setHasRateFileSelected(boolean hasRateFileSelected) {
    this.hasRateFileSelected = hasRateFileSelected;
  }

  public boolean isCollapseRateLinesDetailGrid() {
    return collapseRateLinesDetailGrid;
  }

  public void setCollapseRateLinesDetailGrid(boolean collapseRateLinesDetailGrid) {
    this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
  }

  public boolean isCollapseConditionsDetailGrid() {
    return collapseConditionsDetailGrid;
  }

  public void setCollapseConditionsDetailGrid(boolean collapseConditionsDetailGrid) {
    this.collapseConditionsDetailGrid = collapseConditionsDetailGrid;
  }

  public List<TransportType> getAllTransportTypes() {
    return Arrays.asList(TransportType.values());
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

  public boolean isCollapseConditionsEditor() {
    return collapseConditionsEditor;
  }

  public void setCollapseConditionsEditor(boolean collapseConditionsEditor) {
    this.collapseConditionsEditor = collapseConditionsEditor;
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

  public Condition getSelectedCondition() {
    return selectedCondition;
  }

  public void setSelectedCondition(Condition selectedCondition) {
    this.selectedCondition = selectedCondition;
  }

}
