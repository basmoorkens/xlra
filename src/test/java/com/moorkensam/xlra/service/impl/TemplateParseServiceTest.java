package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
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

public class TemplateParseServiceTest extends UnitilsJUnit4 {

	@TestedObject
	private TemplateParseServiceImpl templateEngine;

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
		String generatedMail = templateEngine.parseUserCreatedTemplate(user);
		Assert.assertTrue(generatedMail.contains("bas moorkens"));
	}

	@Test
	public void testParsePdfOfferteTemplate() throws TemplatingException {
		QuotationResult result = new QuotationResult();
		result.setCreatedUserFullName("basie");
		QuotationQuery query = new QuotationQuery();
		result.setQuery(query);
		query.setQuotationDate(new Date());
		query.setCountry(new Country());
		query.getCountry().setNames(new HashMap<Language, String>());
		query.getCountry().setDutchName("Belgie");
		query.setPostalCode("2222");
		query.setQuantity(10d);
		query.setMeasurement(Measurement.PALET);
		query.setKindOfRate(Kind.EXPRES);
		PriceCalculation calculation = new PriceCalculation();
		calculation.setAppliedOperations(Arrays.asList(
				TranslationKey.DIESEL_SURCHARGE, TranslationKey.EXPORT_FORM,
				TranslationKey.IMPORT_FORM, TranslationKey.CHF_SURCHARGE,
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
		String generatedEmail = templateEngine.parseOffertePdf(result,
				Language.NL);

		Assert.assertNotNull(generatedEmail);
		Assert.assertTrue(generatedEmail.contains("Belgie"));
		Assert.assertTrue(generatedEmail.contains("2222"));
		Assert.assertTrue(generatedEmail.contains("Expres"));
	}

	@Test
	public void testParseTemplate() throws TemplatingException {
		String template = "Dear ${customer}, your offerte is here. please see attached pdf.";
		QuotationResult offerte = new QuotationResult();
		offerte.setQuery(new QuotationQuery());
		offerte.getQuery().setCustomer(new Customer());
		offerte.getQuery().getCustomer().setName("test");
		offerte.getQuery().setQuantity(10d);
		offerte.getQuery().setMeasurement(Measurement.KILO);
		offerte.getQuery().setCountry(new Country());
		offerte.getQuery().getCountry().setShortName("BE");
		offerte.getQuery().getCountry()
				.setNames(new HashMap<Language, String>());
		offerte.getQuery().setResultLanguage(Language.NL);
		offerte.getQuery().setPostalCode("2222");
		offerte.setOfferteUniqueIdentifier("UQ11");
		String result = templateEngine.parseOfferteEmailTemplate(template,
				offerte, "fulldetailblabla", "additioanlConditiosn");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.contains("test"));
	}

	@Test
	public void testParseHtmlFullDetail() throws TemplatingException {
		PriceCalculation calculation = new PriceCalculation();
		calculation.setBasePrice(new BigDecimal(100d));
		calculation.setResultingPriceSurcharge(new BigDecimal(50d));
		calculation.setDieselPrice(new BigDecimal(10d));
		calculation.setChfPrice(new BigDecimal(10d));
		calculation.setImportFormalities(new BigDecimal(30d));
		calculation.setExportFormalities(new BigDecimal(20d));
		calculation.setAppliedOperations(Arrays.asList(
				TranslationKey.ADR_SURCHARGE, TranslationKey.DIESEL_SURCHARGE,
				TranslationKey.EXPORT_FORM, TranslationKey.IMPORT_FORM,
				TranslationKey.CHF_SURCHARGE));
		OfferteOptionDTO o1 = new OfferteOptionDTO();
		o1.setCalculationOption(true);
		o1.setShowToCustomer(true);
		o1.setKey(TranslationKey.IMPORT_FORM);
		o1.setSelected(true);
		o1.setI8nKey("calculation.fulldetail.import.formalities");
		OfferteOptionDTO o2 = new OfferteOptionDTO();
		o2.setCalculationOption(true);
		o2.setShowToCustomer(true);
		o2.setKey(TranslationKey.CHF_SURCHARGE);
		o2.setI8nKey("calculation.fulldetail.swiss.franc.surcharge");
		o2.setSelected(true);
		String result = templateEngine.parseHtmlFullDetailCalculation(
				Arrays.asList(o1, o2), calculation, Language.NL);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.contains("Basis prijs:"));
		Assert.assertTrue(result.contains("Import formaliteiten:"));
	}

	@Test
	public void testParseAdditionalConditions() throws TemplatingException {
		OfferteOptionDTO o1 = new OfferteOptionDTO();
		o1.setI8nKey("calculation.fulldetail.tarif.franco.house");
		o1.setSelected(true);
		o1.setKey(TranslationKey.TARIF_FRANCO_HOUSE);
		o1.setValue("");
		o1.setShowToCustomer(true);
		OfferteOptionDTO o2 = new OfferteOptionDTO();
		o2.setI8nKey("calculation.fulldetail.wait.tarif");
		o2.setKey(TranslationKey.WACHT_TARIF);
		o2.setSelected(true);
		o2.setShowToCustomer(true);
		o2.setValue("55 EUR/h");
		OfferteOptionDTO o3 = new OfferteOptionDTO();
		o3.setSelected(true);
		o3.setI8nKey("calculation.fulldetail.transport.insurance");
		o3.setKey(TranslationKey.TRANSPORT_INSURANCE);
		o3.setShowToCustomer(true);
		o3.setValue("CMR insurance (all risk on request)");

		String result = templateEngine.parseHtmlAdditionalConditions(
				Arrays.asList(o1, o2, o3), Language.EN);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.contains("CMR insurance"));
		Assert.assertTrue(result.contains("Transport insurance"));
		Assert.assertTrue(result.contains("55 EUR/h"));
		Assert.assertTrue(result.contains("Wait hour tarif:"));
	}

}
