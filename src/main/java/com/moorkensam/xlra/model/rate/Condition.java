package com.moorkensam.xlra.model.rate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.configuration.TranslationForLanguage;
import com.moorkensam.xlra.model.translation.Translatable;
import com.moorkensam.xlra.model.translation.TranslationKey;

@Entity
@Table(name = "conditions")
public class Condition extends BaseEntity implements Translatable {

  private static final long serialVersionUID = 7814328305384417198L;

  private String value;

  @Enumerated(EnumType.STRING)
  private TranslationKey conditionKey;

  @Column(name = "offerte_standard_selected")
  private boolean standardSelected;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ConditionType conditionType;

  @Enumerated(EnumType.STRING)
  @Column(name = "calculation_value_type")
  private CalculationValueType calculationValueType;

  @ManyToOne
  @JoinColumn(name = "rateFileId")
  private RateFile rateFile;

  @ElementCollection
  @CollectionTable(name = "translationsForLanguages", joinColumns = @JoinColumn(
      name = "condition_id"))
  private List<TranslationForLanguage> translations;

  @Transient
  private String i8nKey;

  @Transient
  private String translatedKey;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public RateFile getRateFile() {
    return rateFile;
  }

  public void setRateFile(RateFile rateFile) {
    this.rateFile = rateFile;
  }

  /**
   * deep copys the condition.
   * 
   * @return the deep copy.
   */
  public Condition deepCopy() {
    Condition condition = new Condition();
    condition.setValue(this.value);
    condition.setRateFile(this.rateFile);
    condition.setConditionKey(conditionKey);
    condition.setStandardSelected(standardSelected);
    condition.setI8nKey(i8nKey);
    return condition;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Condition)) {
      return false;
    }
    Condition other = (Condition) obj;
    if (other.getId() == id) {
      return true;
    }
    return false;
  }

  public TranslationKey getConditionKey() {
    return conditionKey;
  }

  public void setConditionKey(TranslationKey conditionKey) {
    this.conditionKey = conditionKey;
  }

  public boolean isStandardSelected() {
    return standardSelected;
  }

  public void setStandardSelected(boolean standardSelected) {
    this.standardSelected = standardSelected;
  }

  public ConditionType getConditionType() {
    return conditionType;
  }

  public void setConditionType(ConditionType conditionType) {
    this.conditionType = conditionType;
  }

  public String getI8nKey() {
    return i8nKey;
  }

  public void setI8nKey(String i8nKey) {
    this.i8nKey = i8nKey;
  }

  public String getTranslatedKey() {
    return translatedKey;
  }

  public void setTranslatedKey(String translatedValue) {
    this.translatedKey = translatedValue;
  }

  public List<TranslationForLanguage> getTranslations() {
    return translations;
  }

  public void setTranslations(List<TranslationForLanguage> translations) {
    this.translations = translations;
  }

  /**
   * gets the translation for a lnaguage.
   * 
   * @param language the lagnuage to use.
   * @return the found translation.
   */
  public TranslationForLanguage getTranslationForLanguage(Language language) {
    for (TranslationForLanguage tl : translations) {
      if (tl.getLanguage().equals(language)) {
        return tl;
      }
    }
    return null;
  }

  public TranslationForLanguage getEnglishTranslation() {
    return getTranslationForLanguage(Language.EN);
  }

  public TranslationForLanguage getFrenchTranslation() {
    return getTranslationForLanguage(Language.FR);
  }

  public TranslationForLanguage getGermanTranslation() {
    return getTranslationForLanguage(Language.DE);
  }

  public TranslationForLanguage getDutchTranslation() {
    return getTranslationForLanguage(Language.NL);
  }

  public boolean isCalculationCondition() {
    return conditionType == ConditionType.CALCULATION;
  }

  /**
   * add a translation to the condition.
   * 
   * @param language te language of the translation
   * @param translation the translation.
   */
  public void addTranslation(Language language, String translation) {
    if (translations == null) {
      translations = new ArrayList<TranslationForLanguage>();
    }
    translations.add(new TranslationForLanguage(language, translation));
  }

  public CalculationValueType getCalculationValueType() {
    return calculationValueType;
  }

  public void setCalculationValueType(CalculationValueType calculationValueType) {
    this.calculationValueType = calculationValueType;
  }
}
