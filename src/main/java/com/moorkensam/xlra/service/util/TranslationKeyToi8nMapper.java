package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.translation.TranslationKey;

public class TranslationKeyToi8nMapper {

	public String map(TranslationKey key) {
		switch (key) {
		case ADR_MINIMUM:
			return "calculation.fulldetail.adr.minimum";
		case ADR_SURCHARGE:
			return "calculation.fulldetail.adr.surcharge";
		case CHF_SURCHARGE:
			return "calculation.fulldetail.swiss.franc.surcharge";
		case DIESEL_SURCHARGE:
			return "calculation.fulldetail.diesel.surcharge";
		case EUR1_DOCUMENT:
			return "calculation.fulldetail.eur1.document";
		case EXPORT_FORM:
			return "calculation.fulldetail.export.formalities";
		case IMPORT_FORM:
			return "calculation.fulldetail.import.formalities";
		case KILO:
			return "kilo";
		case LDM:
			return "ldm";
		case PALET:
			return "palet";
		case PAYMENT_TERMS:
			return "calculation.fulldetail.payment.terms";
		case RETOUR_REFUSED_DELIVERY:
			return "calculation.fulldetail.retour.refused.delivery";
		case TARIF_FRANCO_HOUSE:
			return "calculation.fulldetail.tarif.franco.house";
		case TARIF_VALID_TO:
			return "calculation.fulldetail.valid.to";
		case TRANSPORT_INSURANCE:
			return "calculation.fulldetail.transport.insurance";
		case WACHT_TARIF:
			return "calculation.fulldetail.wait.tarif";
		case SUPPL_TARIF:
			return "calculation.fulldetail.suppl.tarif";
		default:
			return "";
		}
	}

}
