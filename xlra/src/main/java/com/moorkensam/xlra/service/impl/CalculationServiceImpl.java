package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.util.CalcUtil;

@Stateless
public class CalculationServiceImpl implements CalculationService {

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
	public void calculatePriceAccordingToConditions(PriceCalculation priceDTO,
			Country country, List<Condition> conditions, QuotationQuery query)
			throws RateFileException {
		Configuration config = getConfigurationDao().getXlraConfiguration();
		calculateDieselSurchargePrice(priceDTO, config);
		if (country.getShortName().equalsIgnoreCase("chf")) {
			calculateChfSurchargePrice(priceDTO, config);
		}
		for (Condition condition : conditions) {
			switch (condition.getConditionKey()) {
			case ADR_SURCHARGE:
				if (query.isAdrSurcharge()) {
					calculateAddressSurcharge(priceDTO, condition);
				}
				break;
			case ADR_MINIMUM:
				if (query.isAdrSurcharge()) {
					calculateAddressSurchargeMinimum(priceDTO, condition);
				}
				break;
			case IMPORT_FORM:
				if (query.isImportFormality()) {
					calculateImportFormality(priceDTO, condition);
				}
				break;
			case EXPORT_FORM:
				if (query.isExportFormality()) {
					calculateExportFormality(priceDTO, condition);
				}
			default:
				break;
			}
		}
		applyAfterConditionLogic(priceDTO);
		priceDTO.calculateTotalPrice();
	}

	protected void calculateExportFormality(PriceCalculation priceDTO,
			Condition condition) throws RateFileException {
		try {
			BigDecimal exportFormalities = new BigDecimal(
					Double.parseDouble(condition.getValue()));
			exportFormalities = getCalcUtil().roundBigDecimal(exportFormalities);
			priceDTO.setExportFormalities(exportFormalities);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for "
					+ condition.getConditionKey() + ": " + condition.getValue());
		}
	}

	protected void calculateImportFormality(PriceCalculation priceDTO,
			Condition condition) throws RateFileException {
		try {
			BigDecimal importFormalities = new BigDecimal(
					Double.parseDouble(condition.getValue()));
			importFormalities = getCalcUtil().roundBigDecimal(importFormalities);
			priceDTO.setImportFormalities(importFormalities);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for "
					+ condition.getConditionKey() + ": " + condition.getValue());
		}
	}

	protected void calculateAddressSurchargeMinimum(PriceCalculation priceDTO,
			Condition condition) throws RateFileException {
		try {
			priceDTO.setAdrSurchargeMinimum(getCalcUtil()
					.roundBigDecimal(new BigDecimal(Double
							.parseDouble(condition.getValue()))));
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for "
					+ condition.getConditionKey() + ": " + condition.getValue());
		}
	}

	/**
	 * Apply some business rules to the calculation that can only be executed
	 * after all conditions are parsed.
	 * 
	 * @param priceDTO
	 */
	protected void applyAfterConditionLogic(PriceCalculation priceDTO) {
		if (priceDTO.getAdrSurchargeMinimum() == null
				&& priceDTO.getCalculatedAdrSurcharge() == null) {
		} else {
			if (priceDTO.getAdrSurchargeMinimum().doubleValue() > priceDTO
					.getCalculatedAdrSurcharge().doubleValue()) {
				priceDTO.setResultingPriceSurcharge(priceDTO
						.getAdrSurchargeMinimum());
			} else {
				priceDTO.setResultingPriceSurcharge(priceDTO
						.getCalculatedAdrSurcharge());
			}

		}

	}

	/**
	 * Calculates the address surcharge.
	 * 
	 * @param condition
	 */
	protected void calculateAddressSurcharge(PriceCalculation priceDTO,
			Condition condition) throws RateFileException {
		try {
			BigDecimal multiplier = getCalcUtil()
					.convertPercentageToBaseMultiplier(Double
							.parseDouble(condition.getValue()));
			BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
					.doubleValue() * multiplier.doubleValue());
			result = getCalcUtil().roundBigDecimal(result);
			priceDTO.setCalculatedAdrSurcharge(result);
		} catch (NumberFormatException exc) {
			throw new RateFileException("Invalid value for "
					+ condition.getConditionKey() + ": " + condition.getValue());
		}
	}

	protected void calculateChfSurchargePrice(PriceCalculation priceDTO,
			Configuration config) throws RateFileException {
		CurrencyRate chfRate = getCurrencyService().getChfRateForCurrentPrice(
				config.getCurrentChfValue());
		BigDecimal multiplier = getCalcUtil()
				.convertPercentageToBaseMultiplier(chfRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = getCalcUtil().roundBigDecimal(result);
		priceDTO.setChfPrice(result);
	}

	/**
	 * Calculates the diesel supplement for the found base price.
	 * 
	 * @param priceDTO
	 *            The pricedto object to take the base price from and to save
	 *            the diesel surcharge into.
	 * @param config
	 *            The config object to take the current diesel price from.
	 * @throws RateFileException
	 *             Thrown when no dieselpercentage multiplier can be found for
	 *             the current diesel price.
	 */
	protected void calculateDieselSurchargePrice(PriceCalculation priceDTO,
			Configuration config) throws RateFileException {
		DieselRate dieselRate = getDieselService()
				.getDieselRateForCurrentPrice(config.getCurrentDieselPrice());
		BigDecimal multiplier = getCalcUtil()
				.convertPercentageToBaseMultiplier(dieselRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = getCalcUtil().roundBigDecimal(result);
		priceDTO.setDieselPrice(result);
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
