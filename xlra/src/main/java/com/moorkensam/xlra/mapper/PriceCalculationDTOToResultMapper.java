package com.moorkensam.xlra.mapper;

import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public class PriceCalculationDTOToResultMapper {

	public void map(final PriceCalculationDTO priceDTO, PriceResultDTO resultDTO) {
		resultDTO.setBasePrice("Basis prijs: " + priceDTO.getBasePrice()
				+ "<br />");
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE_KEY)) {
			resultDTO.setDieselSurcharge("Diesel toeslag: "
					+ priceDTO.getDieselPrice() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE_KEY)) {
			resultDTO.setChfSurcharge("Zwitserse frank toeslag: "
					+ priceDTO.getChfPrice() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.IMPORT_FORM_KEY)) {
			resultDTO.setImportForm("Import formaliteiten: "
					+ priceDTO.getImportFormalities() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.EXPORT_FORM_KEY)) {
			resultDTO.setExportForm("Export formaliteiten: "
					+ priceDTO.getExportFormalities() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE_KEY)) {
			resultDTO.setAdrCalc("ADR toeslag: "
					+ priceDTO.getResultingPriceSurcharge() + "<br />");
		}
		resultDTO.setTotalPrice("<h3>Totale prijs: " + priceDTO.getFinalPrice()
				+ "</h3><br />");
	}

}
