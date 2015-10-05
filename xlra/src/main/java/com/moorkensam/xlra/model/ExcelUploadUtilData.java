package com.moorkensam.xlra.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUploadUtilData {
	private Map<Integer, String> zoneMap = new HashMap<Integer, String>();
	private Map<Integer, List<String>> zoneValuesMap = new HashMap<Integer, List<String>>();
	private Map<Double, List<RateLineExcelImportDTO>> ratesMap = new HashMap<Double, List<RateLineExcelImportDTO>>();
	private Map<String, List<String>> termsMap = new HashMap<String, List<String>>();
	private double selectedMeasurement;
	private String selectedTerm;

	public String getSelectedTerm() {
		return selectedTerm;
	}

	public void setSelectedTerm(String selectedTerm) {
		this.selectedTerm = selectedTerm;
	}

	public double getSelectedMeasurement() {
		return selectedMeasurement;
	}

	public void setSelectedMeasurement(double selectedMeasurement) {
		this.selectedMeasurement = selectedMeasurement;
	}

	public Map<String, List<String>> getTermsMap() {
		return termsMap;
	}

	public void setTermsMap(Map<String, List<String>> termsMap) {
		this.termsMap = termsMap;
	}

	public Map<Double, List<RateLineExcelImportDTO>> getRatesMap() {
		return ratesMap;
	}

	public void setRatesMap(Map<Double, List<RateLineExcelImportDTO>> ratesMap) {
		this.ratesMap = ratesMap;
	}

	public Map<Integer, List<String>> getZoneValuesMap() {
		return zoneValuesMap;
	}

	public void setZoneValuesMap(Map<Integer, List<String>> zoneValuesMap) {
		this.zoneValuesMap = zoneValuesMap;
	}

	public Map<Integer, String> getZoneMap() {
		return zoneMap;
	}

	public void setZoneMap(Map<Integer, String> zoneMap) {
		this.zoneMap = zoneMap;
	}
}