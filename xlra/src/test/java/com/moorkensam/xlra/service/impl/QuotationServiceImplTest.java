package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.mapper.OfferteEmailParameterGenerator;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.EmailResult;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public class QuotationServiceImplTest extends UnitilsJUnit4 {

	private QuotationServiceImpl quotationService;

	@Mock
	private OfferteEmailParameterGenerator mapper;

	private PriceCalculationDTO priceDTO;

	@Mock
	private EmailTemplateDAO emailTemplateDAO;

	private MailTemplate template;

	@Mock
	private TemplateEngine engine;

	private QuotationQuery query;

	private PriceResultDTO resultDTO;

	@Before
	public void init() {
		query = new QuotationQuery();
		query.setCustomer(new BaseCustomer());
		query.getCustomer().setEmail("test@test.com");
		query.setCountry(new Country());
		quotationService = new QuotationServiceImpl();
		resultDTO = new PriceResultDTO();
		priceDTO = new PriceCalculationDTO();
		priceDTO.setAppliedOperations(new ArrayList<TranslationKey>());
		template = new MailTemplate();
		template.setTemplate("test template + ${detailCalculation}");
		template.setSubject("SUBJECT");
		quotationService.setOfferteEmailParameterGenerator(mapper);
		quotationService.setEmailTemplateDAO(emailTemplateDAO);
		quotationService.setTemplatEngine(engine);
	}

	@Test
	public void tesTCopyRTransientLangToPersistent() {
		QuotationResult result = new QuotationResult();
		result.setEmailResult(new EmailResult());
		result.setQuery(query);
		result.getQuery().setResultLanguage(Language.FR);
		quotationService.copyTransientResultLanguageToLanguageIfNeeded(result);

		Assert.assertEquals(Language.FR, result.getQuery().getLanguage());
	}

	@Test
	public void testInitOfferteEmail() throws TemplatingException,
			RateFileException {
		RateFile rf = new RateFile();
		query.setResultLanguage(Language.NL);
		mapper.fillInParameters(priceDTO, resultDTO, "REF-001");
		EasyMock.expectLastCall();
		EasyMock.expect(
				emailTemplateDAO.getMailTemplateForLanguage(Language.NL))
				.andReturn(template);
		EasyMock.expect(engine.createOfferteEmailTemplateParams(query, resultDTO))
				.andReturn(new HashMap<String, Object>());
		EasyMock.expect(
				engine.parseEmailTemplate(template.getTemplate(),
						new HashMap<String, Object>())).andReturn(
				"test template + filled in");
		EasyMockUnitils.replay();

		OfferteMailDTO dto = new OfferteMailDTO();
		QuotationResult result = new QuotationResult();
		result.setQuery(query);
		result.setOfferteUniqueIdentifier("REF-001");

		quotationService.initializeOfferteEmail(result, dto, rf, priceDTO);
		Assert.assertNotNull(dto);
		Assert.assertEquals("SUBJECT", dto.getSubject());
		Assert.assertEquals("test@test.com", dto.getAddress());
	}

	@Test
	public void testCreateSearchFilterForquery() {
		query.setCountry(new Country());
		query.getCountry().setShortName("BE");
		query.setMeasurement(Measurement.KILO);
		query.setKindOfRate(Kind.NORMAL);

		RateFileSearchFilter filter = quotationService
				.createRateFileSearchFilterForQuery(query);
		Assert.assertNotNull(filter);
		Assert.assertEquals("BE", filter.getCountry().getShortName());
		Assert.assertEquals(Measurement.KILO, filter.getMeasurement());
		Assert.assertEquals(Kind.NORMAL, filter.getRateKind());
	}

	@Test
	public void testCreateCreateSearchFilterForQueryWithFullCustomer() {
		query.setCountry(new Country());
		query.getCountry().setShortName("BE");
		query.setMeasurement(Measurement.KILO);
		query.setKindOfRate(Kind.NORMAL);
		query.setCustomer(new FullCustomer());
		query.getCustomer().setName("FULL");

		RateFileSearchFilter filter = quotationService
				.createRateFileSearchFilterForQuery(query);
		Assert.assertNotNull(filter);
		Assert.assertEquals("FULL", filter.getCustomer().getName());
		Assert.assertNull(filter.getCountry());
		Assert.assertNull(filter.getMeasurement());
		Assert.assertNull(filter.getRateKind());
	}

	@Test
	public void testCreateBaseSearchFilterForQueryWithFullCustomer() {
		query.setCountry(new Country());
		query.getCountry().setShortName("BE");
		query.setMeasurement(Measurement.KILO);
		query.setKindOfRate(Kind.NORMAL);
		query.setCustomer(new FullCustomer());
		query.getCustomer().setName("FULL");

		RateFileSearchFilter filter = quotationService
				.createBaseRateFileSearchFilterForQuery(query);
		Assert.assertNotNull(filter);
		Assert.assertNull(filter.getCustomer());
		Assert.assertNotNull(filter.getCountry());
		Assert.assertNotNull(filter.getMeasurement());
		Assert.assertNotNull(filter.getRateKind());
	}
}
