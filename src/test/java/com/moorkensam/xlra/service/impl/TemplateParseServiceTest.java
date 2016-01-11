package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Address;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class TemplateParseServiceTest extends UnitilsJUnit4 {

  @TestedObject
  private TemplateParseServiceImpl templateEngine;

  /**
   * Setup the test.
   */
  @Before
  public void setup() {
    templateEngine = TemplateParseServiceImpl.getInstance();
    templateEngine.setStringTemplateLoader(new StringTemplateLoader());
    templateEngine.setConfiguration(new Configuration());
    templateEngine.setConfigLoader(ConfigurationLoader.getInstance());
  }

  @Test
  public void testParseUSerCreatedTemplate() throws TemplatingException {
    User user = new User();
    user.setFirstName("bas");
    user.setName("moorkens");
    user.setUserName("bmoork");
    String generatedMail = templateEngine.parseUserCreatedTemplate(user);
    Assert.assertTrue(generatedMail.contains("bas moorkens"));
    Assert.assertTrue(generatedMail.contains("bmoork"));
  }

  @Test
  public void testParsePdfOfferteTemplate() throws TemplatingException {
    QuotationResult result = new QuotationResult();
    result.setCreatedUserFullName("basie");
    QuotationQuery query = new QuotationQuery();
    result.setOfferteUniqueIdentifier("2015-123456");
    result.setQuery(query);
    query.setQuotationDate(new Date());
    query.setCountry(new Country());
    query.getCountry().setNames(new HashMap<Language, String>());
    query.getCountry().setDutchName("Belgie");
    query.setPostalCode("2222");
    query.setQuantity(10d);
    query.setMeasurement(Measurement.PALET);
    query.setKindOfRate(Kind.EXPRES);
    Customer customer = new Customer();
    query.setCustomer(customer);
    customer.setAddress(new Address());
    customer.getAddress().setCity("heist");
    customer.getAddress().setZip("2220");
    customer.getAddress().setNumber("10");
    customer.getAddress().setStreet("merelstraat");
    customer.setName("Bas test");
    PriceCalculation calculation = new PriceCalculation();
    calculation.setAppliedOperations(Arrays.asList(TranslationKey.DIESEL_SURCHARGE,
        TranslationKey.EXPORT_FORM, TranslationKey.IMPORT_FORM, TranslationKey.CHF_SURCHARGE,
        TranslationKey.ADR_SURCHARGE));
    calculation.setBasePrice(new BigDecimal(100d));
    calculation.setChfPrice(new BigDecimal(10d));
    calculation.setAdrSurchargeMinimum(new BigDecimal(15d));
    calculation.setCalculatedAdrSurcharge(new BigDecimal(20d));
    calculation.setDieselPrice(new BigDecimal(10d));
    calculation.setExportFormalities(new BigDecimal(10d));
    calculation.setImportFormalities(new BigDecimal(10d));
    calculation.calculateTotalPrice();
    result.setCalculation(calculation);
    String generatedEmail = templateEngine.parseOffertePdf(result, Language.NL);

    Assert.assertNotNull(generatedEmail);
    Assert.assertTrue(generatedEmail.contains("Belgie"));
    Assert.assertTrue(generatedEmail.contains("2222"));
    Assert.assertTrue(generatedEmail.contains("Expres"));
  }

  @Test
  public void testParseTemplate() throws TemplatingException {
    QuotationResult offerte = new QuotationResult();
    offerte.setQuery(new QuotationQuery());
    offerte.getQuery().setCustomer(new Customer());
    offerte.getQuery().getCustomer().setName("test");
    offerte.getQuery().setQuantity(10d);
    offerte.getQuery().setMeasurement(Measurement.KILO);
    offerte.getQuery().setCountry(new Country());
    offerte.getQuery().getCountry().setShortName("BE");
    offerte.getQuery().getCountry().setNames(new HashMap<Language, String>());
    offerte.getQuery().setResultLanguage(Language.NL);
    offerte.getQuery().setPostalCode("2222");
    offerte.setOfferteUniqueIdentifier("UQ11");
    String template = "Dear ${customer}, your offerte is here. please see attached pdf.";
    String result = templateEngine.parseOfferteEmailTemplate(template, offerte);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.contains("test"));
  }

  @Test
  public void testParseUserResetPasswordEmail() throws TemplatingException {
    User user = new User();
    user.setFirstName("bas");
    user.setName("mo");
    user.setUserName("bmo");
    String result = templateEngine.parseUserResetPasswordEmail(user);
    Assert.assertTrue(result.contains("bas mo"));
  }

}
