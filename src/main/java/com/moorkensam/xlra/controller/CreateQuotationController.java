package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
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
import com.moorkensam.xlra.service.util.TranslationUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
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

  private TranslationUtil translationUtil;

  private FileService fileService;

  private List<Customer> customers;

  private QuotationQuery quotationQuery;

  private QuotationResult quotationResult;

  private Customer customerToAdd;

  private boolean renderAddCustomerGrid;

  private List<Country> allCountries;

  private boolean collapseCustomerPanel;

  private boolean collapseFiltersPanel;

  private boolean collapseSummaryPanel;

  private boolean collapseOptionsPanel;

  private boolean collapseResultPanel;

  /**
   * Init logic for the controller.
   */
  @PostConstruct
  public void init() {
    allCountries = countryService.getAllCountries();
    initializeNewQuotationQuery();
    refreshCustomers();
    initializeNewCustomer();
    fileService = new FileServiceImpl();
    translationUtil = new TranslationUtil();
    showCustomerPanel();
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
    FacesContext
        .getCurrentInstance()
        .getExternalContext()
        .redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
                + "/views/user/offerteOverview.xhtml?offerteKey="
                + quotationResult.getOfferteUniqueIdentifier());
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

  private void refreshCustomers() {
    setCustomers(customerService.getAllCustomers());
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
      getQuotationQuery()
          .setCustomer(customerService.createCustomerAndReturnManaged(customerToAdd));
      MessageUtil.addMessage("Customer created", "Created customer " + customerToAdd.getName());
      refreshCustomers();
      renderAddCustomerGrid = false;
      customerToAdd = new Customer();
    } catch (XlraValidationException exc) {
      MessageUtil.addErrorMessage("Invalid customer data", exc.getBusinessException());
    }
  }

  public List<Language> getAllLanguages() {
    return Arrays.asList(Language.values());
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
    collapseCustomerPanel = false;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the ratefilter section.
   */
  public void showRateFilterPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = false;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the options section.
   */
  public void showOptionsPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = false;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the summary section.
   */
  public void showSummaryPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = false;
    collapseResultPanel = true;
  }

  /**
   * Show the result section.
   */
  public void showResultPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = false;
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
        MessageUtil.addErrorMessage("Unknown exception",
            "An unexpected exception occurred, please contact the system admin.");
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
      showSummaryPanel();
    } catch (RateFileException e) {
      showRateFileError(e);
    }
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
    response.reset();
    response.setHeader("Content-Type", "application/pdf");
    OutputStream responseOutputStream = response.getOutputStream();
    File generatedPdfFromDisk =
        fileService.getOffertePdfFromFileSystem(quotationResult.getPdfFileName());
    InputStream pdfInputStream = new FileInputStream(generatedPdfFromDisk);
    byte[] bytesBuffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
      responseOutputStream.write(bytesBuffer, 0, bytesRead);
    }

    responseOutputStream.flush();
    pdfInputStream.close();
    responseOutputStream.close();
    facesContext.responseComplete();
  }

  /**
   * Submit an offerte for persistence.
   */
  public void submitOfferte() {
    try {
      quotationService.submitQuotationResult(quotationResult);
      MessageUtil.addMessage("Offerte successfully send", "The offerte was successfully send to "
          + quotationResult.getEmailResult().getRecipientsAsString());
      showResultPanel();
    } catch (RateFileException re2) {
      showRateFileError(re2);
    } catch (EJBException e) {
      if (e.getCausedByException() instanceof RateFileException) {
        RateFileException re = (RateFileException) e.getCausedByException();
        showRateFileError(re);
      } else {
        MessageUtil.addErrorMessage("Unknown exception",
            "An unexpected exception occurred, please contact the system admin.");
      }
    }
  }

  private void showRateFileError(RateFileException re) {
    MessageUtil.addErrorMessage("Unexpected error whilst processing quotation request.",
        re.getBusinessException());
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
    List<Customer> filteredCustomers = new ArrayList<Customer>();
    if (customers != null) {
      for (Customer baseCustomer : customers) {
        if (baseCustomer.getName().toLowerCase().contains(input.toLowerCase())) {
          filteredCustomers.add(baseCustomer);
        }
      }
    }
    return filteredCustomers;
  }

  public List<Measurement> getAllMeasurements() {
    return Arrays.asList(Measurement.values());
  }

  public List<Kind> getAllKinds() {
    return Arrays.asList(Kind.values());
  }

  public List<Customer> getCustomers() {
    return customers;
  }

  public void setCustomers(List<Customer> customers) {
    this.customers = customers;
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
    return collapseSummaryPanel;
  }

  public void setCollapseSummaryPanel(boolean collapseSummaryPanel) {
    this.collapseSummaryPanel = collapseSummaryPanel;
  }

  public boolean isCollapseCustomerPanel() {
    return collapseCustomerPanel;
  }

  public void setCollapseCustomerPanel(boolean collapseCustomerPanel) {
    this.collapseCustomerPanel = collapseCustomerPanel;
  }

  public boolean isCollapseFiltersPanel() {
    return collapseFiltersPanel;
  }

  public void setCollapseFiltersPanel(boolean collapseFiltersPanel) {
    this.collapseFiltersPanel = collapseFiltersPanel;
  }

  public List<TransportType> getAllTransportTypes() {
    return Arrays.asList(TransportType.values());
  }

  public boolean isCollapseResultPanel() {
    return collapseResultPanel;
  }

  public void setCollapseResultPanel(boolean collapseResultPanel) {
    this.collapseResultPanel = collapseResultPanel;
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public boolean isCollapseOptionsPanel() {
    return collapseOptionsPanel;
  }

  public void setCollapseOptionsPanel(boolean collapseOptionsPanel) {
    this.collapseOptionsPanel = collapseOptionsPanel;
  }

}
