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
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.mapper.OfferteEmailParameterGenerator;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.EmailService;

public class MailTemplateServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private MailTemplateServiceImpl mailTemplateService;

	@Mock
	private OfferteEmailParameterGenerator mapper;

	private PriceCalculationDTO priceDTO;

	@Mock
	private EmailService mailService;

	@Mock
	private EmailTemplateDAO emailTemplateDAO;

	@Mock
	private TemplateEngine templateEngine;

	private MailTemplate template;

	private QuotationQuery query;

	private PriceResultDTO resultDTO;

	@Before
	public void init() {
		query = new QuotationQuery();
		query.setCustomer(new BaseCustomer());
		query.getCustomer().setEmail("test@test.com");
		query.setCountry(new Country());
		resultDTO = new PriceResultDTO();
		priceDTO = new PriceCalculationDTO();
		priceDTO.setAppliedOperations(new ArrayList<TranslationKey>());
		template = new MailTemplate();
		template.setTemplate("test template + ${detailCalculation}");
		template.setSubject("SUBJECT");
		mailTemplateService = new MailTemplateServiceImpl();
		mailTemplateService.setMailTemplateDAO(emailTemplateDAO);
		mailTemplateService.setTemplateEngine(templateEngine);
		mailTemplateService.setOfferteEmailParameterGenerator(mapper);
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
		EasyMock.expect(
				templateEngine.createOfferteEmailTemplateParams(query,
						resultDTO)).andReturn(new HashMap<String, Object>());
		EasyMock.expect(
				templateEngine.parseOfferteEmailTemplate(
						template.getTemplate(), new HashMap<String, Object>()))
				.andReturn("test template + filled in");
		EasyMockUnitils.replay();

		OfferteMailDTO dto = new OfferteMailDTO();
		QuotationResult result = new QuotationResult();
		result.setQuery(query);
		result.setOfferteUniqueIdentifier("REF-001");

		mailTemplateService.initializeOfferteEmail(result, dto, rf, priceDTO);
		Assert.assertNotNull(dto);
		Assert.assertEquals("SUBJECT", dto.getSubject());
		Assert.assertEquals("test@test.com", dto.getAddress());
	}

}
