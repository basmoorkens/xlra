package com.moorkensam.xlra.service.impl;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import com.itextpdf.text.DocumentException;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.PriceCalculationDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.mapper.OfferteEmailToEmailResultMapper;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.PdfException;
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
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.RateFileService;

public class QuotationServiceImplTest extends UnitilsJUnit4 {

	private QuotationServiceImpl quotationService;

	private PriceCalculation priceDTO;

	@Mock
	private EmailService mailService;

	@Mock
	private IdentityService idService;

	@Mock
	private RateFileService rateFileService;

	@Mock
	private EmailTemplateDAO emailTemplateDAO;

	private MailTemplate template;

	@Mock
	private QuotationQueryDAO quotationDAO;

	@Mock
	private MailTemplateService mailTemplateService;

	@Mock
	private PdfService pdfService;

	@Mock
	private QuotationResultDAO quotationResultDAO;

	@Mock
	private RateFileService rfService;

	@Mock
	private RateFile rfMock;

	@Mock
	private FileService fileService;

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
		Country country = new Country();
		country.setShortName("BE");
		query.setCountry(country);
		quotationService = new QuotationServiceImpl();
		priceDTO = new PriceCalculation();
		priceDTO.setAppliedOperations(new ArrayList<TranslationKey>());
		template = new MailTemplate();
		template.setTemplate("test template + ${detailCalculation}");
		template.setSubject("SUBJECT");
		quotationService.setPriceCalculationDAO(calculationDAO);
		quotationService.setQuotationDAO(quotationDAO);
		quotationService.setQuotationResultDAO(quotationResultDAO);
		quotationService.setEmailService(mailService);
		quotationService.setRateFileService(rfService);
		quotationService.setCalculationService(calcService);
		quotationService.setMailTemplateService(mailTemplateService);
		quotationService.setMailMapper(mailMapper);
		quotationService.setIdentityService(idService);
		quotationService.setRateFileService(rateFileService);
		quotationService.setFileService(fileService);
		quotationService.setPdfService(pdfService);
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
	public void testInitService() {
		quotationService.init();
		Assert.assertNotNull(quotationService.getMailMapper());
		Assert.assertNotNull(quotationService.getIdentityService());
	}

	@Test
	public void testSubmitOfferte() throws RateFileException,
			MessagingException, PdfException {
		QuotationResult result = new QuotationResult();
		result.setOfferteUniqueIdentifier("uq123");
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
		EasyMock.expect(fileService.convertTransientOfferteToFinal("uq123"))
				.andReturn("uq123.pdf");

		mailService.sendOfferteMail(result);
		EasyMock.expectLastCall();

		EasyMockUnitils.replay();
		quotationService.submitQuotationResult(result);

	}

	@Test
	public void testGenerateQuotationResult() throws RateFileException,
			TemplatingException, FileNotFoundException, DocumentException {
		String uqId = "12345";
		RateLine rl = new RateLine();
		QuotationResult result = new QuotationResult();
		result.setQuery(query);
		OfferteMailDTO offerteMailDto = new OfferteMailDTO();
		result.setOfferteUniqueIdentifier(uqId);
		EmailResult mailResult = new EmailResult();
		List<Condition> conditions = new ArrayList<Condition>();
		PriceCalculation priceCalculation = new PriceCalculation();
		rl.setValue(new BigDecimal(100d));
		EasyMock.expect(idService.getNextIdentifier()).andReturn(uqId);
		EasyMock.expect(rateFileService.getRateFileForQuery(query)).andReturn(
				rfMock);
		EasyMock.expect(
				rfMock.getRateLineForQuantityAndPostalCode(query.getQuantity(),
						query.getPostalCode())).andReturn(rl);
		EasyMock.expect(rfMock.getCountry()).andReturn(query.getCountry());
		EasyMock.expect(rfMock.getConditions()).andReturn(conditions);
		EasyMock.expect(
				calcService.calculatePriceAccordingToConditions(rl.getValue(),
						query.getCountry(), conditions, query)).andReturn(
				priceCalculation);
		EasyMock.expect(
				mailTemplateService.initializeOfferteEmail(result, rfMock,
						priceCalculation)).andReturn(offerteMailDto);
		EasyMock.expect(mailMapper.map(offerteMailDto)).andReturn(mailResult);
		pdfService.generateTransientOffertePdf(result,
				query.getResultLanguage());
		EasyMock.expectLastCall();

		EasyMockUnitils.replay();
		QuotationResult offerte = quotationService
				.generateQuotationResultForQuotationQuery(query);

		Assert.assertNotNull(offerte);
		Assert.assertNotNull(offerte.getQuery());
		Assert.assertNotNull(offerte.getCalculation());
		Assert.assertNotNull(offerte.getEmailResult());
	}

	@Test
	public void testCreateQuotationResult() {
		QuotationResult offerte = new QuotationResult();
		quotationResultDAO.createQuotationResult(offerte);
		EasyMockUnitils.replay();

		quotationService.createQuotationResult(offerte);
	}

	@Test
	public void testCreateQuotationQuery() {
		QuotationQuery q = new QuotationQuery();
		EasyMock.expect(quotationDAO.createQuotationQuery(q)).andReturn(q);
		EasyMockUnitils.replay();

		quotationService.createQuotationQuery(q);
		Assert.assertNotNull(q);
	}
}
