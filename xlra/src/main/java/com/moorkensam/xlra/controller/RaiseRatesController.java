package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RateFileService;

@ManagedBean
@ViewScoped
public class RaiseRatesController {

	@Inject
	private RateFileService rateFileService;

	private List<RateFile> allRateFiles;

	private List<RateFile> selectedRateFiles = new ArrayList<RateFile>();

	@PostConstruct
	public void initializeController() {
		allRateFiles = rateFileService.getAllRateFiles();
	}

	public void selectAllRateFiles() {
		selectedRateFiles = new ArrayList<RateFile>();
		for (RateFile rf : allRateFiles) {
			selectedRateFiles.add(rf);
		}
	}

	public void unSelectAllRateFiles() {
		selectedRateFiles = new ArrayList<RateFile>();
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
}
