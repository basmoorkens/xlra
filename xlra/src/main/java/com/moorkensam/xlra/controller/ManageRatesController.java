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
public class ManageRatesController {

	@Inject
	private RateFileService rateFileService;

	private String rateSearchTerm;

	private List<RateFile> rateFiles;

	@PostConstruct
	public void initializeController() {
		refreshRates();
	}

	private void refreshRates() {
		rateFiles = rateFileService.getAllRateFiles();
	}

	public String getRateSearchTerm() {
		return rateSearchTerm;
	}

	public void setRateSearchTerm(String rateSearchTerm) {
		this.rateSearchTerm = rateSearchTerm;
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
}
