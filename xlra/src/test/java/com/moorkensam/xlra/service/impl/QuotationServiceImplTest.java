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
import com.moorkensam.xlra.mapper.PriceCalculationDTOToResultMapper;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public class QuotationServiceImplTest extends UnitilsJUnit4 {

	private QuotationServiceImpl quotationService;

	@Mock
	private PriceCalculationDTOToResultMapper mapper;

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
		quotationService.setMapper(mapper);
		quotationService.setEmailTemplateDAO(emailTemplateDAO);
		quotationService.setTemplatEngine(engine);
	}

	@Test
	public void testInitOfferteEmail() throws TemplatingException,
			RateFileException {
		RateFile rf = new RateFile();
		mapper.map(priceDTO, resultDTO);
		EasyMock.expectLastCall();
		EasyMock.expect(
				emailTemplateDAO.getMailTemplateForLanguage(Language.NL))
				.andReturn(template);
		EasyMock.expect(engine.createTemplateParams(query, resultDTO))
				.andReturn(new HashMap<String, Object>());
		EasyMock.expect(
				engine.parseEmailTemplate(template.getTemplate(),
						new HashMap<String, Object>())).andReturn(
				"test template + filled in");
		EasyMockUnitils.replay();

		OfferteMailDTO dto = new OfferteMailDTO();

		quotationService.initializeOfferteEmail(query, dto, rf, priceDTO);
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
}
