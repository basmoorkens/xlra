package com.moorkensam.xlra.dto;

public class PriceResultDTO {

	private String basePrice;

	private String importForm;

	private String exportForm;

	private String adrCalc;

	private String dieselSurcharge;

	private String chfSurcharge;

	private String totalPrice;

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getAdrCalc() {
		return adrCalc;
	}

	public void setAdrCalc(String adrCalc) {
		this.adrCalc = adrCalc;
	}

	public String getExportForm() {
		return exportForm;
	}

	public void setExportForm(String exportForm) {
		this.exportForm = exportForm;
	}

	public String getImportForm() {
		return importForm;
	}

	public void setImportForm(String importForm) {
		this.importForm = importForm;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getDetailedCalculation() {
		String fullDetail = basePrice;
		if (dieselSurcharge != null) {
			fullDetail += dieselSurcharge;
		}
		if (chfSurcharge != null) {
			fullDetail += chfSurcharge;
		}
		if (importForm != null) {
			fullDetail += importForm;
		}
		if (exportForm != null) {
			fullDetail += exportForm;
		}
		if (adrCalc != null) {
			fullDetail += adrCalc;
		}
		return fullDetail;
	}

	public String getChfSurcharge() {
		return chfSurcharge;
	}

	public void setChfSurcharge(String chfSurcharge) {
		this.chfSurcharge = chfSurcharge;
	}

	public String getDieselSurcharge() {
		return dieselSurcharge;
	}

	public void setDieselSurcharge(String dieselSurcharge) {
		this.dieselSurcharge = dieselSurcharge;
	}

}
