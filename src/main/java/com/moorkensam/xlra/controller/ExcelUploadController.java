package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.util.RateUtil;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ViewScoped
@ManagedBean
public class ExcelUploadController {

  @Inject
  private ExcelService excelService;

  @Inject
  private CountryService countryService;

  private List<Country> countries;

  private RateFile ratefile;

  private FileUploadEvent event;

  private List<Measurement> measurements = Arrays.asList(Measurement.values());

  private List<Kind> kindOfRates = Arrays.asList(Kind.values());

  private boolean showUpload = false;

  /**
   * Initialize logic for the controller.
   */
  @PostConstruct
  public void init() {
    ratefile = new RateFile();
    ratefile.setTransportType(TransportType.EXPORT);
    setCountries(countryService.getAllCountries());
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

  public List<TransportType> getAllTransportTypes() {
    return Arrays.asList(TransportType.values());
  }

  public FileUploadEvent getEvent() {
    return event;
  }

  public void setEvent(FileUploadEvent event) {
    this.event = event;
  }

}
