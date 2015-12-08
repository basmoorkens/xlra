package com.moorkensam.xlra.service.impl;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.itextpdf.text.DocumentException;
import com.moorkensam.xlra.dao.PriceCalculationDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.mapper.OfferteEmailToEmailResultMapper;
import com.moorkensam.xlra.model.error.PdfException;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.QuotationUtil;

@Stateless
public class QuotationServiceImpl implements QuotationService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private QuotationQueryDAO quotationDAO;

	@Inject
	private QuotationResultDAO quotationResultDAO;

	@Inject
	private PriceCalculationDAO priceCalculationDAO;

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
	private UserService userService;

	private FileService fileService;

	private IdentityService identityService;

	private OfferteEmailToEmailResultMapper mailMapper;

	private QuotationUtil quotationUtil;

	@PostConstruct
	public void init() {
		mailMapper = new OfferteEmailToEmailResultMapper();
		identityService = IdentityService.getInstance();
		fileService = new FileServiceImpl();
		setQuotationUtil(QuotationUtil.getInstance());
	}

	@Override
	public void createQuotationQuery(QuotationQuery quotation) {
		logger.info("Creating quotation query: " + quotation);
		getQuotationDAO().createQuotationQuery(quotation);
	}

	@Override
	public QuotationQuery updateQuotationQuery(QuotationQuery quotation) {
		return getQuotationDAO().updateQuotationQuery(quotation);
	}

	@Override
	public List<QuotationQuery> getAllQuotationQueries() {
		return getQuotationDAO().getAllQuotationQueries();
	}

	@Override
	public void createQuotationResult(QuotationResult result) {
		logger.info("Creating quotation result");
		getQuotationResultDAO().createQuotationResult(result);
	}

	@Override
	public QuotationResult updateQuotationResult(QuotationResult result) {
		return getQuotationResultDAO().updateQuotationResult(result);
	}

	@Override
	public List<QuotationResult> getAllQuotationResults() {
		return getQuotationResultDAO().getAllQuotationResults();
	}

	@Override
	public QuotationResult generateQuotationResultForQuotationQuery(
			QuotationQuery query) throws RateFileException {
		QuotationResult offerte = initializeQuotationResult(query);
		query.setQuotationDate(new Date());
		RateLine result;
		try {
			RateFile rf = rateFileService.getRateFileForQuery(query);
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			List<OfferteOptionDTO> options = quotationUtil
					.generateOfferteOptionsForRateFileAndLanguage(rf,
							offerte.getQuery().getResultLanguage());
			offerte.setSelectableOptions(options);
			PriceCalculation calculatedPrice = new PriceCalculation();
			calculatedPrice.setBasePrice(result.getValue());
			offerte.setCalculation(calculatedPrice);
		} catch (RateFileException e1) {
			logger.error(e1.getBusinessException() + e1.getMessage());
			throw e1;
		}
		return offerte;
	}

	@Override
	public QuotationResult generateEmailAndPdfForOfferte(QuotationResult offerte)
			throws RateFileException {
		try {
			PriceCalculation calculatedPrice = calculationService
					.calculatePriceAccordingToConditions(offerte);
			offerte.setCalculation(calculatedPrice);
			OfferteMailDTO dto = mailTemplateService
					.initializeOfferteEmail(offerte);
			fillInQuotationResult(dto, offerte);
			pdfService.generateTransientOffertePdf(offerte, offerte.getQuery()
					.getResultLanguage());
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
		quotationResult.setOfferteUniqueIdentifier(identityService
				.getNextIdentifier());
		User loggedInUser = userService.getUserByUserName(userService
				.getCurrentUsername());
		quotationResult.setCreatedUserFullName(loggedInUser.getFullName());
		return quotationResult;
	}

	/**
	 * This method sets the language used to generate the email. If the frontend
	 * was filled in (quotationquery) it takes that language, else it takes the
	 * language filled in when creating the customer.
	 * 
	 * @param query
	 */
	protected void fillInMailLanguage(QuotationQuery query) {
		query.setResultLanguage(query.getLanguage() != null ? query
				.getLanguage() : query.getCustomer().getLanguage());
	}

	private void fillInQuotationResult(OfferteMailDTO dto,
			QuotationResult quotationResult) {
		quotationResult.setEmailResult(mailMapper.map(dto));
	}

	public OfferteEmailToEmailResultMapper getMailMapper() {
		return mailMapper;
	}

	public void setMailMapper(OfferteEmailToEmailResultMapper mailMapper) {
		this.mailMapper = mailMapper;
	}

	@Override
	public void submitQuotationResult(QuotationResult result)
			throws RateFileException {
		copyTransientResultLanguageToLanguageIfNeeded(result);
		try {
			createAndSaveFullOfferte(result);
			result.setPdfFileName(fileService
					.convertTransientOfferteToFinal(result
							.getOfferteUniqueIdentifier()));
			getEmailService().sendOfferteMail(result);
		} catch (MessagingException e) {
			logger.error("Failed to send offerte email");
			throw new RateFileException("Failed to send email");
		} catch (PdfException e) {
			throw new RateFileException(e.getBusinessException());
		}
	}

	private void createAndSaveFullOfferte(QuotationResult offerte) {
		offerte.getQuery().setQuotationDate(new Date());
		QuotationQuery managedQuery = getQuotationDAO().createQuotationQuery(
				offerte.getQuery());
		offerte.setQuery(managedQuery);
		PriceCalculation managedCalculation = priceCalculationDAO
				.createCalculation(offerte.getCalculation());
		offerte.setCalculation(managedCalculation);
		getQuotationResultDAO().createQuotationResult(offerte);
	}

	/**
	 * If no language was selected for the quotation query then this method
	 * copies the calculated language to that field. Its just done to improve
	 * the data readability in the db so each quotationrecord has a language.
	 * 
	 * @param result
	 */
	protected void copyTransientResultLanguageToLanguageIfNeeded(
			QuotationResult result) {
		if (result.getQuery().getLanguage() == null) {
			result.getQuery()
					.setLanguage(result.getQuery().getResultLanguage());
		}
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public QuotationQueryDAO getQuotationDAO() {
		return quotationDAO;
	}

	public void setQuotationDAO(QuotationQueryDAO quotationDAO) {
		this.quotationDAO = quotationDAO;
	}

	public QuotationResultDAO getQuotationResultDAO() {
		return quotationResultDAO;
	}

	public void setQuotationResultDAO(QuotationResultDAO quotationResultDAO) {
		this.quotationResultDAO = quotationResultDAO;
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
	public List<QuotationResult> getQuotationQueries(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		return quotationResultDAO.getQuotationResults(first, pageSize,
				sortField, sortOrder, filters);
	}

	@Override
	public int getQuotationQueryCount(Map<String, String> filters) {
		return quotationResultDAO.getQuotationResultCount(filters);
	}

	@Override
	public List<QuotationResult> getQuotationResultsForFilters(
			OfferteSearchFilter filter) {
		return quotationResultDAO.getQuotationResultsForFilter(filter);
	}

	public PriceCalculationDAO getPriceCalculationDAO() {
		return priceCalculationDAO;
	}

	public void setPriceCalculationDAO(PriceCalculationDAO priceCalculationDAO) {
		this.priceCalculationDAO = priceCalculationDAO;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public QuotationUtil getQuotationUtil() {
		return quotationUtil;
	}

	public void setQuotationUtil(QuotationUtil quotationUtil) {
		this.quotationUtil = quotationUtil;
	}

	@Override
	public QuotationResult getFullOfferteById(Long id) {
		return quotationResultDAO.getQuotationResultById(id);
	}

	@Override
	public QuotationResult getOfferteByOfferteKey(String offerteKey) {
		return quotationResultDAO.getOfferteByKey(offerteKey);
	}
}
