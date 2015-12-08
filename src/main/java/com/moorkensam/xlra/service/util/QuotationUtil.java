package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.CalculationValueType;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class QuotationUtil {

	private final static Logger logger = LogManager.getLogger();

	private static QuotationUtil instance;

	private TranslationKeyToi8nMapper mapper;

	private QuotationUtil() {

	}

	public static QuotationUtil getInstance() {
		if (instance == null) {
			instance = new QuotationUtil();
			instance.setMapper(new TranslationKeyToi8nMapper());
		}
		return instance;
	}

	/**
	 * Fills in the search params. If the customer is filled in and is of type
	 * fullCustomer that is filled in. Otherwise the country / measurement /
	 * ratekind is filled in.
	 * 
	 * @param query
	 * @return
	 */
	public RateFileSearchFilter createRateFileSearchFilterForQuery(
			QuotationQuery query, boolean ignoreFullCustomerIfPresent) {
		RateFileSearchFilter filter = new RateFileSearchFilter();
		if (ignoreFullCustomerIfPresent) {
			fillInBaseFilterProperties(query, filter);
		} else {
			if (query.getCustomer() instanceof Customer) {
				filter.setCustomer(query.getCustomer());
			} else {
				fillInBaseFilterProperties(query, filter);
			}
		}
		return filter;
	}

	private void fillInBaseFilterProperties(QuotationQuery query,
			RateFileSearchFilter filter) {
		filter.setCountry(query.getCountry());
		filter.setMeasurement(query.getMeasurement());
		filter.setRateKind(query.getKindOfRate());
		filter.setTransportationType(query.getTransportType());
	}

	public List<OfferteOptionDTO> generateOfferteOptionsForRateFileAndLanguage(
			RateFile rf, Language language) {
		List<OfferteOptionDTO> options = new ArrayList<OfferteOptionDTO>();
		for (Condition c : rf.getConditions()) {
			OfferteOptionDTO option = new OfferteOptionDTO();
			option.setKey(c.getConditionKey());
			option.setSelected(c.isStandardSelected());
			option.setI8nKey(mapper.map(option.getKey()));
			option.setCalculationOption(isCalculationKey(option.getKey()));
			if (option.isCalculationOption()) {
				option.setValue(c.getValue());
			} else {
				option.setValue(c.getTranslationForLanguage(language)
						.getTranslation());
			}
			option.setShowToCustomer(isShowToCustomer(option.getKey()));
			options.add(option);
		}
		return options;
	}

	public OfferteOptionDTO createCalculationOption(TranslationKey key,
			BigDecimal value) {
		OfferteOptionDTO option = new OfferteOptionDTO();
		option.setSelected(true);
		option.setKey(key);
		option.setI8nKey(mapper.map(option.getKey()));
		option.setCalculationOption(true);
		option.setValue(value + "");
		option.setShowToCustomer(isShowToCustomer(option.getKey()));
		return option;
	}

	public boolean isShowToCustomer(TranslationKey key) {
		if (key == TranslationKey.ADR_MINIMUM) {
			return false;
		}
		return true;
	}

	public boolean isCalculationKey(TranslationKey key) {
		switch (key) {
		case ADR_SURCHARGE:
			return true;
		case ADR_MINIMUM:
			return true;
		case DIESEL_SURCHARGE:
			return true;
		case CHF_SURCHARGE:
			return true;
		case IMPORT_FORM:
			return true;
		case EXPORT_FORM:
			return true;
		default:
			return false;
		}
	}

	public CalculationValueType getCalculationValueTypeForCalculationKey(
			TranslationKey key) {
		switch (key) {
		case ADR_SURCHARGE:
			return CalculationValueType.PERCENTAGE;
		case ADR_MINIMUM:
			return CalculationValueType.CONSTANT;
		case DIESEL_SURCHARGE:
			return CalculationValueType.CONSTANT;
		case CHF_SURCHARGE:
			return CalculationValueType.CONSTANT;
		case IMPORT_FORM:
			return CalculationValueType.CONSTANT;
		case EXPORT_FORM:
			return CalculationValueType.CONSTANT;
		default:
			return null;
		}
	}

	public boolean offerteOptionsContainsKey(List<OfferteOptionDTO> options,
			TranslationKey key) {
		for (OfferteOptionDTO o : options) {
			if (o.getKey() == key) {
				return true;
			}
		}
		return false;
	}

	public TranslationKeyToi8nMapper getMapper() {
		return mapper;
	}

	public void setMapper(TranslationKeyToi8nMapper mapper) {
		this.mapper = mapper;
	}

}
