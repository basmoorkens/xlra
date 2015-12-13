package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.CalcUtil;
import com.moorkensam.xlra.service.util.QuotationUtil;

public class CalculationServiceTest extends UnitilsJUnit4 {
  @TestedObject
  private CalculationServiceImpl calcService;

  private PriceCalculation priceDto;

  private Configuration config;

  @Mock
  private DieselService dieselService;

  @Mock
  private CurrencyService currencyService;

  private CalcUtil calcUtil;

  @Mock
  private ConfigurationDao configurationDao;

  private DieselRate dieselRate;

  private CurrencyRate chfRate;

  private OfferteOptionDto adrsurchargeOption;

  private QuotationUtil quotationUtil;

  /**
   * init the test.
   */
  @Before
  public void init() {
    calcService = new CalculationServiceImpl();
    calcService.setDieselService(dieselService);
    calcService.setConfigurationDao(configurationDao);
    calcService.setCurrencyService(currencyService);
    priceDto = new PriceCalculation();
    priceDto.setBasePrice(new BigDecimal(500d));
    config = new Configuration();
    config.setCurrentDieselPrice(BigDecimal.valueOf(1.20d));
    dieselRate = new DieselRate();
    dieselRate.setSurchargePercentage(5.00d);
    chfRate = new CurrencyRate();
    chfRate.setSurchargePercentage(10);
    adrsurchargeOption = new OfferteOptionDto();
    adrsurchargeOption.setValue("20");
    adrsurchargeOption.setKey(TranslationKey.ADR_SURCHARGE);
    calcUtil = CalcUtil.getInstance();
    calcService.setCalcUtil(calcUtil);
    quotationUtil = QuotationUtil.getInstance();
    calcService.setQuotationUtil(quotationUtil);
  }

  @Test
  public void testcalculateExportFormality() throws RateFileException {
    OfferteOptionDto option = new OfferteOptionDto();
    option.setValue("40.15d");
    calcService.calculateExportFormality(priceDto, option);
    BigDecimal result = BigDecimal.valueOf(40.15d);
    result = result.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(result, priceDto.getExportFormalities());
  }

  @Test
  public void testCalcImportFormality() throws RateFileException {
    OfferteOptionDto option = new OfferteOptionDto();
    option.setValue("20.33d");
    calcService.calculateImportFormality(priceDto, option);
    BigDecimal result = BigDecimal.valueOf(20.33d);
    result = result.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(result, priceDto.getImportFormalities());
  }

  @Test
  public void testApplyAfterconditionLogic() {
    priceDto.setAdrSurchargeMinimum(new BigDecimal(20.00d));
    priceDto.setCalculatedAdrSurcharge(new BigDecimal(19.00d));
    calcService.applyAfterConditionLogic(priceDto);
    Assert.assertEquals(priceDto.getAdrSurchargeMinimum(), priceDto.getResultingPriceSurcharge());
  }

  @Test
  public void testApplyAfterconditionLogicCalc() {
    priceDto.setAdrSurchargeMinimum(new BigDecimal(20.00d));
    priceDto.setCalculatedAdrSurcharge(new BigDecimal(190.00d));
    calcService.applyAfterConditionLogic(priceDto);
    Assert
        .assertEquals(priceDto.getCalculatedAdrSurcharge(), priceDto.getResultingPriceSurcharge());
  }

  @Test
  public void testCalculateAdrSurcharge() throws RateFileException {
    calcService.calculateAddressSurcharge(priceDto, adrsurchargeOption);
    BigDecimal expected = new BigDecimal(100.00d);
    expected = expected.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(expected, priceDto.getCalculatedAdrSurcharge());
  }

  @Test
  public void testCalculateChfPrice() throws RateFileException {
    EasyMock.expect(currencyService.getChfRateForCurrentPrice(config.getCurrentChfValue()))
        .andReturn(chfRate);
    EasyMockUnitils.replay();
    QuotationResult offerte = new QuotationResult();
    offerte.setCalculation(priceDto);
    calcService.calculateChfSurchargePrice(offerte, config);

    BigDecimal expected = new BigDecimal(50.00d);
    expected = expected.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(expected, priceDto.getChfPrice());
  }

  @Test
  public void testCalculateDieselPrice() throws RateFileException {
    EasyMock.expect(dieselService.getDieselRateForCurrentPrice(config.getCurrentDieselPrice()))
        .andReturn(dieselRate);
    EasyMockUnitils.replay();
    QuotationResult offerte = new QuotationResult();
    offerte.setCalculation(priceDto);
    calcService.calculateDieselSurchargePrice(offerte, config);
    BigDecimal expected = new BigDecimal(25.00d);
    expected = expected.setScale(2, RoundingMode.HALF_UP);
    Assert.assertEquals(expected, priceDto.getDieselPrice());
  }

  @Test
  public void testCalculatePRiceACcordingToConditions() throws RateFileException {
    Country country = new Country();
    country.setShortName("ch");

    PriceCalculation priceDto = new PriceCalculation();
    priceDto.setBasePrice(new BigDecimal(100d));
    DieselRate dr = new DieselRate();
    dr.setSurchargePercentage(10d);
    CurrencyRate cr = new CurrencyRate();
    cr.setSurchargePercentage(10d);
    OfferteOptionDto adrOption = new OfferteOptionDto();
    adrOption.setKey(TranslationKey.ADR_SURCHARGE);
    adrOption.setValue("10");
    adrOption.setSelected(true);
    OfferteOptionDto adrMinOption = new OfferteOptionDto();
    adrMinOption.setKey(TranslationKey.ADR_MINIMUM);
    adrMinOption.setValue("50d");
    adrMinOption.setSelected(true);
    OfferteOptionDto importOption = new OfferteOptionDto();
    importOption.setKey(TranslationKey.IMPORT_FORM);
    importOption.setValue("100");
    importOption.setSelected(true);
    OfferteOptionDto expoOption = new OfferteOptionDto();
    expoOption.setKey(TranslationKey.EXPORT_FORM);
    expoOption.setValue("100");
    expoOption.setSelected(true);
    QuotationQuery query = new QuotationQuery();
    query.setImportFormality(true);
    query.setExportFormality(true);
    query.setAdrSurcharge(true);
    query.setCountry(country);
    QuotationResult offerte = new QuotationResult();
    offerte.setQuery(query);
    offerte.setCalculation(priceDto);
    priceDto.setBasePrice(new BigDecimal(100d));
    List<OfferteOptionDto> dtoList = new ArrayList<OfferteOptionDto>();
    dtoList.add(adrMinOption);
    dtoList.add(adrOption);
    dtoList.add(importOption);
    dtoList.add(expoOption);
    offerte.setSelectableOptions(dtoList);
    config.setCurrentDieselPrice(new BigDecimal(100d));
    config.setCurrentChfValue(new BigDecimal(100));
    EasyMock.expect(configurationDao.getXlraConfiguration()).andReturn(config);
    EasyMock.expect(dieselService.getDieselRateForCurrentPrice(config.getCurrentDieselPrice()))
        .andReturn(dr);
    EasyMock.expect(currencyService.getChfRateForCurrentPrice(new BigDecimal(100d))).andReturn(cr);

    EasyMockUnitils.replay();
    priceDto = calcService.calculatePriceAccordingToConditions(offerte);
    CalcUtil calcUtil = CalcUtil.getInstance();
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)), priceDto.getChfPrice());
    Assert
        .assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)), priceDto.getDieselPrice());
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)),
        priceDto.getCalculatedAdrSurcharge());
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(50.00d)),
        priceDto.getAdrSurchargeMinimum());
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(100.00d)),
        priceDto.getImportFormalities());
    Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(100.00d)),
        priceDto.getExportFormalities());
  }
}
