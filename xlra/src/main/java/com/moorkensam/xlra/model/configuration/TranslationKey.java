package com.moorkensam.xlra.model.configuration;

/**
 * These are the translation keys used in the application. USe this in the excel
 * upload to mark the fields.
 * 
 * @author bas
 *
 */
public enum TranslationKey {

	IMPORT_FORM(false), EXPORT_FORM(false), EUR1_DOCUMENT(false), ADR_SURCHARGE(
			false), ADR_MINIMUM(false), SUPPL_TARIF(false), WACHT_TARIF(false), PAYMENT_TERMS(
			false), TRANSPORT_INS(false), GELD_DATE(false), RETOUR_REFUSED_DELIVERY(
			false), TARIF_FRANCO_HOUSE(false), TARIF_VALID_TO(false), TRANSPORT_INSURANCE(
			false), DIESEL_SURCHARGE(false), CHF_SURCHARGE(false), LDM(true), KILO(
			true), PALET(true);

	private boolean key;

	TranslationKey(boolean key) {
		this.key = key;
	}

	public boolean isKey() {
		return key;
	}

}
