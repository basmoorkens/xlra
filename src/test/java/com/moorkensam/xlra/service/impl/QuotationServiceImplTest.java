package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.EmailTemplateDao;
import com.moorkensam.xlra.dao.PriceCalculationDao;
import com.moorkensam.xlra.dao.QuotationQueryDao;
import com.moorkensam.xlra.dao.QuotationResultDao;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.PdfException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.generator.UserGenerator;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.LogRecordFactory;
import com.moorkensam.xlra.service.util.QuotationUtil;

import com.itextpdf.text.DocumentException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.MessagingException;

public class QuotationServiceImplTest extends UnitilsJUnit4 {

  private QuotationServiceImpl quotationService;

  private PriceCalculation priceDto;

  @Mock
  private EmailService mailService;

  @Mock
  private IdentityService idService;

  @Mock
  private RateFileService rateFileService;

  @Mock
  private EmailTemplateDao emailTemplateDao;

  private MailTemplate template;

  @Mock
  private QuotationQueryDao quotationDao;

  @Mock
  private MailTemplateService mailTemplateService;

  @Mock
  private PdfService pdfService;

  @Mock
  private QuotationResultDao quotationResultDao;

  @Mock
  private RateFileService rfService;

  @Mock
  private RateFile rfMock;

  @Mock
  private QuotationUtil quotationUtil;

  @Mock
  private FileService fileService;

  @Mock
  private CalculationService calcService;

  @Mock
  private PriceCalculationDao calculationDao;

  @Mock
  private UserSessionService userSessionService;

  @Mock
  private LogRecordFactory logRecordFactory;

  @Mock
  private LogService logService;

  private QuotationQuery query;

  /**
   * init the test.
   */
  @Before
  public void init() {
    query = new QuotationQuery();
    query.setCustomer(new Customer());
    query.getCustomer().getStandardContact().setEmail("test@test.com");
    Country country = new Country();
    country.setShortName("BE");
    query.setCountry(country);
    quotationService = new QuotationServiceImpl();
    priceDto = new PriceCalculation();
    priceDto.setAppliedOperations(new ArrayList<TranslationKey>());
    template = new MailTemplate();
    template.setTemplate("test template + ${detailCalculation}");
    template.setSubject("SUBJECT");
    quotationService.setPriceCalculationDao(calculationDao);
    quotationService.setUserSessionService(userSessionService);
    quotationService.setQuotationDao(quotationDao);
    quotationService.setQuotationResultDao(quotationResultDao);
    quotationService.setEmailService(mailService);
    quotationService.setRateFileService(rfService);
    quotationService.setCalculationService(calcService);
    quotationService.setMailTemplateService(mailTemplateService);
    quotationService.setIdentityService(idService);
    quotationService.setRateFileService(rateFileService);
    quotationService.setFileService(fileService);
    quotationService.setPdfService(pdfService);
    quotationService.setQuotationUtil(quotationUtil);
    quotationService.setLogService(logService);
    quotationService.setLogFactory(logRecordFactory);
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
    Assert.assertNotNull(quotationService.getIdentityService());
  }

  @Test
  public void testSubmitOfferte() throws RateFileException, MessagingException, PdfException {
    User user = UserGenerator.getStandardUser();
    QuotationResult result = new QuotationResult();
    result.setOfferteUniqueIdentifier("uq123");
    PriceCalculation calculation = new PriceCalculation();
    result.setCalculation(calculation);
    result.setQuery(query);

    EasyMock.expect(quotationDao.createQuotationQuery(query)).andReturn(query);
    query.setId(1);
    EasyMock.expect(calculationDao.createCalculation(calculation)).andReturn(calculation);
    quotationResultDao.createQuotationResult(result);
    EasyMock.expectLastCall();
    EasyMock.expect(userSessionService.getLoggedInUser())
        .andReturn(UserGenerator.getStandardUser());
    EasyMock.expect(fileService.convertTransientOfferteToFinal("uq123")).andReturn("uq123.pdf");
    LogRecord log = new QuotationLogRecord();
    EasyMock.expect(logRecordFactory.createOfferteLogRecord(result, user.getUserName())).andReturn(
        log);
    logService.createLogRecord(log);
    EasyMock.expectLastCall();
    mailService.sendOfferteMail(result);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();
    quotationService.submitQuotationResult(result);

  }

  @Test
  public void testGenerateQuotationResult() throws RateFileException, TemplatingException,
      FileNotFoundException, DocumentException {
    String uqId = "12345";
    QuotationResult result = new QuotationResult();
    result.setQuery(query);
    query.setLanguage(Language.NL);
    result.setOfferteUniqueIdentifier(uqId);
    RateLine rl = new RateLine();
    rl.setValue(new BigDecimal(100d));
    OfferteOptionDto option = new OfferteOptionDto();
    option.setKey(TranslationKey.ADR_MINIMUM);
    option.setSelected(true);
    option.setValue("200d");
    EasyMock.expect(idService.getNextIdentifier()).andReturn(uqId);
    User user = UserGenerator.getStandardUser();
    EasyMock.expect(userSessionService.getLoggedInUser()).andReturn(user);
    EasyMock.expect(rateFileService.getRateFileForQuery(query)).andReturn(rfMock);
    EasyMock.expect(
        rfMock.getRateLineForQuantityAndPostalCode(query.getQuantity(), query.getPostalCode()))
        .andReturn(rl);
    EasyMock
        .expect(quotationUtil.generateOfferteOptionsForRateFileAndLanguage(rfMock, Language.NL))
        .andReturn(Arrays.asList(option));
    EasyMock.expect(rfMock.getName()).andReturn("test rf");
    quotationUtil.setupEmailRecipients(EasyMock.isA(QuotationResult.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();
    QuotationResult offerte = quotationService.generateQuotationResultForQuotationQuery(query);

    Assert.assertNotNull(offerte);
    Assert.assertNotNull(offerte.getQuery());
    Assert.assertNotNull(offerte.getCalculation());
    Assert.assertEquals("test rf", offerte.getUsedRateFileName());
    Assert.assertEquals("bas moorkens", offerte.getCreatedUserFullName());
  }

  @Test
  public void testGenerateMailAndPdf() throws TemplatingException, RateFileException,
      FileNotFoundException, DocumentException {
    QuotationResult result = new QuotationResult();
    result.setCalculation(new PriceCalculation());
    result.setQuery(new QuotationQuery());
    result.getQuery().setResultLanguage(Language.NL);
    result.getCalculation().setBasePrice(new BigDecimal(100d));
    PriceCalculation newCalc = new PriceCalculation();
    newCalc.setBasePrice(new BigDecimal(100d));
    newCalc.setDieselPrice(new BigDecimal(10d));
    newCalc.setFinalPrice(new BigDecimal(110d));

    EmailResult mailResult = new EmailResult();
    EasyMock.expect(calcService.calculatePriceAccordingToConditions(result)).andReturn(newCalc);
    EasyMock.expect(mailTemplateService.initializeOfferteEmail(result)).andReturn(mailResult);
    pdfService.generateTransientOffertePdf(result, query.getResultLanguage());
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();
    QuotationResult offerte = quotationService.generateEmailAndPdfForOfferte(result);

    Assert.assertNotNull(offerte.getEmailResult());
  }

  @Test
  public void testCreateQuotationResult() {
    QuotationResult offerte = new QuotationResult();
    quotationResultDao.createQuotationResult(offerte);
    EasyMockUnitils.replay();

    quotationService.createQuotationResult(offerte);
  }

  @Test
  public void testCreateQuotationQuery() {
    QuotationQuery query = new QuotationQuery();
    EasyMock.expect(quotationDao.createQuotationQuery(query)).andReturn(query);
    EasyMockUnitils.replay();

    quotationService.createQuotationQuery(query);
    Assert.assertNotNull(query);
  }
}
