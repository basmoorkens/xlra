package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RaiseRateFileService;
import com.moorkensam.xlra.service.RateFileService;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class RaiseRatesController {

  @Inject
  private RateFileService rateFileService;

  @Inject
  private RaiseRateFileService raiseRatesService;

  private List<RateFile> allRateFiles;

  private DualListModel<RateFile> rateFiles;

  private List<RateFile> selectedRateFiles;

  private double percentage;

  private List<RaiseRatesRecord> logRecords;


  /**
   * Inits the controller.
   */
  @PostConstruct
  public void initializeController() {
    resetState();
    rateFiles.setTarget(selectedRateFiles);
  }

  private void resetState() {
    rateFiles = new DualListModel<RateFile>();
    allRateFiles = rateFileService.getAllRateFiles();
    rateFiles.setSource(allRateFiles);
    selectedRateFiles = new ArrayList<RateFile>();
    refreshLogs();
  }

  /**
   * cancel the creation of a raise rate files.
   */
  public void cancel() {
    percentage = 0d;
    resetState();
    hideAddDialog();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('raiseRatesDialog').hide();");
  }

  public void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('raiseRatesDialog').show();");
  }

  private void refreshLogs() {
    logRecords = raiseRatesService.getRaiseRatesLogRecordsThatAreNotUndone();
  }

  /**
   * Raise the selected ratefiles by the given percentage.
   */
  public void raiseRates() {
    if (validRaise()) {
      raiseRatesService.raiseRateFileRateLinesWithPercentage(rateFiles.getTarget(), percentage);
      hideAddDialog();
      MessageUtil.addMessage("Rates raised", "Succesfully raised rates for");
      resetState();
    } else {
      showAddDialog();
    }
  }

  private boolean validRaise() {
    if (rateFiles.getTarget() == null || rateFiles.getTarget().size() == 0) {
      MessageUtil.addErrorMessage("No rates selected", "Please select ratefiles to raise.");
      return false;
    }
    if (percentage == 0.00d) {
      MessageUtil.addErrorMessage("0.00 is an invalid percentage",
          "A raise of 0.00 doesnt do anything.");
      return false;
    }
    return true;
  }

  /**
   * Undo the last raise rates.
   */
  public void undoLatestRaiseRates() {
    raiseRatesService.undoLatestRatesRaise();
    refreshLogs();
    MessageUtil.addMessage("Rates raised", "Succesfully undid latest rates raise.");
  }

  public List<RateFile> getAllRateFiles() {
    return allRateFiles;
  }

  public void setAllRateFiles(List<RateFile> allRateFiles) {
    this.allRateFiles = allRateFiles;
  }

  public List<RateFile> getSelectedRateFiles() {
    return selectedRateFiles;
  }

  public void setSelectedRateFiles(List<RateFile> selectedRateFiles) {
    this.selectedRateFiles = selectedRateFiles;
  }

  public DualListModel<RateFile> getRateFiles() {
    return rateFiles;
  }

  public void setRateFiles(DualListModel<RateFile> rateFiles) {
    this.rateFiles = rateFiles;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public List<RaiseRatesRecord> getLogRecords() {
    return logRecords;
  }

  public void setLogRecords(List<RaiseRatesRecord> logRecords) {
    this.logRecords = logRecords;
  }
}
