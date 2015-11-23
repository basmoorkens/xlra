package com.moorkensam.xlra.mapper;

import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.translation.TranslationKey;

/**
 * This class generates the parameters for the offerte email based on the
 * pricecalculationDTO object.
 * 
 * @author bas
 *
 */
public class PriceCalculationToHtmlConverter {

	public String generateHtmlFullDetailCalculation(
			final PriceCalculation priceDTO, String offerteReference) {
		StringBuilder detailCalculationBuilder = new StringBuilder();

		String basePrice = "Basis prijs: " + priceDTO.getBasePrice();
		detailCalculationBuilder.append(basePrice);
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE)) {
			detailCalculationBuilder.append("Diesel toeslag: "
					+ priceDTO.getDieselPrice());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE)) {
			detailCalculationBuilder.append("Zwitserse frank toeslag: "
					+ priceDTO.getChfPrice());
		}
		if (priceDTO.getAppliedOperations()
				.contains(TranslationKey.IMPORT_FORM)) {
			detailCalculationBuilder.append("Import formaliteiten: "
					+ priceDTO.getImportFormalities());
		}
		if (priceDTO.getAppliedOperations()
				.contains(TranslationKey.EXPORT_FORM)) {
			detailCalculationBuilder.append("Export formaliteiten: "
					+ priceDTO.getExportFormalities());
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE)) {
			detailCalculationBuilder.append("ADR toeslag: "
					+ priceDTO.getResultingPriceSurcharge());
		}
		detailCalculationBuilder.append("<h3>Totale prijs: "
				+ priceDTO.getFinalPrice() + "</h3><br />");
		return detailCalculationBuilder.toString();
	}

}
