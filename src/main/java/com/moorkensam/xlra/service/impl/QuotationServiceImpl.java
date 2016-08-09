package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.PriceCalculationDao;
import com.moorkensam.xlra.dao.QuotationQueryDao;
import com.moorkensam.xlra.dao.QuotationResultDao;
import com.moorkensam.xlra.model.error.PdfException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.error.UnAuthorizedAccessException;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.offerte.QuotationResultStatus;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.AuthorizationService;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.LogRecordFactoryService;
import com.moorkensam.xlra.service.LogService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.QuotationUtil;

import com.itextpdf.text.DocumentException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.SortOrder;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

@Stateless
public class QuotationServiceImpl implements QuotationService {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  private QuotationQueryDao quotationDao;

  @Inject
  private QuotationResultDao quotationResultDao;

  @Inject
  private PriceCalculationDao priceCalculationDao;

  @Inject
  private EmailService emailService;

  @Inject
  private MailTemplateService mailTemplateService;

  @Inject
  private CalculationService calculationService;

  @Inject
  private RateFileService rateFileService;

  @Inject
  private PdfService pdfService;

  @Inject
  private UserSessionService userSessionService;

  @Inject
  private LogService logService;

  @Inject
  private LogRecordFactoryService logFactoryService;

  @Inject
  private AuthorizationService authorizationService;

  private FileService fileService;

  private IdentityService identityService;

  private QuotationUtil quotationUtil;

  /**
   * Inits for the service.
   */
  @PostConstruct
  public void init() {
    identityService = IdentityService.getInstance();
    fileService = new FileServiceImpl();
    setQuotationUtil(QuotationUtil.getInstance());
  }

  @Override
  public void createQuotationQuery(QuotationQuery quotation) {
    logger.info("Creating quotation query: " + quotation);
    getQuotationDao().createQuotationQuery(quotation);
  }

  @Override
  public QuotationQuery updateQuotationQuery(QuotationQuery quotation) {
    return getQuotationDao().updateQuotationQuery(quotation);
  }

  @Override
  public List<QuotationQuery> getAllQuotationQueries() {
    return getQuotationDao().getAllQuotationQueries();
  }

  @Override
  public void createQuotationResult(QuotationResult result) {
    logger.info("Creating quotation result");
    getQuotationResultDao().createQuotationResult(result);
  }

  @Override
  public QuotationResult updateQuotationResult(QuotationResult result) {
    return getQuotationResultDao().updateQuotationResult(result);
  }

  @Override
  public List<QuotationResult> getAllQuotationResults() {
    return getQuotationResultDao().getAllQuotationResults();
  }

  @Override
  public QuotationResult generateQuotationResultForQuotationQuery(final QuotationQuery query)
      throws RateFileException {
    QuotationResult offerte = initializeQuotationResult(query);
    fillInOfferteFromRateFile(query, offerte);
    return offerte;
  }

  private void fillInOfferteFromRateFile(final QuotationQuery query, QuotationResult offerte)
      throws RateFileException {
    RateLine result;
    try {
      RateFile rf = rateFileService.getRateFileForQuery(query);
      result = rf.getRateLineForQuantityAndPostalCode(query.getQuantity(), query.getPostalCode());
      List<OfferteOptionDto> options =
          quotationUtil.generateOfferteOptionsForRateFileAndLanguage(rf, offerte.getQuery()
              .getResultLanguage());
      offerte.setSelectableOptions(options);
      offerte.getCalculation().setBasePrice(result.getValue());
      offerte.setUsedRateFileName(rf.getName());
      quotationUtil.setupEmailRecipients(offerte);
    } catch (RateFileException e1) {
      logger.error(e1.getBusinessException() + e1.getMessage());
      throw e1;
    }
  }

  @Override
  public QuotationResult generateEmailAndPdfForOfferte(QuotationResult offerte)
      throws RateFileException {
    try {
      PriceCalculation calculatedPrice =
          calculationService.calculatePriceAccordingToConditions(offerte);
      offerte.setCalculation(calculatedPrice);
      EmailResult dto = mailTemplateService.initializeOfferteEmail(offerte);
      offerte.setEmailResult(dto);
      pdfService.generateTransientOffertePdf(offerte, offerte.getQuery().getResultLanguage());
      offerte.setQuotationResultStatus(QuotationResultStatus.PROCESSED_OPTIONS);
    } catch (TemplatingException e) {
      logger.error("Failed to parse Template" + e.getMessage());
      throw new RateFileException("Failed to parse email template.");
    } catch (FileNotFoundException | DocumentException e) {
      logger.error("Failed to create PDF." + e.getMessage());
      throw new RateFileException("Failed to generate pdf");
    }
    return offerte;
  }

  private QuotationResult initializeQuotationResult(QuotationQuery query) {
    QuotationResult quotationResult = new QuotationResult();
    quotationResult.setQuery(query);
    fillInMailLanguage(query);
    quotationResult.setOfferteUniqueIdentifier(identityService.getNextIdentifier());
    User loggedInUser = userSessionService.getLoggedInUser();
    quotationResult.setCreatedUserFullName(loggedInUser.getFullName());
    PriceCalculation calculatedPrice = new PriceCalculation();
    quotationResult.setCalculation(calculatedPrice);
    query.setQuotationDate(new Date());
    quotationResult.setQuotationResultStatus(QuotationResultStatus.PROCESSED_RATE_FILTERS);
    return quotationResult;
  }

  /**
   * This method sets the language used to generate the email. If the frontend was filled in
   * (quotationquery) it takes that language, else it takes the language filled in when creating the
   * customer.
   * 
   * @param query The query to fill the language into.
   */
  protected void fillInMailLanguage(QuotationQuery query) {
    query.setResultLanguage(query.getLanguage() != null ? query.getLanguage() : query.getCustomer()
        .getLanguage());
  }

  @Override
  public void submitQuotationResult(QuotationResult result) throws RateFileException {
    copyTransientResultLanguageToLanguageIfNeeded(result);
    try {
      User user = userSessionService.getLoggedInUser();
      createAndSaveFullOfferte(result, user);
      logOfferteSubmit(result, user);
      result.setPdfFileName(fileService.convertTransientOfferteToFinal(result
          .getOfferteUniqueIdentifier()));
      getEmailService().sendOfferteMail(result);
    } catch (MessagingException e) {
      logger.error("Failed to send offerte email");
      throw new RateFileException("Failed to send email");
    } catch (PdfException e) {
      throw new RateFileException(e.getBusinessException());
    }
  }

  private void logOfferteSubmit(final QuotationResult result, final User user) {
    LogRecord log = getLogFactoryService().createOfferteLogRecord(result);
    logService.createLogRecord(log);
    logger.info(user.getUserName() + " created offerte " + result.getOfferteUniqueIdentifier());
  }


  private void createAndSaveFullOfferte(final QuotationResult offerte, final User user) {
    offerte.getQuery().setQuotationDate(new Date());
    offerte.setCreatedUserName(user.getUserName());
    QuotationQuery managedQuery = getQuotationDao().createQuotationQuery(offerte.getQuery());
    offerte.setQuery(managedQuery);
    PriceCalculation managedCalculation =
        priceCalculationDao.createCalculation(offerte.getCalculation());
    offerte.setCalculation(managedCalculation);
    offerte.setQuotationResultStatus(QuotationResultStatus.FINALIZED);
    getQuotationResultDao().createQuotationResult(offerte);
  }

  /**
   * If no language was selected for the quotation query then this method copies the calculated
   * language to that field. Its just done to improve the data readability in the db so each
   * quotationrecord has a language.
   * 
   * @param result The result to fill the language into.
   */
  protected void copyTransientResultLanguageToLanguageIfNeeded(QuotationResult result) {
    if (result.getQuery().getLanguage() == null) {
      result.getQuery().setLanguage(result.getQuery().getResultLanguage());
    }
  }

  public IdentityService getIdentityService() {
    return identityService;
  }

  public void setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
  }

  public QuotationQueryDao getQuotationDao() {
    return quotationDao;
  }

  public void setQuotationDao(QuotationQueryDao quotationDao) {
    this.quotationDao = quotationDao;
  }

  public QuotationResultDao getQuotationResultDao() {
    return quotationResultDao;
  }

  public void setQuotationResultDao(QuotationResultDao quotationResultDao) {
    this.quotationResultDao = quotationResultDao;
  }

  public EmailService getEmailService() {
    return emailService;
  }

  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }

  public RateFileService getRateFileService() {
    return rateFileService;
  }

  public void setRateFileService(RateFileService rateFileService) {
    this.rateFileService = rateFileService;
  }

  public CalculationService getCalculationService() {
    return calculationService;
  }

  public void setCalculationService(CalculationService calculationService) {
    this.calculationService = calculationService;
  }

  public MailTemplateService getMailTemplateService() {
    return mailTemplateService;
  }

  public void setMailTemplateService(MailTemplateService mailTemplateService) {
    this.mailTemplateService = mailTemplateService;
  }

  @Override
  public List<QuotationResult> getQuotationQueries(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, String> filters) {
    return quotationResultDao.getQuotationResults(first, pageSize, sortField, sortOrder, filters);
  }

  @Override
  public int getQuotationQueryCount(Map<String, String> filters) {
    return quotationResultDao.getQuotationResultCount(filters);
  }

  public PriceCalculationDao getPriceCalculationDao() {
    return priceCalculationDao;
  }

  public void setPriceCalculationDao(PriceCalculationDao priceCalculationDao) {
    this.priceCalculationDao = priceCalculationDao;
  }

  public PdfService getPdfService() {
    return pdfService;
  }

  public void setPdfService(PdfService pdfService) {
    this.pdfService = pdfService;
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public QuotationUtil getQuotationUtil() {
    return quotationUtil;
  }

  public void setQuotationUtil(QuotationUtil quotationUtil) {
    this.quotationUtil = quotationUtil;
  }

  @Override
  public QuotationResult getFullOfferteById(Long id) throws UnAuthorizedAccessException {
    QuotationResult offerte = quotationResultDao.getQuotationResultById(id);
    authorizationService.authorizeOfferteAccess(offerte);
    return offerte;
  }

  @Override
  public QuotationResult getOfferteByOfferteKey(String offerteKey)
      throws UnAuthorizedAccessException {
    QuotationResult offerte = quotationResultDao.getOfferteByKey(offerteKey);
    authorizationService.authorizeOfferteAccess(offerte);
    return offerte;
  }

  @Override
  public List<QuotationResult> getLazyloadedOffertes(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters) {
    return quotationResultDao.getLazyloadedOffertes(first, pageSize, sortField, sortOrder, filters);
  }

  @Override
  public int countOffertes() {
    return quotationResultDao.countOffertes();
  }

  public LogService getLogService() {
    return logService;
  }

  public void setLogService(LogService logService) {
    this.logService = logService;
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

  public LogRecordFactoryService getLogFactoryService() {
    return logFactoryService;
  }

  public void setLogFactoryService(LogRecordFactoryService logFactoryService) {
    this.logFactoryService = logFactoryService;
  }

  public AuthorizationService getAuthorizationService() {
    return authorizationService;
  }

  public void setAuthorizationService(AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }
}
