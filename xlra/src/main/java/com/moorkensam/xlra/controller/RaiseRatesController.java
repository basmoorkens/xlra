package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RateFileService;

@ManagedBean
@ViewScoped
public class RaiseRatesController {

	@Inject
	private RateFileService rateFileService;

	private List<RateFile> allRateFiles;

	private DualListModel<RateFile> rateFiles;

	private List<RateFile> selectedRateFiles;

	private double percentage;

	private List<RaiseRatesRecord> logRecords;

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

	private void refreshLogs() {
		logRecords = rateFileService.getRaiseRatesLogRecordsThatAreNotUndone();
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public void raiseRates() {
		rateFileService.raiseRateFileRateLinesWithPercentage(
				rateFiles.getTarget(), percentage);
		MessageUtil.addMessage("Rates raised", "Succesfully raised rates for");
	}

	public void undoLatestRaiseRates() {
		rateFileService.undoLatestRatesRaise();
		refreshLogs();
		MessageUtil.addMessage("Rates raised",
				"Succesfully undid latest rates raise.");
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
