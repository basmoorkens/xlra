package com.moorkensam.xlra.model.offerte;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.CalcUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "pricecalculation")
public class PriceCalculation extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private BigDecimal basePrice;

  private BigDecimal finalPrice;

  private BigDecimal dieselPrice;

  private BigDecimal chfPrice;

  private BigDecimal calculatedAdrSurcharge;

  private BigDecimal resultingPriceSurcharge;

  private BigDecimal adrSurchargeMinimum;

  private BigDecimal importFormalities;

  private BigDecimal exportFormalities;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "appliedoperations", joinColumns = @JoinColumn(name = "calculation_id"))
  private List<TranslationKey> appliedOperations;

  public BigDecimal getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public BigDecimal getDieselPrice() {
    return dieselPrice;
  }

  public void setDieselPrice(BigDecimal dieselPrice) {
    this.dieselPrice = dieselPrice;
  }

  public BigDecimal getChfPrice() {
    return chfPrice;
  }

  public void setChfPrice(BigDecimal chfPrice) {
    this.chfPrice = chfPrice;
  }

  public BigDecimal getCalculatedAdrSurcharge() {
    return calculatedAdrSurcharge;
  }

  public void setCalculatedAdrSurcharge(BigDecimal calculatedAdrSurcharge) {
    this.calculatedAdrSurcharge = calculatedAdrSurcharge;
  }

  public BigDecimal getAdrSurchargeMinimum() {
    return adrSurchargeMinimum;
  }

  public void setAdrSurchargeMinimum(BigDecimal adrSurchargeMinimum) {
    this.adrSurchargeMinimum = adrSurchargeMinimum;
  }

  public BigDecimal getResultingPriceSurcharge() {
    return resultingPriceSurcharge;
  }

  public void setResultingPriceSurcharge(BigDecimal resultingPriceSurcharge) {
    this.resultingPriceSurcharge = resultingPriceSurcharge;
  }

  public BigDecimal getExportFormalities() {
    return exportFormalities;
  }

  public void setExportFormalities(BigDecimal exportFormalities) {
    this.exportFormalities = exportFormalities;
  }

  public BigDecimal getImportFormalities() {
    return importFormalities;
  }

  public void setImportFormalities(BigDecimal importFormalities) {
    this.importFormalities = importFormalities;
  }

  /**
   * Add to final price.
   * 
   * @param toAdd the value to add.
   */
  public void addToFinalPrice(BigDecimal toAdd) {
    if (finalPrice == null) {
      finalPrice = new BigDecimal(0d);
    }
    finalPrice = new BigDecimal(finalPrice.doubleValue() + toAdd.doubleValue());
  }

  public List<TranslationKey> getAppliedOperations() {
    return appliedOperations;
  }

  public void setAppliedOperations(List<TranslationKey> appliedOperations) {
    this.appliedOperations = appliedOperations;
  }

  /**
   * Calculate the total price.
   */
  public void calculateTotalPrice() {
    setAppliedOperations(new ArrayList<TranslationKey>());
    addToFinalPrice(getBasePrice());
    if (getDieselPrice() != null) {
      addToFinalPrice(getDieselPrice());
      getAppliedOperations().add(TranslationKey.DIESEL_SURCHARGE);
    }
    if (getChfPrice() != null) {
      addToFinalPrice(getChfPrice());
      getAppliedOperations().add(TranslationKey.CHF_SURCHARGE);
    }
    if (getImportFormalities() != null) {
      addToFinalPrice(getImportFormalities());
      getAppliedOperations().add(TranslationKey.IMPORT_FORM);
    }
    if (getExportFormalities() != null) {
      addToFinalPrice(getExportFormalities());
      getAppliedOperations().add(TranslationKey.EXPORT_FORM);
    }
    if (getResultingPriceSurcharge() != null) {
      addToFinalPrice(getResultingPriceSurcharge());
      getAppliedOperations().add(TranslationKey.ADR_SURCHARGE);
    }
    CalcUtil calcUtil = CalcUtil.getInstance();
    setFinalPrice(calcUtil.roundBigDecimal(getFinalPrice()));
  }

}
