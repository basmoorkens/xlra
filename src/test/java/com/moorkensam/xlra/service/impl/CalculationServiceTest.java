package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.omnifaces.taghandler.ImportConstants;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.CalcUtil;

public class CalculationServiceTest extends UnitilsJUnit4 {
	@TestedObject
	private CalculationServiceImpl calcService;

	private PriceCalculation priceDTO;

	private Configuration config;

	@Mock
	private DieselService dieselService;

	@Mock
	private CurrencyService currencyService;

	private CalcUtil calcUtil;

	@Mock
	private ConfigurationDao configurationDAO;

	private DieselRate dieselRate;

	private CurrencyRate chfRate;

	private OfferteOptionDTO adrsurchargeOption;

	@Before
	public void init() {
		calcService = new CalculationServiceImpl();
		calcService.setDieselService(dieselService);
		calcService.setConfigurationDao(configurationDAO);
		calcService.setCurrencyService(currencyService);
		priceDTO = new PriceCalculation();
		priceDTO.setBasePrice(new BigDecimal(500d));
		config = new Configuration();
		config.setCurrentDieselPrice(new BigDecimal(1.20d));
		dieselRate = new DieselRate();
		dieselRate.setSurchargePercentage(5.00d);
		chfRate = new CurrencyRate();
		chfRate.setSurchargePercentage(10);
		adrsurchargeOption = new OfferteOptionDTO();
		adrsurchargeOption.setValue("20");
		adrsurchargeOption.setKey(TranslationKey.ADR_SURCHARGE);
		calcUtil = CalcUtil.getInstance();
		calcService.setCalcUtil(calcUtil);
	}

	@Test
	public void testcalculateExportFormality() throws RateFileException {
		OfferteOptionDTO option = new OfferteOptionDTO();
		option.setValue("40.15d");
		calcService.calculateExportFormality(priceDTO, option);
		BigDecimal result = new BigDecimal(40.15d);
		result = result.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(result, priceDTO.getExportFormalities());
	}

	@Test
	public void testCalcImportFormality() throws RateFileException {
		OfferteOptionDTO option = new OfferteOptionDTO();
		option.setValue("20.33d");
		calcService.calculateImportFormality(priceDTO, option);
		BigDecimal result = new BigDecimal(20.33d);
		result = result.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(result, priceDTO.getImportFormalities());
	}

	@Test
	public void testApplyAfterconditionLogic() {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(20.00d));
		priceDTO.setCalculatedAdrSurcharge(new BigDecimal(19.00d));
		calcService.applyAfterConditionLogic(priceDTO);
		Assert.assertEquals(priceDTO.getAdrSurchargeMinimum(),
				priceDTO.getResultingPriceSurcharge());
	}

	@Test
	public void testApplyAfterconditionLogicCalc() {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(20.00d));
		priceDTO.setCalculatedAdrSurcharge(new BigDecimal(190.00d));
		calcService.applyAfterConditionLogic(priceDTO);
		Assert.assertEquals(priceDTO.getCalculatedAdrSurcharge(),
				priceDTO.getResultingPriceSurcharge());
	}

	@Test
	public void testCalculateAdrSurcharge() throws RateFileException {
		calcService.calculateAddressSurcharge(priceDTO, adrsurchargeOption);
		BigDecimal expected = new BigDecimal(100.00d);
		expected = expected.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(expected, priceDTO.getCalculatedAdrSurcharge());
	}

	@Test
	public void testCalculateChfPrice() throws RateFileException {
		EasyMock.expect(
				currencyService.getChfRateForCurrentPrice(config
						.getCurrentChfValue())).andReturn(chfRate);
		EasyMockUnitils.replay();

		calcService.calculateChfSurchargePrice(priceDTO, config);

		BigDecimal expected = new BigDecimal(50.00d);
		expected = expected.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(expected, priceDTO.getChfPrice());
	}

	@Test
	public void testCalculateDieselPrice() throws RateFileException {
		EasyMock.expect(
				dieselService.getDieselRateForCurrentPrice(config
						.getCurrentDieselPrice())).andReturn(dieselRate);
		EasyMockUnitils.replay();
		calcService.calculateDieselSurchargePrice(priceDTO, config);
		BigDecimal expected = new BigDecimal(25.00d);
		expected = expected.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(expected, priceDTO.getDieselPrice());
	}

	@Test
	public void testCalculatePRiceACcordingToConditions()
			throws RateFileException {
		Configuration config = new Configuration();
		Country country = new Country();
		country.setShortName("chf");

		PriceCalculation priceDTO = new PriceCalculation();
		priceDTO.setBasePrice(new BigDecimal(100d));
		DieselRate dr = new DieselRate();
		dr.setSurchargePercentage(10d);
		CurrencyRate cr = new CurrencyRate();
		cr.setSurchargePercentage(10d);
		OfferteOptionDTO adrOption = new OfferteOptionDTO();
		adrOption.setKey(TranslationKey.ADR_SURCHARGE);
		adrOption.setValue("10");
		adrOption.setSelected(true);
		OfferteOptionDTO adrMinOption = new OfferteOptionDTO();
		adrMinOption.setKey(TranslationKey.ADR_MINIMUM);
		adrMinOption.setValue("50d");
		adrMinOption.setSelected(true);
		OfferteOptionDTO importOption = new OfferteOptionDTO();
		importOption.setKey(TranslationKey.IMPORT_FORM);
		importOption.setValue("100");
		importOption.setSelected(true);
		OfferteOptionDTO expoOption = new OfferteOptionDTO();
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
		offerte.setCalculation(priceDTO);
		priceDTO.setBasePrice(new BigDecimal(100d));
		offerte.setSelectableOptions(Arrays.asList(adrOption, adrMinOption,
				importOption, expoOption));
		config.setCurrentDieselPrice(new BigDecimal(100d));
		config.setCurrentChfValue(new BigDecimal(100));
		EasyMock.expect(configurationDAO.getXlraConfiguration()).andReturn(
				config);
		EasyMock.expect(
				dieselService.getDieselRateForCurrentPrice(config
						.getCurrentDieselPrice())).andReturn(dr);
		EasyMock.expect(
				currencyService.getChfRateForCurrentPrice(new BigDecimal(100d)))
				.andReturn(cr);

		EasyMockUnitils.replay();
		priceDTO = calcService.calculatePriceAccordingToConditions(offerte);
		CalcUtil calcUtil = CalcUtil.getInstance();
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)),
				priceDTO.getChfPrice());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)),
				priceDTO.getDieselPrice());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(10.00d)),
				priceDTO.getCalculatedAdrSurcharge());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(50.00d)),
				priceDTO.getAdrSurchargeMinimum());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(100.00d)),
				priceDTO.getImportFormalities());
		Assert.assertEquals(calcUtil.roundBigDecimal(new BigDecimal(100.00d)),
				priceDTO.getExportFormalities());
	}
}
