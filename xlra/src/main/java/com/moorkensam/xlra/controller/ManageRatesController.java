package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import com.moorkensam.xlra.controller.util.RateUtil;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.IncoTermType;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean(name = "manageRatesController")
@ViewScoped
public class ManageRatesController {

	@Inject
	private RateFileService rateFileService;

	private List<RateFile> rateFiles;

	private boolean collapseConditionsDetailGrid = true;

	private boolean collapseRateLinesDetailGrid = true;

	private RateFile selectedRateFile;

	private List<String> columnHeaders = new ArrayList<String>();

	private boolean hasRateFileSelected = false;

	private TranslationKey keyToAdd;

	@PostConstruct
	public void initializeController() {
		refreshRates();
		resetSelectedRateFile();
	}

	private void resetSelectedRateFile() {
		selectedRateFile = new RateFile();
	}

	public void saveRateFile() {
		rateFileService.updateRateFile(selectedRateFile);
		resetPage();
	}

	private void resetPage() {
		resetSelectedRateFile();
		collapseConditionsDetailGrid = true;
		collapseRateLinesDetailGrid = true;
		completeRateName("");
		hasRateFileSelected = false;
		columnHeaders = new ArrayList<String>();
	}

	/**
	 * 
	 * @param event
	 */
	public void onRateLineCellEdit(CellEditEvent event) {
		RateUtil.onRateLineCellEdit(event);
	}

	public void onConditionCellEdit(CellEditEvent event) {
		RateUtil.onConditionCellEdit(event);
	}

	public void deleteCondition(Condition condition) {
		selectedRateFile.getConditions().remove(condition);
		condition.setRateFile(null);
	}

	public void fetchDetailsOfRatefile(SelectEvent event) {
		selectedRateFile = rateFileService.getFullRateFile(((RateFile) event
				.getObject()).getId());
		collapseConditionsDetailGrid = false;
		collapseRateLinesDetailGrid = false;
		hasRateFileSelected = true;
		refreshRateLineColumns();
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

	public List<IncoTermType> getIncoTermTypes() {
		return RateUtil.getIncoTermTypes();
	}

	public RateFile getRateFileById(long id) {
		for (RateFile rf : rateFiles) {
			if (rf.getId() == id) {
				return rf;
			}
		}
		return null;
	}

	public boolean isCollapseConditionsDetailGrid() {
		return collapseConditionsDetailGrid;
	}

	public void setCollapseConditionsDetailGrid(
			boolean collapseConditionsDetailGrid) {
		this.collapseConditionsDetailGrid = collapseConditionsDetailGrid;
	}

	public boolean isCollapseRateLinesDetailGrid() {
		return collapseRateLinesDetailGrid;
	}

	public void setCollapseRateLinesDetailGrid(
			boolean collapseRateLinesDetailGrid) {
		this.collapseRateLinesDetailGrid = collapseRateLinesDetailGrid;
	}

	public boolean isHasRateFileSelected() {
		return hasRateFileSelected;
	}

	public void setHasRateFileSelected(boolean hasRateFileSelected) {
		this.hasRateFileSelected = hasRateFileSelected;
	}

	private List<String> refreshRateLineColumns() {
		columnHeaders = new ArrayList<String>();
		for (RateLine rl : selectedRateFile.getRateLines()) {
			columnHeaders.add(rl.getZone());
		}
		return columnHeaders;
	}

	public String getMeasurementTitle() {
		if (selectedRateFile.getId() != 0) {
			return selectedRateFile.getMeasurement().getDescription();
		} else {
			return "Measurement";
		}
	}

	public List<String> getColumnHeaders() {
		return columnHeaders;
	}

	public void setColumnHeaders(List<String> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}

	public TranslationKey getKeyToAdd() {
		return keyToAdd;
	}

	public void setKeyToAdd(TranslationKey keyToAdd) {
		this.keyToAdd = keyToAdd;
	}

	public void createConditionForSelectedTranslationKey(ActionEvent event) {
		Condition c = new Condition();
		c.setValue("");
		c.setRateFile(selectedRateFile);
		selectedRateFile.getConditions().add(c);
		keyToAdd = null;
	}

	public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
		List<TranslationKey> allKeys = TranslationUtil.getTranslationsNotKey();
		if (selectedRateFile != null
				&& selectedRateFile.getConditions() != null
				&& !selectedRateFile.getConditions().isEmpty()) {
			List<TranslationKey> usedKeys = new ArrayList<TranslationKey>();
			for (Condition c : selectedRateFile.getConditions()) {
				usedKeys.add(c.getTranslation().getTranslationKey());
			}
			List<TranslationKey> result = new ArrayList<TranslationKey>();
			for (TranslationKey key : allKeys) {
				if (!usedKeys.contains(key)) {
					result.add(key);
				}
			}
			return result;
		}
		return allKeys;
	}
}
