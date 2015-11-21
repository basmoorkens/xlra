package com.moorkensam.xlra.mapper;

import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.translation.TranslationKey;

/**
 * This class generates the parameters for the offerte email based on the
 * pricecalculationDTO object.
 * 
 * @author bas
 *
 */
public class OfferteEmailParameterGenerator {

	public void fillInParameters(final PriceCalculation priceDTO,
			PriceResultDTO resultDTO, String offerteReference) {
		resultDTO.setBasePrice("Basis prijs: " + priceDTO.getBasePrice()
				+ "<br />");
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE)) {
			resultDTO.setDieselSurcharge("Diesel toeslag: "
					+ priceDTO.getDieselPrice() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE)) {
			resultDTO.setChfSurcharge("Zwitserse frank toeslag: "
					+ priceDTO.getChfPrice() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.IMPORT_FORM)) {
			resultDTO.setImportForm("Import formaliteiten: "
					+ priceDTO.getImportFormalities() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.EXPORT_FORM)) {
			resultDTO.setExportForm("Export formaliteiten: "
					+ priceDTO.getExportFormalities() + "<br />");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE)) {
			resultDTO.setAdrCalc("ADR toeslag: "
					+ priceDTO.getResultingPriceSurcharge() + "<br />");
		}
		resultDTO.setOfferteReference(offerteReference);
		resultDTO.setTotalPrice("<h3>Totale prijs: " + priceDTO.getFinalPrice()
				+ "</h3><br />");
	}

}
