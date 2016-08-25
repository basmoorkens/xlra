package com.moorkensam.xlra.controller;


import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.util.LocaleUtil;
import com.moorkensam.xlra.service.util.RateUtil;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ViewScoped
@ManagedBean
public class ExcelUploadController {

  @Inject
  private ExcelService excelService;

  @Inject
  private CountryService countryService;

  @ManagedProperty("#{localeController}")
  private LocaleController localeController;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private LocaleUtil localeUtil;

  private List<Country> countries;

  private RateFile ratefile;

  private FileUploadEvent event;

  private List<Measurement> measurements;

  private List<Kind> kindOfRates;

  private boolean showUpload = false;

  /**
   * Initialize logic for the controller.
   */
  @PostConstruct
  public void init() {
    ratefile = new RateFile();
    localeUtil = new LocaleUtil();
    ratefile.setTransportType(TransportType.EXPORT);
    setCountries(countryService.getAllCountries());
    measurements = getLocaleController().getMeasurements();
    kindOfRates = getLocaleController().getKinds();
  }

  /**
   * Handles the file upload event.
   * 
   * @param event The fileupload event.
   * @throws IOException Thrown when the file could not be uploaded.
   */
  public void handleFileUpload(FileUploadEvent event) throws IOException {
    this.setEvent(event);
    InputStream inputStream;
    inputStream = event.getFile().getInputstream();
    XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
    excelService.uploadRateFileExcel(ratefile, workBook);
  }

  public void goToUpload() {
    setShowUpload(true);
  }

  public RateFile getRatefile() {
    return ratefile;
  }

  public void setRatefile(RateFile ratefile) {
    this.ratefile = ratefile;
  }

  public List<Language> getLanguages() {
    return RateUtil.getLanguages();
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

  public boolean isShowUpload() {
    return showUpload;
  }

  public void setShowUpload(boolean showUpload) {
    this.showUpload = showUpload;
  }

  public FileUploadEvent getEvent() {
    return event;
  }

  public void setEvent(FileUploadEvent event) {
    this.event = event;
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

  /**
   * Get all the rateskinds and fill in the translation for the frontend
   * 
   * @return The list of translated ratekinds.
   */
  public List<Kind> getAllKinds() {
    List<Kind> kinds = Arrays.asList(Kind.values());
    getLocaleUtil().fillInRateKindTranslations(kinds, messageBundle);
    return kinds;
  }

  /**
   * Get all the measurements and fill in the translation for the frontend.
   * 
   * @return List of translated measurements
   */
  public List<Measurement> getAllMeasurements() {
    List<Measurement> measurements = Arrays.asList(Measurement.values());
    getLocaleUtil().fillInMeasurementTranslations(measurements, messageBundle);
    return measurements;
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

  public LocaleUtil getLocaleUtil() {
    return localeUtil;
  }

  public void setLocaleUtil(LocaleUtil localeUtil) {
    this.localeUtil = localeUtil;
  }

}
