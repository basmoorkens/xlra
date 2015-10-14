package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;

public class QuotationServiceTest extends UnitilsJUnit4 {

	@TestedObject
	private QuotationServiceImpl quotationService;

	private PriceCalculationDTO priceDTO;

	private Configuration config;

	@Mock
	private DieselService dieselService;

	@Mock
	private CurrencyService currencyService;

	private DieselRate dieselRate;

	private CurrencyRate chfRate;

	private Condition adrsurchargeCondition;

	@Before
	public void init() {
		quotationService = new QuotationServiceImpl();
		quotationService.setDieselService(dieselService);
		quotationService.setCurrencyService(currencyService);
		priceDTO = new PriceCalculationDTO();
		priceDTO.setBasePrice(new BigDecimal(500d));
		config = new Configuration();
		config.setCurrentDieselPrice(new BigDecimal(1.20d));
		dieselRate = new DieselRate();
		dieselRate.setSurchargePercentage(5.00d);
		chfRate = new CurrencyRate();
		chfRate.setSurchargePercentage(10);
		adrsurchargeCondition = new Condition();
		adrsurchargeCondition.setValue("20");
		adrsurchargeCondition.setConditionKey(TranslationKey.ADR_SURCHARGE);
	}

	@Test
	public void testApplyAfterconditionLogic() {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(20.00d));
		priceDTO.setCalculatedAdrSurcharge(new BigDecimal(19.00d));
		quotationService.applyAfterConditionLogic(priceDTO);
		Assert.assertEquals(priceDTO.getAdrSurchargeMinimum(),
				priceDTO.getResultingPriceSurcharge());
	}

	@Test
	public void testApplyAfterconditionLogicCalc() {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(20.00d));
		priceDTO.setCalculatedAdrSurcharge(new BigDecimal(190.00d));
		quotationService.applyAfterConditionLogic(priceDTO);
		Assert.assertEquals(priceDTO.getCalculatedAdrSurcharge(),
				priceDTO.getResultingPriceSurcharge());
	}

	@Test
	public void testCalculateAdrSurcharge() {
		quotationService.calculateAddressSurcharge(priceDTO,
				adrsurchargeCondition);
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

		quotationService.calculateChfSurchargePrice(priceDTO, config);

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
		quotationService.calculateDieselSurchargePrice(priceDTO, config);
		BigDecimal expected = new BigDecimal(25.00d);
		expected = expected.setScale(2, RoundingMode.HALF_UP);
		Assert.assertEquals(expected, priceDTO.getDieselPrice());
	}
}
