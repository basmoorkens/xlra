package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.delegate.QuotationControllerDelegate;
import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.impl.FileServiceImpl;
import com.moorkensam.xlra.service.util.CustomerUtil;
import com.moorkensam.xlra.service.util.FileUtil;
import com.moorkensam.xlra.service.util.LocaleUtil;
import com.moorkensam.xlra.service.util.QuotationUtil;
import com.moorkensam.xlra.service.util.TranslationUtil;

import org.primefaces.model.DualListModel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@ViewScoped
public class CreateQuotationController {

  @Inject
  private QuotationService quotationService;

  @Inject
  private CustomerService customerService;

  @Inject
  private CountryService countryService;

  @ManagedProperty("#{localeController}")
  private LocaleController localeController;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private LocaleUtil localeUtil;

  private MessageUtil messageUtil;

  private TranslationUtil translationUtil;

  private FileService fileService;

  private QuotationQuery quotationQuery;

  private QuotationResult quotationResult;

  private Customer customerToAdd;

  private boolean renderAddCustomerGrid;

  private List<Country> allCountries;

  private String manualAddedEmail;

  private QuotationControllerDelegate controllerDelegate;

  private DualListModel<CustomerContact> customerContacts;

  /**
   * Init logic for the controller.
   */
  @PostConstruct
  public void init() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    controllerDelegate = new QuotationControllerDelegate();
    allCountries = countryService.getAllCountries();
    initializeNewQuotationQuery();
    initializeNewCustomer();
    fileService = new FileServiceImpl();
    translationUtil = new TranslationUtil();
    showCustomerPanel();
    customerContacts = new DualListModel<CustomerContact>();
    localeUtil = new LocaleUtil();
  }

  public void resetPage() {
    initializeNewQuotationQuery();
    showCustomerPanel();
  }

  /**
   * Go to the offerte detail page in the application.
   * 
   * @throws IOException Thrown when there was a problem redirecting.
   */
  public void goToOfferteDetail() throws IOException {
    controllerDelegate.goToOfferteDetail(quotationResult);
  }

  private void initializeNewQuotationQuery() {
    QuotationQuery query = new QuotationQuery();
    query.setTransportType(TransportType.EXPORT);
    for (Country c : allCountries) {
      if (c.getShortName().toLowerCase().equals("be")) {
        query.setCountry(c);
      }
    }
    query.setMeasurement(Measurement.PALET);
    query.setKindOfRate(Kind.NORMAL);
    setQuotationQuery(query);
  }

  private void initializeNewCustomer() {
    customerToAdd = new Customer();
  }

  public void setupPageForNewCustomer() {
    renderAddCustomerGrid = true;
  }

  /**
   * Create a customer on the backend.
   */
  public void createCustomer() {
    try {
      getQuotationQuery().setCustomer(
          getCustomerService().createCustomerAndReturnManaged(customerToAdd));
      messageUtil.addMessage("message.customer.created.title", "message.customer.created.detail",
          customerToAdd.getName());
      renderAddCustomerGrid = false;
      customerToAdd = new Customer();
    } catch (XlraValidationException exc) {
      messageUtil.addErrorMessage("message.customer.invalid.data", exc.getBusinessException(), exc
          .getExtraArguments().get(0));
    }
  }

  public List<Language> getAllLanguages() {
    List<Language> supportedLanguages = localeController.getSupportedLanguages();
    localeUtil.fillInLanguageTranslations(supportedLanguages, getMessageBundle());
    return supportedLanguages;
  }

  /**
   * Proces the customer section.
   */
  public void procesCustomer() {
    if (getQuotationQuery().getCustomer().isHasOwnRateFile()) {
      setupFiltersFromExistingCustomer();
    }
    showRateFilterPanel();
  }

  /**
   * Show the customer section.
   */
  public void showCustomerPanel() {
    controllerDelegate.showCustomerPanel();
  }

  /**
   * Show the ratefilter section.
   */
  public void showRateFilterPanel() {
    controllerDelegate.showRateFilterPanel();
  }

  /**
   * Show the options section.
   */
  public void showOptionsPanel() {
    controllerDelegate.showOptionsPanel();
  }

  /**
   * Show the summary section.
   */
  public void showSummaryPanel() {
    controllerDelegate.showSummaryPanel();
  }

  /**
   * Show the result section.
   */
  public void showResultPanel() {
    controllerDelegate.showResultPanel();
  }

  /**
   * Proces the selected ratefiles: generates a draft result.
   */
  public void processRateFilters() {
    try {
      quotationResult = quotationService.generateQuotationResultForQuotationQuery(quotationQuery);
      fillInOptionTranslations();
      showOptionsPanel();
    } catch (RateFileException re2) {
      showRateFileError(re2);
    } catch (EJBException e) {
      if (e.getCausedByException() instanceof RateFileException) {
        RateFileException re = (RateFileException) e.getCausedByException();
        showRateFileError(re);
      } else {
        messageUtil
            .addErrorMessage("message.unknown.exception", "message.unknown.exception.detail");
      }
    }
  }

  private void fillInOptionTranslations() {
    translationUtil.fillInTranslations(quotationResult.getSelectableOptions());
  }

  public void selectAllOptions() {
    applySelection(true);
  }

  public void deSelectAllOptions() {
    applySelection(false);
  }

  private void applySelection(boolean selected) {
    for (OfferteOptionDto option : quotationResult.getSelectableOptions()) {
      option.setSelected(selected);
    }
  }

  /**
   * Proces the options that were selected.
   */
  public void processOptions() {
    try {
      quotationResult = quotationService.generateEmailAndPdfForOfferte(quotationResult);
      setupCustomerContacts();
      showSummaryPanel();
    } catch (RateFileException e) {
      showRateFileError(e);
    }
  }

  private void setupCustomerContacts() {
    customerContacts = new DualListModel<CustomerContact>();
    List<CustomerContact> contacts =
        CustomerUtil.getInstance().getCustomerContactsForCustomerWithoutStandardContact(
            quotationResult.getQuery().getCustomer());
    customerContacts.setSource(contacts);
    customerContacts.setTarget(quotationResult.getEmailResult().getSelectedContacts());
  }

  /**
   * Download the generated pdf document.
   * 
   * @throws IOException Throw when there was a problem loading the pdf from the filesystem.
   */
  public void downloadPdf() throws IOException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpServletResponse response =
        (HttpServletResponse) facesContext.getExternalContext().getResponse();
    File pdfToServe = fileService.getOffertePdfFromFileSystem(quotationResult.getPdfFileName());
    FileUtil.serveDownloadToHttpServletResponse(pdfToServe, response);
    facesContext.responseComplete();
  }

  /**
   * Submit an offerte for persistence.
   */
  public void submitOfferte() {
    try {
      QuotationUtil.getInstance().setCustomerContactsForOfferte(customerContacts.getTarget(),
          quotationResult);
      quotationService.submitQuotationResult(quotationResult);
      messageUtil.addMessage("message.offerte.sent.title", "message.offerte.sent.detail",
          quotationResult.getEmailResult().getRecipientsAsString());
      showResultPanel();
    } catch (RateFileException re2) {
      showRateFileError(re2);
    } catch (EJBException e) {
      if (e.getCausedByException() instanceof RateFileException) {
        RateFileException re = (RateFileException) e.getCausedByException();
        showRateFileError(re);
      } else {
        messageUtil
            .addErrorMessage("message.unknown.exception", "message.unknown.exception.detail");
      }
    }
  }

  private void showRateFileError(RateFileException re) {
    messageUtil
        .addErrorMessage("message.processing.unexecptected.error", re.getBusinessException());
  }

  private void setupFiltersFromExistingCustomer() {
    // TODO implements loading of filters based on customer ratefile present
  }

  /**
   * Auto complete method for the customer name.
   * 
   * @param input The input string on the frontend.
   * @return Returns a list of names that match the input.
   */
  public List<Customer> completeCustomerName(String input) {
    List<Customer> customers = getCustomerService().findCustomersLikeName(input);
    return customers;
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

  public Customer getCustomerToAdd() {
    return customerToAdd;
  }

  public void setCustomerToAdd(Customer customerToAdd) {
    this.customerToAdd = customerToAdd;
  }

  public boolean isRenderAddCustomerGrid() {
    return renderAddCustomerGrid;
  }

  public void setRenderAddCustomerGrid(boolean renderAddCustomerGrid) {
    this.renderAddCustomerGrid = renderAddCustomerGrid;
  }

  public List<Country> getAllCountries() {
    localeUtil.fillInCountryi8nNameByLanguage(allCountries, localeController.getLanguage());
    return allCountries;
  }

  public void setAllCountries(List<Country> allCountries) {
    this.allCountries = allCountries;
  }

  public QuotationQuery getQuotationQuery() {
    return quotationQuery;
  }

  public void setQuotationQuery(QuotationQuery quotationQuery) {
    this.quotationQuery = quotationQuery;
  }

  public QuotationResult getQuotationResult() {
    return quotationResult;
  }

  public void setQuotationResult(QuotationResult quotationResult) {
    this.quotationResult = quotationResult;
  }

  public boolean isCollapseSummaryPanel() {
    return controllerDelegate.isCollapseSummaryPanel();
  }

  public void setCollapseSummaryPanel(boolean collapseSummaryPanel) {
    controllerDelegate.setCollapseSummaryPanel(collapseSummaryPanel);
  }

  public boolean isCollapseCustomerPanel() {
    return controllerDelegate.isCollapseCustomerPanel();
  }

  public void setCollapseCustomerPanel(boolean collapseCustomerPanel) {
    controllerDelegate.setCollapseCustomerPanel(collapseCustomerPanel);
  }

  public boolean isCollapseFiltersPanel() {
    return controllerDelegate.isCollapseFiltersPanel();
  }

  public void setCollapseFiltersPanel(boolean collapseFiltersPanel) {
    controllerDelegate.setCollapseFiltersPanel(collapseFiltersPanel);
  }

  /**
   * Get all the transporttypes translated in the correct language.
   * 
   * @return The list of transporttypes.
   */
  public List<TransportType> getAllTransportTypes() {
    List<TransportType> transportTypes = Arrays.asList(TransportType.values());
    localeUtil.fillInTransportTypeTranslations(transportTypes, messageBundle);
    return transportTypes;
  }

  public boolean isCollapseResultPanel() {
    return controllerDelegate.isCollapseResultPanel();
  }

  public void setCollapseResultPanel(boolean collapseResultPanel) {
    controllerDelegate.setCollapseResultPanel(collapseResultPanel);
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public boolean isCollapseOptionsPanel() {
    return controllerDelegate.isCollapseOptionsPanel();
  }

  public void setCollapseOptionsPanel(boolean collapseOptionsPanel) {
    controllerDelegate.setCollapseOptionsPanel(collapseOptionsPanel);
  }

  public boolean isShowCustomerContacts() {
    return quotationQuery.getCustomer() != null;
  }

  public String getManualAddedEmail() {
    return manualAddedEmail;
  }

  public void setManualAddedEmail(String manualAddedEmail) {
    this.manualAddedEmail = manualAddedEmail;
  }

  public void showEditRecipientsDialog() {
    controllerDelegate.showEditRecipientsDialog();
  }

  public void hideEditRecipientsDialog() {
    controllerDelegate.hideEditRecipientsDialog();
  }

  public DualListModel<CustomerContact> getCustomerContacts() {
    return customerContacts;
  }

  public void setCustomerContacts(DualListModel<CustomerContact> customerContacts) {
    this.customerContacts = customerContacts;
  }

  /**
   * Gets all the selected customercontacts to send the created offerte to.
   * 
   * @return String representation of all customer contacts.
   */
  public String getSelectedCustomerContactsAsString() {
    if (customerContacts != null) {
      return CustomerUtil.getInstance().getCustomerContactsAsString(customerContacts.getTarget());
    }
    return "";
  }

  public CustomerService getCustomerService() {
    return customerService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
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
}
