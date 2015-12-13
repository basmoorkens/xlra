package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.CalcUtil;
import com.moorkensam.xlra.service.util.QuotationUtil;

@Stateless
public class CalculationServiceImpl implements CalculationService {

  private static final String CHF = "ch";

  @Inject
  private DieselService dieselService;

  @Inject
  private CurrencyService currencyService;

  @Inject
  private ConfigurationDao configurationDao;

  private CalcUtil calcUtil;

  private QuotationUtil quotationUtil;

  @PostConstruct
  public void init() {
    setCalcUtil(CalcUtil.getInstance());
    setQuotationUtil(QuotationUtil.getInstance());
  }

  @Override
  /**
   * Calculates the prices according to some business rules.
   */
  public PriceCalculation calculatePriceAccordingToConditions(QuotationResult offerte)
      throws RateFileException {
    PriceCalculation calculation = new PriceCalculation();
    calculation.setBasePrice(offerte.getCalculation().getBasePrice());
    offerte.setCalculation(calculation);

    Configuration config = getConfigurationDao().getXlraConfiguration();

    for (OfferteOptionDto option : offerte.getSelectableOptions()) {
      switch (option.getKey()) {
        case ADR_SURCHARGE:
          if (option.isSelected()) {
            calculateAddressSurcharge(offerte.getCalculation(), option);
          }
          break;
        case ADR_MINIMUM:
          if (option.isSelected()) {
            calculateAddressSurchargeMinimum(offerte.getCalculation(), option);
          }
          break;
        case IMPORT_FORM:
          if (option.isSelected()) {
            calculateImportFormality(offerte.getCalculation(), option);
          }
          break;
        case EXPORT_FORM:
          if (option.isSelected()) {
            calculateExportFormality(offerte.getCalculation(), option);
          }
          break;
        default:
          break;
      }
    }

    calculateDieselSurchargePrice(offerte, config);
    if (offerte.getCountry().getShortName().equalsIgnoreCase(CHF)) {
      calculateChfSurchargePrice(offerte, config);
    }

    applyAfterConditionLogic(offerte.getCalculation());
    offerte.getCalculation().calculateTotalPrice();
    return offerte.getCalculation();
  }

  /**
   * Calculates the export formalities.
   * 
   * @param priceCalculation The pricecalculation to fill the export forms into.
   * @param option The option that has the value.
   * @throws RateFileException Thrown when an invalid number was in the value.
   */
  protected void calculateExportFormality(PriceCalculation priceCalculation, OfferteOptionDto option)
      throws RateFileException {
    try {
      BigDecimal exportFormalities = new BigDecimal(Double.parseDouble(option.getValue()));
      exportFormalities = getCalcUtil().roundBigDecimal(exportFormalities);
      priceCalculation.setExportFormalities(exportFormalities);
    } catch (NumberFormatException exc) {
      throw new RateFileException("Invalid value for " + option.getKey() + ": " + ""
          + option.getValue());
    }
  }

  /**
   * Calculates the import formalities.
   * 
   * @param priceCalculation The pricecalculation to fill the export forms into.
   * 
   * @param option The option that has the value.
   * @throws RateFileException Thrown when an invalid number was in the value.
   */
  protected void calculateImportFormality(PriceCalculation priceCalculation, OfferteOptionDto option)
      throws RateFileException {
    try {
      BigDecimal importFormalities = new BigDecimal(Double.parseDouble(option.getValue()));
      importFormalities = getCalcUtil().roundBigDecimal(importFormalities);
      priceCalculation.setImportFormalities(importFormalities);
    } catch (NumberFormatException exc) {
      throw new RateFileException("Invalid value for " + option.getKey() + ": " + ""
          + option.getValue());
    }
  }

  /**
   * Calculates the min adress surcharge.
   * 
   * @param priceCalculation The pricecalculation to fill the export forms into.
   * @param option The option that has the value.
   * @throws RateFileException Thrown when an invalid number was in the value.
   */
  protected void calculateAddressSurchargeMinimum(PriceCalculation priceCalculation,
      OfferteOptionDto option) throws RateFileException {
    try {
      priceCalculation.setAdrSurchargeMinimum(getCalcUtil().roundBigDecimal(
          new BigDecimal(Double.parseDouble(option.getValue()))));
    } catch (NumberFormatException exc) {
      throw new RateFileException("Invalid value for " + option.getKey() + ": " + ""
          + option.getValue());
    }
  }

  /**
   * Apply some business rules to the calculation that can only be executed after all conditions are
   * parsed.
   * 
   * @param priceCalculation The pricecalculation
   */
  protected void applyAfterConditionLogic(PriceCalculation priceCalculation) {
    if (priceCalculation.getCalculatedAdrSurcharge() != null) {
      if (priceCalculation.getAdrSurchargeMinimum().doubleValue() > priceCalculation
          .getCalculatedAdrSurcharge().doubleValue()) {
        priceCalculation.setResultingPriceSurcharge(priceCalculation.getAdrSurchargeMinimum());
      } else {
        priceCalculation.setResultingPriceSurcharge(priceCalculation.getCalculatedAdrSurcharge());
      }

    }

  }

  /**
   * Calculates the address surcharge.
   * 
   * @param condition The condition
   */
  protected void calculateAddressSurcharge(PriceCalculation priceCalculation,
      OfferteOptionDto option) throws RateFileException {
    try {
      BigDecimal multiplier =
          getCalcUtil().convertPercentageToBaseMultiplier(Double.parseDouble(option.getValue()));
      BigDecimal result =
          new BigDecimal(priceCalculation.getBasePrice().doubleValue() * multiplier.doubleValue());
      result = getCalcUtil().roundBigDecimal(result);
      priceCalculation.setCalculatedAdrSurcharge(result);
    } catch (NumberFormatException exc) {
      throw new RateFileException("Invalid value for " + option.getKey() + ": " + ""
          + option.getValue());
    }
  }

  protected void calculateChfSurchargePrice(QuotationResult offerte, Configuration config)
      throws RateFileException {
    CurrencyRate chfRate =
        getCurrencyService().getChfRateForCurrentPrice(config.getCurrentChfValue());
    BigDecimal multiplier =
        getCalcUtil().convertPercentageToBaseMultiplier(chfRate.getSurchargePercentage());
    BigDecimal result =
        new BigDecimal(offerte.getCalculation().getBasePrice().doubleValue()
            * multiplier.doubleValue());
    result = getCalcUtil().roundBigDecimal(result);
    offerte.getCalculation().setChfPrice(result);

    if (!quotationUtil.offerteOptionsContainsKey(offerte.getSelectableOptions(),
        TranslationKey.CHF_SURCHARGE)) {
      offerte.getSelectableOptions().add(
          quotationUtil.createCalculationOption(TranslationKey.CHF_SURCHARGE, result));
    }
  }

  /**
   * Calculates the diesel supplement for the found base price.
   * 
   * @param priceCalculation The pricedto object to take the base price from and to save the diesel
   *        surcharge into.
   * @param config The config object to take the current diesel price from.
   * @throws RateFileException Thrown when no dieselpercentage multiplier can be found for the
   *         current diesel price.
   */
  protected void calculateDieselSurchargePrice(QuotationResult offerte, Configuration config)
      throws RateFileException {
    DieselRate dieselRate =
        getDieselService().getDieselRateForCurrentPrice(config.getCurrentDieselPrice());
    BigDecimal multiplier =
        getCalcUtil().convertPercentageToBaseMultiplier(dieselRate.getSurchargePercentage());
    BigDecimal result =
        new BigDecimal(offerte.getCalculation().getBasePrice().doubleValue()
            * multiplier.doubleValue());
    result = getCalcUtil().roundBigDecimal(result);
    offerte.getCalculation().setDieselPrice(result);

    if (!quotationUtil.offerteOptionsContainsKey(offerte.getSelectableOptions(),
        TranslationKey.DIESEL_SURCHARGE)) {
      offerte.getSelectableOptions().add(
          quotationUtil.createCalculationOption(TranslationKey.DIESEL_SURCHARGE, result));
    }
  }

  public DieselService getDieselService() {
    return dieselService;
  }

  public void setDieselService(DieselService dieselService) {
    this.dieselService = dieselService;
  }

  public CurrencyService getCurrencyService() {
    return currencyService;
  }

  public void setCurrencyService(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  public ConfigurationDao getConfigurationDao() {
    return configurationDao;
  }

  public void setConfigurationDao(ConfigurationDao configurationDao) {
    this.configurationDao = configurationDao;
  }

  public CalcUtil getCalcUtil() {
    return calcUtil;
  }

  public void setCalcUtil(CalcUtil calcUtil) {
    this.calcUtil = calcUtil;
  }

  public QuotationUtil getQuotationUtil() {
    return quotationUtil;
  }

  public void setQuotationUtil(QuotationUtil quotationUtil) {
    this.quotationUtil = quotationUtil;
  }
}
