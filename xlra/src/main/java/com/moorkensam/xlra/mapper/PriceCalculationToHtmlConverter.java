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
		detailCalculationBuilder.append("<table>");
		detailCalculationBuilder.append("<tr><td>");
		detailCalculationBuilder.append("Basis prijs:</td><td> "
				+ priceDTO.getBasePrice() + "</td></tr>");
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE)) {
			detailCalculationBuilder.append("<tr><td>Diesel toeslag: </td><td>"
					+ priceDTO.getDieselPrice() + "</td></tr>");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE)) {
			detailCalculationBuilder
					.append("<tr><td>Zwitserse frank toeslag: </td><td>"
							+ priceDTO.getChfPrice() + "</td></tr>");
		}
		if (priceDTO.getAppliedOperations()
				.contains(TranslationKey.IMPORT_FORM)) {
			detailCalculationBuilder
					.append("<tr><td>Import formaliteiten: </td><td>"
							+ priceDTO.getImportFormalities() + "</td></tr>");
		}
		if (priceDTO.getAppliedOperations()
				.contains(TranslationKey.EXPORT_FORM)) {
			detailCalculationBuilder
					.append("<tr><td>Export formaliteiten: </td><td>"
							+ priceDTO.getExportFormalities() + "</td></tr>");
		}
		if (priceDTO.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE)) {
			detailCalculationBuilder.append("<tr><td>ADR toeslag: </td><td>"
					+ priceDTO.getResultingPriceSurcharge() + "</td></tr>");
		}
		detailCalculationBuilder.append("</table>");
		detailCalculationBuilder.append("<h3>Totale prijs: "
				+ priceDTO.getFinalPrice() + "</h3><br />");

		return detailCalculationBuilder.toString();
	}

}
