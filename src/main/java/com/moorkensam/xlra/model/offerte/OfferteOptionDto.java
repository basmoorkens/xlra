package com.moorkensam.xlra.model.offerte;

import java.io.Serializable;

import com.moorkensam.xlra.model.translation.Translatable;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class OfferteOptionDto implements Serializable, Translatable {

  private static final long serialVersionUID = -4073642999193664486L;

  private TranslationKey key;

  private boolean selected;

  private boolean showToCustomer;

  private boolean calculationOption;

  private String value;

  private String i8nKey;

  private String translatedKey;

  public String getTranslatedKey() {
    return translatedKey;
  }

  public void setTranslatedKey(String translatedKey) {
    this.translatedKey = translatedKey;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public TranslationKey getKey() {
    return key;
  }

  public void setKey(TranslationKey key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getI8nKey() {
    return i8nKey;
  }

  public void setI8nKey(String i8nKey) {
    this.i8nKey = i8nKey;
  }

  public boolean isCalculationOption() {
    return calculationOption;
  }

  public void setCalculationOption(boolean calculationOption) {
    this.calculationOption = calculationOption;
  }

  public boolean isShowToCustomer() {
    return showToCustomer;
  }

  public void setShowToCustomer(boolean showToCustomer) {
    this.showToCustomer = showToCustomer;
  }
}
