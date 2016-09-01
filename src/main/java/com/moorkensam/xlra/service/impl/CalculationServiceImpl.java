package com.moorkensam.xlra.service.impl;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;


@Stateless
public class CalculationServiceImpl implements CalculationService {

  private static final String CHF = "ch";

  private static final Logger logger = LogManager.getLogger();

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
    if (logger.isDebugEnabled()) {
      logger.debug("Calculating price for " + offerte.getOfferteUniqueIdentifier());
    }
    PriceCalculation calculation = new PriceCalculation();
    calculation.setBasePrice(offerte.getCalculation().getBasePrice());
    offerte.setCalculation(calculation);

    calculateOptionsInOfferte(offerte.getSelectableOptions(), offerte.getCalculation(), offerte
        .getOfferteCountry().getShortName());

    applyAfterConditionLogic(offerte.getCalculation());
    offerte.getCalculation().calculateTotalPrice();
    return offerte.getCalculation();
  }

  private void calculateOptionsInOfferte(List<OfferteOptionDto> options, PriceCalculation calc,
      String countryShortName) throws RateFileException {
    calculateVariableOptions(options, calc);
    calculateFixedOptions(options, calc, countryShortName);
  }

  private void calculateFixedOptions(List<OfferteOptionDto> options, PriceCalculation calc,
      String countryShortName) throws RateFileException {
    Configuration config = getConfigurationDao().getXlraConfiguration();
    calculateDieselSurchargePrice(options, calc, config.getCurrentDieselPrice());
    if (countryShortName.equalsIgnoreCase(CHF)) {
      calculateChfSurchargePrice(options, calc, config.getCurrentChfValue());
    }
  }

  private void calculateVariableOptions(List<OfferteOptionDto> options, PriceCalculation calc)
      throws RateFileException {
    boolean adrCalculated = false;
    for (OfferteOptionDto option : options) {
      switch (option.getKey()) {
        case ADR_SURCHARGE:
          if (option.isSelected()) {
            calculateAddressSurcharge(calc, option);
            adrCalculated = true;
          }
          break;
        case IMPORT_FORM:
          if (option.isSelected()) {
            calculateImportFormality(calc, option);
          }
          break;
        case EXPORT_FORM:
          if (option.isSelected()) {
            calculateExportFormality(calc, option);
          }
          break;
        default:
          break;
      }
    }

    if (adrCalculated) {
      setupBaseAdr(options, calc);
    }
  }

  private void setupBaseAdr(List<OfferteOptionDto> options, PriceCalculation calc)
      throws RateFileException {
    for (OfferteOptionDto option : options) {
      if (TranslationKey.ADR_MINIMUM.equals(option.getKey())) {
        calculateAddressSurchargeMinimum(calc, option);
        break;
      }
    }
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
   * @param priceCalc The pricecalculation to fill the export forms into.
   * 
   * @param option The option that has the value.
   * @throws RateFileException Thrown when an invalid number was in the value.
   */
  protected void calculateImportFormality(PriceCalculation priceCalc, OfferteOptionDto option)
      throws RateFileException {
    try {
      BigDecimal importFormalities = new BigDecimal(Double.parseDouble(option.getValue()));
      importFormalities = getCalcUtil().roundBigDecimal(importFormalities);
      priceCalc.setImportFormalities(importFormalities);
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
  protected void applyAfterConditionLogic(PriceCalculation priceCalculation)
      throws RateFileException {
    if (priceCalculation.getCalculatedAdrSurcharge() != null) {
      if (priceCalculation.getAdrSurchargeMinimum() == null) {
        throw new RateFileException("message.adr.calculated.no.minimum");
      }
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
      RateFileException rfe = new RateFileException("message.adr.parse.error");
      rfe.addExtraArgument(option.getValue());
      throw rfe;
    }
  }

  protected void calculateChfSurchargePrice(List<OfferteOptionDto> options, PriceCalculation calc,
      BigDecimal currentChfValue) throws RateFileException {
    CurrencyRate chfRate = getCurrencyService().getChfRateForCurrentPrice(currentChfValue);
    BigDecimal multiplier =
        getCalcUtil().convertPercentageToBaseMultiplier(chfRate.getSurchargePercentage());
    BigDecimal result =
        new BigDecimal(calc.getBasePrice().doubleValue() * multiplier.doubleValue());
    result = getCalcUtil().roundBigDecimal(result);
    calc.setChfPrice(result);

    if (!quotationUtil.offerteOptionsContainsKey(options, TranslationKey.CHF_SURCHARGE)) {
      options.add(quotationUtil.createCalculationOption(TranslationKey.CHF_SURCHARGE, result));
    }
  }

  /**
   * 
   * @param options the offerte options, the diesel option will get added to it.
   * @param calc the calculation object
   * @param currentDieselValue the current diesel price.
   * @throws RateFileException thrown when no currentdieselprice or diesel percentage could be
   *         found.
   */
  protected void calculateDieselSurchargePrice(List<OfferteOptionDto> options,
      PriceCalculation calc, BigDecimal currentDieselValue) throws RateFileException {
    DieselRate dieselRate = getDieselService().getDieselRateForCurrentPrice(currentDieselValue);
    BigDecimal multiplier =
        getCalcUtil().convertPercentageToBaseMultiplier(dieselRate.getSurchargePercentage());
    BigDecimal result =
        new BigDecimal(calc.getBasePrice().doubleValue() * multiplier.doubleValue());
    result = getCalcUtil().roundBigDecimal(result);
    calc.setDieselPrice(result);

    if (!quotationUtil.offerteOptionsContainsKey(options, TranslationKey.DIESEL_SURCHARGE)) {
      options.add(quotationUtil.createCalculationOption(TranslationKey.DIESEL_SURCHARGE, result));
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
