package com.moorkensam.xlra.service.impl;

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

import com.moorkensam.xlra.dao.PriceCalculationDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.mapper.PriceCalculationToHtmlConverter;
import com.moorkensam.xlra.mapper.OfferteEmailToEmailResultMapper;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.MailTemplateService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.RateFileService;

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

	private IdentityService identityService;

	private PriceCalculationToHtmlConverter offerteEmailParameterGenerator;

	private OfferteEmailToEmailResultMapper mailMapper;

	@PostConstruct
	public void init() {
		setOfferteEmailParameterGenerator(new PriceCalculationToHtmlConverter());
		mailMapper = new OfferteEmailToEmailResultMapper();
		identityService = IdentityService.getInstance();
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
		QuotationResult quotationResult = initializeQuotationResult(query);
		RateLine result;
		try {
			RateFile rf = rateFileService.getRateFileForQuery(query);
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			PriceCalculation calculatedPrice = calculationService
					.calculatePriceAccordingToConditions(result.getValue(),
							rf.getCountry(), rf.getConditions(), query);
			quotationResult.setCalculation(calculatedPrice);

			OfferteMailDTO dto = mailTemplateService.initializeOfferteEmail(
					quotationResult, rf, quotationResult.getCalculation());
			fillInQuotationResult(dto, quotationResult);
		} catch (RateFileException e1) {
			logger.error(e1.getBusinessException() + e1.getMessage());
			throw e1;
		} catch (TemplatingException e) {
			logger.error("Failed to parse Template" + e.getMessage());
			throw new RateFileException("Failed to parse email template.");
		}
		// generate pdf
		return quotationResult;
	}

	private QuotationResult initializeQuotationResult(QuotationQuery query) {
		QuotationResult quotationResult = new QuotationResult();
		quotationResult.setQuery(query);
		fillInMailLanguage(query);
		quotationResult.setOfferteUniqueIdentifier(identityService
				.getNextIdentifier());
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

	/**
	 * This method fetches the email templates from the database and then parses
	 * it with the given parameters.
	 * 
	 * @param query
	 * @param dto
	 * @param rf
	 * @param result
	 * @throws TemplatingException
	 */

	public PriceCalculationToHtmlConverter getOfferteEmailParameterGenerator() {
		return offerteEmailParameterGenerator;
	}

	public void setOfferteEmailParameterGenerator(
			PriceCalculationToHtmlConverter mapper) {
		this.offerteEmailParameterGenerator = mapper;
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
		createAndSaveFullOfferte(result);
		try {
			getEmailService().sendOfferteMail(result);
		} catch (MessagingException e) {
			logger.error("Failed to send offerte email");
			throw new RateFileException("Failed to send email");
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
}
