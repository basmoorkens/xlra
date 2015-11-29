package com.moorkensam.xlra.service.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class QuotationUtilTest extends UnitilsJUnit4 {

	@TestedObject
	private QuotationUtil quotationUtil;

	private QuotationQuery quotationQuery;

	@Before
	public void init() {
		quotationUtil = QuotationUtil.getInstance();
		quotationQuery = new QuotationQuery();
	}

	@Test
	public void testCreateRateFileSearchFilterForQueryForFullCustomer() {
		Customer fc = new Customer();
		fc.setName("basfull");
		quotationQuery.setCustomer(fc);

		RateFileSearchFilter filter = quotationUtil
				.createRateFileSearchFilterForQuery(quotationQuery, false);
		Assert.assertNotNull(filter);
		Assert.assertEquals(filter.getCustomer(), fc);
	}

	@Test
	public void testCreateRateFileSearchFilterForNormalQuery() {
		Country c = new Country();
		c.setShortName("kl");
		quotationQuery.setCountry(c);
		quotationQuery.setMeasurement(Measurement.KILO);
		quotationQuery.setTransportType(TransportType.EXPORT);
		quotationQuery.setKindOfRate(Kind.EXPRES);
		RateFileSearchFilter filter = quotationUtil
				.createRateFileSearchFilterForQuery(quotationQuery, false);
		Assert.assertNotNull(filter);
		Assert.assertEquals(quotationQuery.getCountry(), filter.getCountry());
		Assert.assertEquals(quotationQuery.getMeasurement(),
				filter.getMeasurement());
		Assert.assertEquals(quotationQuery.getTransportType(),
				filter.getTransportationType());
		Assert.assertEquals(quotationQuery.getKindOfRate(),
				filter.getRateKind());
	}

	@Test
	public void testgenerateOfferteOptionsForRateFile() {
		RateFile rf = new RateFile();
		Condition c1 = new Condition();
		c1.setConditionKey(TranslationKey.ADR_MINIMUM);
		Condition c2 = new Condition();
		c2.setConditionKey(TranslationKey.EUR1_DOCUMENT);
		Condition c3 = new Condition();
		c3.setConditionKey(TranslationKey.IMPORT_FORM);
		rf.setConditions(Arrays.asList(c1, c2, c3));

		List<OfferteOptionDTO> options = quotationUtil
				.generateOfferteOptionsForRateFile(rf);
		Assert.assertNotNull(options);
		Assert.assertEquals(3, options.size());
		Assert.assertTrue(options.get(0).getKey() == TranslationKey.ADR_MINIMUM);
		Assert.assertFalse(options.get(0).isSelected());
	}

}
