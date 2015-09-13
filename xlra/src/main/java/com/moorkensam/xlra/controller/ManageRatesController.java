package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.moorkensam.xlra.model.rate.IncoTermType;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RateFileService;

@ManagedBean(name = "manageRatesController")
@ViewScoped
public class ManageRatesController {

	@Inject
	private RateFileService rateFileService;

	private List<RateFile> rateFiles;

	private boolean collapseDetailGrid = true;

	private RateFile selectedRateFile;

	@PostConstruct
	public void initializeController() {
		refreshRates();
		resetSelectedRateFile();
	}

	private void resetSelectedRateFile() {
		selectedRateFile = new RateFile();
	}

	private void resetPage() {
		resetSelectedRateFile();
		collapseDetailGrid = true;
		completeRateName("");
	}

	public void saveConditions() {
		selectedRateFile.setCondition(rateFileService
				.updateTermsAndConditions(selectedRateFile.getCondition()));
		resetPage();
	}

	public void fetchDetailsOfRatefile(SelectEvent event) {
		selectedRateFile = rateFileService.getFullRateFile(((RateFile) event
				.getObject()).getId());
		collapseDetailGrid = false;
	}

	private void refreshRates() {
		rateFiles = rateFileService.getAllRateFiles();
	}

	public List<RateFile> completeRateName(String input) {
		List<RateFile> filteredRateFiles = new ArrayList<RateFile>();
		for (RateFile rf : rateFiles) {
			if (rf.getName().toLowerCase().contains(input.toLowerCase())) {
				filteredRateFiles.add(rf);
			}
		}
		return filteredRateFiles;
	}

	public List<RateFile> getRateFiles() {
		return rateFiles;
	}

	public void setRateFiles(List<RateFile> rateFiles) {
		this.rateFiles = rateFiles;
	}

	public RateFileService getRateFileService() {
		return rateFileService;
	}

	public void setRateFileService(RateFileService rateFileService) {
		this.rateFileService = rateFileService;
	}

	public RateFile getSelectedRateFile() {
		return selectedRateFile;
	}

	public void setSelectedRateFile(RateFile selectedRateFile) {
		this.selectedRateFile = selectedRateFile;
	}

	public boolean isCollapseDetailGrid() {
		return collapseDetailGrid;
	}

	public void setCollapseDetailGrid(boolean collapseDetailGrid) {
		this.collapseDetailGrid = collapseDetailGrid;
	}

	public List<IncoTermType> getIncoTermTypes() {
		return Arrays.asList(IncoTermType.values());
	}

	public RateFile getRateFileById(long id) {
		for (RateFile rf : rateFiles) {
			if (rf.getId() == id) {
				return rf;
			}
		}
		return null;
	}

}
