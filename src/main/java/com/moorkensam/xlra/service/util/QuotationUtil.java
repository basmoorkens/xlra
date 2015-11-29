package com.moorkensam.xlra.service.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;

public class QuotationUtil {

	private final static Logger logger = LogManager.getLogger();

	private static QuotationUtil instance;

	private QuotationUtil() {

	}

	public static QuotationUtil getInstance() {
		if (instance == null) {
			instance = new QuotationUtil();
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

	public List<OfferteOptionDTO> generateOfferteOptionsForRateFile(RateFile rf) {
		List<OfferteOptionDTO> options = new ArrayList<OfferteOptionDTO>();
		for (Condition c : rf.getConditions()) {
			OfferteOptionDTO option = new OfferteOptionDTO();
			option.setKey(c.getConditionKey());
			option.setSelected(false);
			option.setValue(c.getValue());
			options.add(option);
		}
		return options;
	}

}
