package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
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

	@Override
	/**
	 * Calculates the prices according to some business rules.
	 */
	public void calculatePriceAccordingToConditions(
			PriceCalculationDTO priceDTO, Country country,
			List<Condition> conditions, QuotationQuery query)
			throws RateFileException {
		Configuration config = configurationDao.getXlraConfiguration();
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
		calculateTotalPrice(priceDTO);
	}

	private void calculateTotalPrice(PriceCalculationDTO priceDTO) {
		priceDTO.setAppliedOperations(new ArrayList<TranslationKey>());
		priceDTO.addToFinalPrice(priceDTO.getBasePrice());
		if (priceDTO.getDieselPrice() != null) {
			priceDTO.addToFinalPrice(priceDTO.getDieselPrice());
			priceDTO.getAppliedOperations().add(
					TranslationKey.DIESEL_SURCHARGE_KEY);
		}
		if (priceDTO.getChfPrice() != null) {
			priceDTO.addToFinalPrice(priceDTO.getChfPrice());
			priceDTO.getAppliedOperations().add(
					TranslationKey.CHF_SURCHARGE_KEY);
		}
		if (priceDTO.getImportFormalities() != null) {
			priceDTO.addToFinalPrice(priceDTO.getImportFormalities());
			priceDTO.getAppliedOperations().add(TranslationKey.IMPORT_FORM_KEY);
		}
		if (priceDTO.getExportFormalities() != null) {
			priceDTO.addToFinalPrice(priceDTO.getExportFormalities());
			priceDTO.getAppliedOperations().add(TranslationKey.EXPORT_FORM_KEY);
		}
		if (priceDTO.getResultingPriceSurcharge() != null) {
			priceDTO.addToFinalPrice(priceDTO.getResultingPriceSurcharge());
			priceDTO.getAppliedOperations().add(TranslationKey.ADR_SURCHARGE);
		}
	}

	protected void calculateExportFormality(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal exportFormalities = new BigDecimal(
				Double.parseDouble(condition.getValue()));
		exportFormalities = CalcUtil.roundBigDecimal(exportFormalities);
		priceDTO.setExportFormalities(exportFormalities);
	}

	protected void calculateImportFormality(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal importFormalities = new BigDecimal(
				Double.parseDouble(condition.getValue()));
		importFormalities = CalcUtil.roundBigDecimal(importFormalities);
		priceDTO.setImportFormalities(importFormalities);
	}

	protected void calculateAddressSurchargeMinimum(
			PriceCalculationDTO priceDTO, Condition condition) {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(Double
				.parseDouble(condition.getValue())));
	}

	/**
	 * Apply some business rules to the calculation that can only be executed
	 * after all conditions are parsed.
	 * 
	 * @param priceDTO
	 */
	protected void applyAfterConditionLogic(PriceCalculationDTO priceDTO) {
		if (priceDTO.getAdrSurchargeMinimum().doubleValue() > priceDTO
				.getCalculatedAdrSurcharge().doubleValue()) {
			priceDTO.setResultingPriceSurcharge(priceDTO
					.getAdrSurchargeMinimum());
		} else {
			priceDTO.setResultingPriceSurcharge(priceDTO
					.getCalculatedAdrSurcharge());
		}
	}

	/**
	 * Calculates the address surcharge.
	 * 
	 * @param condition
	 */
	protected void calculateAddressSurcharge(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(Double.parseDouble(condition
						.getValue()));
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = CalcUtil.roundBigDecimal(result);
		priceDTO.setCalculatedAdrSurcharge(result);
	}

	protected void calculateChfSurchargePrice(PriceCalculationDTO priceDTO,
			Configuration config) throws RateFileException {
		CurrencyRate chfRate = getCurrencyService().getChfRateForCurrentPrice(
				config.getCurrentChfValue());
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(chfRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = CalcUtil.roundBigDecimal(result);
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
	protected void calculateDieselSurchargePrice(PriceCalculationDTO priceDTO,
			Configuration config) throws RateFileException {
		DieselRate dieselRate = getDieselService()
				.getDieselRateForCurrentPrice(config.getCurrentDieselPrice());
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(dieselRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = CalcUtil.roundBigDecimal(result);
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
}
