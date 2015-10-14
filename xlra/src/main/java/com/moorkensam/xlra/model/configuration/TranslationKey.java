package com.moorkensam.xlra.model.configuration;

/**
 * These are the translation keys used in the application. USe this in the excel
 * upload to mark the fields.
 * 
 * @author bas
 *
 */
public enum TranslationKey {

			IMPORT_FORM(false), IMPORT_FORM_KEY(true), 
			EXPORT_FORM(false), EXPORT_FORM_KEY(true), 
			EUR1_DOCUMENT(false), EUR1_DOCUMENT_KEY(true), 
			ADR_SURCHARGE(false), ADR_SURCHARGE_KEY(true), 
			ADR_MINIMUM(false), ADR_MINIMUM_KEY(true), 
			SUPPL_TARIF(false), SUPPL_TARIF_KEY(true),
			WACHT_TARIF(false), WACHT_TARIF_KEY(true),
			PAYMENT_TERMS(false), PAYMENT_TERMS_KEY(true),
			TRANSPORT_INS(false), TRANSPORT_INS_KEY(true), GELD_DATE(false), 
			GELD_DATE_KEY(true), RETOUR_REFUSED_DELIVERY(false), 
			RETOUR_REFUSED_DELIVERY_KEY(true), TARIF_FRANCO_HOUSE(false), TARIF_FRANCO_HOUSE_KEY(true), 
			TARIF_VALID_TO(false), TARIF_VALID_TO_KEY(true), 
			TRANSPORT_INSURANCE(false), TRANSPORT_INSURANCE_KEY(true);

	private boolean key;

	TranslationKey(boolean key) {
		this.key = key;
	}

	public boolean isKey() {
		return key;
	}

}
