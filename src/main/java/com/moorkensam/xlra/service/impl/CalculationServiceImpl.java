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
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.CalcUtil;

@Stateless
public class CalculationServiceImpl implements CalculationService {

	private static final String CHF = "chf";

	@Inject
	private DieselService dieselService;

	@Inject
	private CurrencyService currencyService;

	@Inject
	private ConfigurationDao configurationDao;

	private CalcUtil calcUtil;

	@PostConstruct
	public void init() {
		setCalcUtil(CalcUtil.getInstance());
	}

	@Override
	/**
	 * Calculates the prices according to some business rules.
	 */
	public PriceCalculation calculatePriceAccordingToConditions(
			QuotationResult offerte) throws RateFileException {
		PriceCalculation priceCalculation = new PriceCalculation();
		priceCalculation.setBasePrice(offerte.getCalculation().getBasePrice());
		Configuration config = getConfigurationDao().getXlraConfiguration();
		calculateDieselSurchargePrice(priceCalculation, config);
		if (offerte.getCountry().getShortName().equalsIgnoreCase(CHF)) {
			calculateChfSurchargePrice(priceCalculation, config);
		}
		for (OfferteOptionDTO option : offerte.getSelectableOptions()) {
			switch (option.getKey()) {
			case ADR_SURCHARGE:
				if (option.isSelected()) {
					calculateAddressSurcharge(priceCalculation, option);
				}
				break;
			case ADR_MINIMUM:
				if (option.isSelected()) {
					calculateAddressSurchargeMinimum(priceCalculation, option);
				}
				break;
			case IMPORT_FORM:
				if (option.isSelected()) {
					calculateImportFormality(priceCalculation, option);
				}
				break;
			case EXPORT_FORM:
				if (option.isSelected()) {
					calculateExportFormality(priceCalculation, option);
				}
			default:
				break;
			}
		}
		applyAfterConditionLogic(priceCalculation);
		priceCalculation.calculateTotalPrice();
		return priceCalculation;
	}

	protected void calculateExportFormality(PriceCalculation priceCalculation,
			OfferteOptionDTO option) throws RateFileException {
		try {
			BigDecimal exportFormalities = new BigDecimal(
					Double.parseDouble(option.getValue()));
			exportFormalities = getCalcUtil()
					.roundBigDecimal(exportFormalities);
			priceCalculation.setExportFormalities(exportFormalities);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for " + option.getKey()
					+ ": " + option.getValue());
		}
	}

	protected void calculateImportFormality(PriceCalculation priceCalculation,
			OfferteOptionDTO option) throws RateFileException {
		try {
			BigDecimal importFormalities = new BigDecimal(
					Double.parseDouble(option.getValue()));
			importFormalities = getCalcUtil()
					.roundBigDecimal(importFormalities);
			priceCalculation.setImportFormalities(importFormalities);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for " + option.getKey()
					+ ": " + option.getValue());
		}
	}

	protected void calculateAddressSurchargeMinimum(
			PriceCalculation priceCalculation, OfferteOptionDTO option)
			throws RateFileException {
		try {
			priceCalculation.setAdrSurchargeMinimum(getCalcUtil()
					.roundBigDecimal(
							new BigDecimal(
									Double.parseDouble(option.getValue()))));
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for " + option.getKey()
					+ ": " + option.getValue());
		}
	}

	/**
	 * Apply some business rules to the calculation that can only be executed
	 * after all conditions are parsed.
	 * 
	 * @param priceCalculation
	 */
	protected void applyAfterConditionLogic(PriceCalculation priceCalculation) {
		if (priceCalculation.getAdrSurchargeMinimum() == null
				&& priceCalculation.getCalculatedAdrSurcharge() == null) {
		} else {
			if (priceCalculation.getAdrSurchargeMinimum().doubleValue() > priceCalculation
					.getCalculatedAdrSurcharge().doubleValue()) {
				priceCalculation.setResultingPriceSurcharge(priceCalculation
						.getAdrSurchargeMinimum());
			} else {
				priceCalculation.setResultingPriceSurcharge(priceCalculation
						.getCalculatedAdrSurcharge());
			}

		}

	}

	/**
	 * Calculates the address surcharge.
	 * 
	 * @param condition
	 */
	protected void calculateAddressSurcharge(PriceCalculation priceCalculation,
			OfferteOptionDTO option) throws RateFileException {
		try {
			BigDecimal multiplier = getCalcUtil()
					.convertPercentageToBaseMultiplier(
							Double.parseDouble(option.getValue()));
			BigDecimal result = new BigDecimal(priceCalculation.getBasePrice()
					.doubleValue() * multiplier.doubleValue());
			result = getCalcUtil().roundBigDecimal(result);
			priceCalculation.setCalculatedAdrSurcharge(result);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for " + option.getKey()
					+ ": " + option.getValue());
		}
	}

	protected void calculateChfSurchargePrice(
			PriceCalculation priceCalculation, Configuration config)
			throws RateFileException {
		CurrencyRate chfRate = getCurrencyService().getChfRateForCurrentPrice(
				config.getCurrentChfValue());
		BigDecimal multiplier = getCalcUtil()
				.convertPercentageToBaseMultiplier(
						chfRate.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceCalculation.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = getCalcUtil().roundBigDecimal(result);
		priceCalculation.setChfPrice(result);
	}

	/**
	 * Calculates the diesel supplement for the found base price.
	 * 
	 * @param priceCalculation
	 *            The pricedto object to take the base price from and to save
	 *            the diesel surcharge into.
	 * @param config
	 *            The config object to take the current diesel price from.
	 * @throws RateFileException
	 *             Thrown when no dieselpercentage multiplier can be found for
	 *             the current diesel price.
	 */
	protected void calculateDieselSurchargePrice(
			PriceCalculation priceCalculation, Configuration config)
			throws RateFileException {
		DieselRate dieselRate = getDieselService()
				.getDieselRateForCurrentPrice(config.getCurrentDieselPrice());
		BigDecimal multiplier = getCalcUtil()
				.convertPercentageToBaseMultiplier(
						dieselRate.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceCalculation.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = getCalcUtil().roundBigDecimal(result);
		priceCalculation.setDieselPrice(result);
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
}
