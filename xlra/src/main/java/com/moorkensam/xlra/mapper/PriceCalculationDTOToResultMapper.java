package com.moorkensam.xlra.mapper;

import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public class PriceCalculationDTOToResultMapper {

	public void map(final PriceCalculationDTO priceDTO, PriceResultDTO resultDTO) {
		resultDTO.setBasePrice("Basis prijs: " + priceDTO.getBasePrice());
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE_KEY)) {
			resultDTO.setDieselSurcharge("Diesel toeslag: "
					+ priceDTO.getDieselPrice());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE_KEY)) {
			resultDTO.setChfSurcharge("Zwitserse frank toeslag: "
					+ priceDTO.getChfPrice());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.IMPORT_FORM_KEY)) {
			resultDTO.setImportForm("Import formaliteiten: "
					+ priceDTO.getImportFormalities());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.EXPORT_FORM_KEY)) {
			resultDTO.setExportForm("Export formaliteiten: "
					+ priceDTO.getExportFormalities());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE_KEY)) {
			resultDTO.setAdrCalc("ADR toeslag: "
					+ priceDTO.getResultingPriceSurcharge());
		}
	}

}
