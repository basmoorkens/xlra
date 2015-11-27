package com.moorkensam.xlra.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
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

}
