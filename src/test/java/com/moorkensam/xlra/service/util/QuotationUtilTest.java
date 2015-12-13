package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
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

    RateFileSearchFilter filter =
        quotationUtil.createRateFileSearchFilterForQuery(quotationQuery, false);
    Assert.assertNotNull(filter);
    Assert.assertEquals(filter.getCustomer(), fc);
  }

  @Test
  public void testCreateRateFileSearchFilterForNormalQuery() {
    Country country = new Country();
    country.setShortName("kl");
    quotationQuery.setCountry(country);
    quotationQuery.setMeasurement(Measurement.KILO);
    quotationQuery.setTransportType(TransportType.EXPORT);
    quotationQuery.setKindOfRate(Kind.EXPRES);
    RateFileSearchFilter filter =
        quotationUtil.createRateFileSearchFilterForQuery(quotationQuery, false);
    Assert.assertNotNull(filter);
    Assert.assertEquals(quotationQuery.getCountry(), filter.getCountry());
    Assert.assertEquals(quotationQuery.getMeasurement(), filter.getMeasurement());
    Assert.assertEquals(quotationQuery.getTransportType(), filter.getTransportationType());
    Assert.assertEquals(quotationQuery.getKindOfRate(), filter.getRateKind());
  }

  @Test
  public void testgenerateOfferteOptionsForRateFile() {
    Condition c1 = new Condition();
    c1.setConditionKey(TranslationKey.ADR_MINIMUM);
    Condition c2 = new Condition();
    c2.setConditionKey(TranslationKey.EUR1_DOCUMENT);
    c2.addTranslation(Language.NL, "test");
    Condition c3 = new Condition();
    c3.setConditionKey(TranslationKey.IMPORT_FORM);
    RateFile rf = new RateFile();
    rf.setConditions(Arrays.asList(c1, c2, c3));

    List<OfferteOptionDto> options =
        quotationUtil.generateOfferteOptionsForRateFileAndLanguage(rf, Language.NL);
    Assert.assertNotNull(options);
    Assert.assertEquals("test", options.get(1).getValue());
    Assert.assertEquals(3, options.size());
    Assert.assertTrue(options.get(0).getKey() == TranslationKey.ADR_MINIMUM);
    Assert.assertFalse(options.get(0).isSelected());
  }

  @Test
  public void testOFferteOPtionsContainsKey() {
    OfferteOptionDto option = new OfferteOptionDto();
    option.setKey(TranslationKey.ADR_MINIMUM);
    OfferteOptionDto option2 = new OfferteOptionDto();
    option.setKey(TranslationKey.DIESEL_SURCHARGE);
    Assert.assertTrue(quotationUtil.offerteOptionsContainsKey(Arrays.asList(option, option2),
        TranslationKey.DIESEL_SURCHARGE));
    Assert.assertFalse(quotationUtil.offerteOptionsContainsKey(Arrays.asList(option, option2),
        TranslationKey.CHF_SURCHARGE));
  }

  @Test
  public void testIsShowToCustomer() {
    OfferteOptionDto option = new OfferteOptionDto();
    option.setKey(TranslationKey.ADR_MINIMUM);
    Assert.assertFalse(quotationUtil.isShowToCustomer(option.getKey()));
    OfferteOptionDto option2 = new OfferteOptionDto();
    option.setKey(TranslationKey.DIESEL_SURCHARGE);
    Assert.assertTrue(quotationUtil.isShowToCustomer(option2.getKey()));
  }

  @Test
  public void testCreateOption() {
    OfferteOptionDto option =
        quotationUtil
            .createCalculationOption(TranslationKey.DIESEL_SURCHARGE, new BigDecimal(100d));
    Assert.assertNotNull(option);
    Assert.assertEquals(true, option.isCalculationOption());
    Assert.assertTrue(option.isShowToCustomer());
  }

}
