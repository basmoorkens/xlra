package com.moorkensam.xlra.model.translation;

/**
 * These are the translation keys used in the application. USe this in the excel upload to mark the
 * fields.
 * 
 * @author bas
 *
 */
public enum TranslationKey {


  ADR_MINIMUM,
  ADR_SURCHARGE,
  CHF_SURCHARGE(false),
  DIESEL_SURCHARGE(false),
  EUR1_DOCUMENT,
  EXPORT_FORM,
  IMPORT_FORM,
  KILO(false),
  LDM(false),
  PALET(false),
  PAYMENT_TERMS,
  RETOUR_REFUSED_DELIVERY,
  SUPPL_TARIF,
  TARIF_FRANCO_HOUSE,
  TARIF_VALID_TO,
  TRANSPORT_INSURANCE,
  WACHT_TARIF;

  private boolean showInApplication;

  TranslationKey(boolean show) {
    showInApplication = show;
  }

  TranslationKey() {
    showInApplication = true;
  }

  public boolean isShowInApplication() {
    return showInApplication;
  }
}
