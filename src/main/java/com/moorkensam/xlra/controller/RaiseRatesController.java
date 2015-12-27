package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RaiseRateFileService;
import com.moorkensam.xlra.service.RateFileService;

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

  private boolean collapseRaiseRatesGrid;

  private boolean collapseSummaryGrid;

  @PostConstruct
  public void initializeController() {
    resetState();
    rateFiles.setTarget(selectedRateFiles);
    showRaiseRatesPanel();
  }

  private void resetState() {
    rateFiles = new DualListModel<RateFile>();
    allRateFiles = rateFileService.getAllRateFiles();
    rateFiles.setSource(allRateFiles);
    selectedRateFiles = new ArrayList<RateFile>();
    refreshLogs();
  }

  private void refreshLogs() {
    logRecords = raiseRatesService.getRaiseRatesLogRecordsThatAreNotUndone();
  }

  public String onFlowProcess(FlowEvent event) {
    return event.getNewStep();
  }

  public void showSummaryPanel() {
    collapseRaiseRatesGrid = true;
    collapseSummaryGrid = false;
  }

  public void showRaiseRatesPanel() {
    collapseRaiseRatesGrid = false;
    collapseSummaryGrid = true;
  }

  public void raiseRates() {
    raiseRatesService.raiseRateFileRateLinesWithPercentage(rateFiles.getTarget(), percentage);
    MessageUtil.addMessage("Rates raised", "Succesfully raised rates for");
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

  public boolean isCollapseSummaryGrid() {
    return collapseSummaryGrid;
  }

  public void setCollapseSummaryGrid(boolean collapseSummaryGrid) {
    this.collapseSummaryGrid = collapseSummaryGrid;
  }

  public boolean isCollapseRaiseRatesGrid() {
    return collapseRaiseRatesGrid;
  }

  public void setCollapseRaiseRatesGrid(boolean collapseRaiseRatesGrid) {
    this.collapseRaiseRatesGrid = collapseRaiseRatesGrid;
  }
}
