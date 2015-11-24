package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.mail.MessagingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.PriceCalculationDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dao.impl.PriceCalculationDAOImpl;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.mapper.PriceCalculationToHtmlConverter;
import com.moorkensam.xlra.mapper.OfferteEmailToEmailResultMapper;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.RateFileService;

public class QuotationServiceImplTest extends UnitilsJUnit4 {

	private QuotationServiceImpl quotationService;

	@Mock
	private PriceCalculationToHtmlConverter mapper;

	private PriceCalculation priceDTO;

	@Mock
	private EmailService mailService;

	@Mock
	private EmailTemplateDAO emailTemplateDAO;

	private MailTemplate template;

	@Mock
	private QuotationQueryDAO quotationDAO;

	@Mock
	private MailTemplateService mailTemplateService;

	@Mock
	private QuotationResultDAO quotationResultDAO;

	@Mock
	private RateFileService rfService;

	@Mock
	private RateFile rfMock;

	@Mock
	private OfferteEmailToEmailResultMapper mailMapper;

	@Mock
	private CalculationService calcService;

	@Mock
	private PriceCalculationDAO calculationDAO;

	private QuotationQuery query;


	@Before
	public void init() {
		query = new QuotationQuery();
		query.setCustomer(new Customer());
		query.getCustomer().setEmail("test@test.com");
		query.setCountry(new Country());
		quotationService = new QuotationServiceImpl();
		priceDTO = new PriceCalculation();
		priceDTO.setAppliedOperations(new ArrayList<TranslationKey>());
		template = new MailTemplate();
		template.setTemplate("test template + ${detailCalculation}");
		template.setSubject("SUBJECT");
		quotationService.setPriceCalculationDAO(calculationDAO);
		quotationService.setOfferteEmailParameterGenerator(mapper);
		quotationService.setQuotationDAO(quotationDAO);
		quotationService.setQuotationResultDAO(quotationResultDAO);
		quotationService.setEmailService(mailService);
		quotationService.setRateFileService(rfService);
		quotationService.setCalculationService(calcService);
		quotationService.setMailTemplateService(mailTemplateService);
		quotationService.setMailMapper(mailMapper);
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

	// @Test
	// public void testgenerateQuotationResultForQuotationQuery()
	// throws RateFileException, TemplatingException {
	// PriceCalculationDTO priceDTO = new PriceCalculationDTO();
	// QuotationQuery query = new QuotationQuery();
	// query.setQuantity(10d);
	// query.setPostalCode("2220");
	// query.setLanguage(Language.NL);
	// rfMock.setCountry(new Country());
	// rfMock.setConditions(new ArrayList<Condition>());
	// OfferteMailDTO mailDto = new OfferteMailDTO();
	// EasyMock.expect(rfService.getRateFileForQuery(query)).andReturn(rfMock);
	// RateLine rl = new RateLine();
	// rl.setValue(new BigDecimal(100d));
	// EasyMock.expect(
	// rfMock.getRateLineForQuantityAndPostalCode(query.getQuantity(),
	// query.getPostalCode())).andReturn(rl);
	// priceDTO.setBasePrice(rl.getValue());
	// calcService.calculatePriceAccordingToConditions(priceDTO,
	// rfMock.getCountry(), rfMock.getConditions(), query);
	// EasyMock.expectLastCall();
	//
	// mailTemplateService.initializeOfferteEmail(
	// (QuotationResult) EasyMock.anyObject(), mailDto, rfMock,
	// priceDTO);
	// EasyMock.expectLastCall();
	// EasyMock.expect(mailMapper.map(mailDto)).andReturn(new EmailResult());
	// EasyMockUnitils.replay();
	//
	// quotationService.generateQuotationResultForQuotationQuery(query);
	//
	// }

	@Test
	public void testSubmitOfferte() throws RateFileException,
			MessagingException {
		QuotationResult result = new QuotationResult();
		PriceCalculation calculation = new PriceCalculation();
		result.setCalculation(calculation);
		result.setQuery(query);

		EasyMock.expect(quotationDAO.createQuotationQuery(query)).andReturn(
				query);
		query.setId(1);
		EasyMock.expect(calculationDAO.createCalculation(calculation))
				.andReturn(calculation);
		quotationResultDAO.createQuotationResult(result);
		EasyMock.expectLastCall();

		mailService.sendOfferteMail(result);
		EasyMock.expectLastCall();

		EasyMockUnitils.replay();
		quotationService.submitQuotationResult(result);

	}
}
