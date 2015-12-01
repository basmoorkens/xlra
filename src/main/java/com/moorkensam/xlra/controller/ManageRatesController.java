package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.controller.util.RateUtil;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.IncoTermType;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean(name = "manageRatesController")
@ViewScoped
public class ManageRatesController {

	@Inject
	private RateFileService rateFileService;

	@Inject
	private UserSessionController sessionController;

	private TranslationUtil translationUtil;

	private ConditionFactory conditionFactory;

	private List<RateFile> rateFiles;

	private boolean collapseConditionsDetailGrid = true;

	private boolean collapseRateLinesDetailGrid = true;

	private boolean collapseZonesDetailGrid = true;

	private RateFile selectedRateFile;

	private List<String> columnHeaders = new ArrayList<String>();

	private boolean hasRateFileSelected = false;

	private TranslationKey keyToAdd;

	private boolean editable;

	@PostConstruct
	public void initializeController() {
		editable = sessionController.isAdmin();
		refreshRates();
		resetSelectedRateFile();
		translationUtil = new TranslationUtil();
		conditionFactory = new ConditionFactory();
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
		collapseZonesDetailGrid = true;
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
		updateRateFile();
	}

	public void onConditionRowEdit(RowEditEvent event) {
		Condition condition = (Condition) event.getObject();
		MessageUtil.addMessage(
				"Condition updated",
				"Updated " + condition.getTranslatedValue() + " to "
						+ condition.getValue());
		updateRateFile();
	}

	public boolean isNumericRateFileZone() {
		if (selectedRateFile != null) {
			return selectedRateFile.isNumericalZoneRateFile();
		}
		return false;
	}

	public boolean isAlphaNumericRateFileZone() {
		if (selectedRateFile != null) {
			return selectedRateFile.isAlphaNumericalZoneRateFile();
		}
		return false;
	}

	public void onZoneEdit(RowEditEvent event) {
		Zone zone = (Zone) event.getObject();
		MessageUtil.addMessage("Zone update", zone.getName()
				+ " successfully updated.");
		updateRateFile();
	}

	public void deleteZone(Zone zone) {
		selectedRateFile = rateFileService.deleteZone(zone);
	}

	public void deleteCondition(Condition condition) {
		MessageUtil.addMessage("condition removed", condition.getTranslatedValue()
				+ " was successfully removed.");
		selectedRateFile.getConditions().remove(condition);
		condition.setRateFile(null);
		updateRateFile();
	}

	public void fetchDetailsOfRatefile(SelectEvent event) {
		selectedRateFile = rateFileService.getFullRateFile(((RateFile) event
				.getObject()).getId());
		collapseConditionsDetailGrid = false;
		collapseRateLinesDetailGrid = false;
		collapseZonesDetailGrid = false;
		hasRateFileSelected = true;
		refreshRateLineColumns();
		translationUtil.fillInTranslations(selectedRateFile.getConditions());
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
			columnHeaders.add(rl.getZone().getName());
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
		Condition createCondition = conditionFactory.createCondition(keyToAdd,
				"");
		selectedRateFile.addCondition(createCondition);
		keyToAdd = null;
		updateRateFile();
	}

	private void updateRateFile() {
		selectedRateFile = rateFileService.updateRateFile(selectedRateFile);
		translationUtil.fillInTranslations(selectedRateFile.getConditions());
	}

	public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile() {
		return translationUtil
				.getAvailableTranslationKeysForSelectedRateFile(selectedRateFile);
	}

	public boolean isCollapseZonesDetailGrid() {
		return collapseZonesDetailGrid;
	}

	public void setCollapseZonesDetailGrid(boolean collapseZonesDetailGrid) {
		this.collapseZonesDetailGrid = collapseZonesDetailGrid;
	}

}
