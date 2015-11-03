package com.moorkensam.xlra.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.management.remote.NotificationResult;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.dto.PriceResultDTO;
import com.moorkensam.xlra.mapper.OfferteEmailToEmailResultMapper;
import com.moorkensam.xlra.mapper.OfferteEmailParameterGenerator;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.QuotationService;

@Stateless
public class QuotationServiceImpl implements QuotationService {

	private final static Logger logger = LogManager.getLogger();

	private TemplateEngine templatEngine;

	@Inject
	private EmailTemplateDAO emailTemplateDAO;

	@Inject
	private RateFileDAO rateFileDAO;

	@Inject
	private QuotationQueryDAO quotationDAO;

	@Inject
	private QuotationResultDAO quotationResultDAO;

	@Inject
	private EmailService emailService;

	@Inject
	private CalculationService calculationService;

	@Inject
	private TemplateEngine templateEngine;

	private IdentityService identityService;

	private OfferteEmailParameterGenerator offerteEmailParameterGenerator;

	private OfferteEmailToEmailResultMapper mailMapper;

	@PostConstruct
	public void init() {
		setTemplatEngine(templateEngine);
		setOfferteEmailParameterGenerator(new OfferteEmailParameterGenerator());
		mailMapper = new OfferteEmailToEmailResultMapper();
		identityService = IdentityService.getInstance();
	}

	@Override
	public void createQuotationQuery(QuotationQuery quotation) {
		logger.info("Creating quotation query: " + quotation);
		quotationDAO.createQuotationQuery(quotation);
	}

	@Override
	public QuotationQuery updateQuotationQuery(QuotationQuery quotation) {
		return quotationDAO.updateQuotationQuery(quotation);
	}

	@Override
	public List<QuotationQuery> getAllQuotationQueries() {
		return quotationDAO.getAllQuotationQueries();
	}

	@Override
	public void createQuotationResult(QuotationResult result) {
		logger.info("Creating quotation result");
		quotationResultDAO.createQuotationResult(result);
	}

	@Override
	public QuotationResult updateQuotationResult(QuotationResult result) {
		return quotationResultDAO.updateQuotationResult(result);
	}

	@Override
	public List<QuotationResult> getAllQuotationResults() {
		return quotationResultDAO.getAllQuotationResults();
	}

	@Override
	public QuotationResult generateQuotationResultForQuotationQuery(
			QuotationQuery query) throws RateFileException {
		OfferteMailDTO dto = new OfferteMailDTO();
		QuotationResult quotationResult = initializeQuotationResult(query);
		RateLine result;
		PriceCalculationDTO priceDTO = new PriceCalculationDTO();
		try {
			RateFile rf = getRateFileForQuery(query);
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			priceDTO.setBasePrice(result.getValue());
			calculationService.calculatePriceAccordingToConditions(priceDTO,
					rf.getCountry(), rf.getConditions(), query);
			initializeOfferteEmail(quotationResult, dto, rf, priceDTO);
			fillInQuotationResult(query, dto, quotationResult);
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

	private RateFile getRateFileForQuery(QuotationQuery query)
			throws RateFileException {
		RateFileSearchFilter firstFilter = createRateFileSearchFilterForQuery(query);
		RateFile rf = null;
		try {
			rf = rateFileDAO.getFullRateFileForFilter(firstFilter);
		} catch (NoResultException nre) {
			try {
				RateFileSearchFilter fallBackFilter = createBaseRateFileSearchFilterForQuery(query);
				rf = rateFileDAO.getFullRateFileForFilter(fallBackFilter);
			} catch (NoResultException nre2) {
				throw new RateFileException(
						"Could not find ratefile for searchfilter "
								+ firstFilter);
			}
		}
		return rf;
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

	private void fillInQuotationResult(QuotationQuery query,
			OfferteMailDTO dto, QuotationResult quotationResult) {
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

	protected void initializeOfferteEmail(QuotationResult result,
			OfferteMailDTO dto, RateFile rf, PriceCalculationDTO priceDTO)
			throws TemplatingException, RateFileException {
		PriceResultDTO resultDTO = new PriceResultDTO();
		offerteEmailParameterGenerator.fillInParameters(priceDTO, resultDTO,
				result.getOfferteUniqueIdentifier());
		try {
			MailTemplate template = getEmailTemplateDAO()
					.getMailTemplateForLanguage(
							result.getQuery().getResultLanguage());
			Map<String, Object> templateParameters = templatEngine
					.createOfferteEmailTemplateParams(result.getQuery(), resultDTO);
			String emailMessage = getTemplatEngine().parseOfferteEmailTemplate(
					template.getTemplate(), templateParameters);
			dto.setAddress(result.getQuery().getCustomer().getEmail());
			dto.setSubject(template.getSubject());
			dto.setContent(emailMessage);
		} catch (NoResultException nre) {
			logger.error("Could not find email template for "
					+ result.getQuery().getResultLanguage());
			throw new RateFileException(
					"Could not find email template for language "
							+ result.getQuery().getResultLanguage());
		}

	}

	/**
	 * Fills in the search params. If the customer is filled in and is of type
	 * fullCustomer that is filled in. Otherwise the country / measurement /
	 * ratekind is filled in.
	 * 
	 * @param query
	 * @return
	 */
	protected RateFileSearchFilter createRateFileSearchFilterForQuery(
			QuotationQuery query) {
		RateFileSearchFilter filter = new RateFileSearchFilter();
		if (query.getCustomer() instanceof FullCustomer) {
			filter.setCustomer(query.getCustomer());
		} else {
			fillInBaseFilterProperties(query, filter);
		}
		return filter;
	}

	private void fillInBaseFilterProperties(QuotationQuery query,
			RateFileSearchFilter filter) {
		filter.setCountry(query.getCountry());
		filter.setMeasurement(query.getMeasurement());
		filter.setRateKind(query.getKindOfRate());
		filter.setTransportationType(query.getTransportType());
	}

	/**
	 * This method creates the searchfilter and ignores a full customer if it
	 * would be present. Its used as the fallback search mechanism for when a
	 * fullcustomer is selected but does not have its own ratefile.
	 * 
	 * @param query
	 * @return
	 */
	protected RateFileSearchFilter createBaseRateFileSearchFilterForQuery(
			QuotationQuery query) {
		logger.warn("Could not find customer ratefile for "
				+ query.getCustomer().getName()
				+ ". Falling back to base ratefile search.");
		RateFileSearchFilter filter = new RateFileSearchFilter();
		fillInBaseFilterProperties(query, filter);
		return filter;
	}

	public OfferteEmailParameterGenerator getOfferteEmailParameterGenerator() {
		return offerteEmailParameterGenerator;
	}

	public void setOfferteEmailParameterGenerator(
			OfferteEmailParameterGenerator mapper) {
		this.offerteEmailParameterGenerator = mapper;
	}

	public EmailTemplateDAO getEmailTemplateDAO() {
		return emailTemplateDAO;
	}

	public void setEmailTemplateDAO(EmailTemplateDAO emailTemplateDAO) {
		this.emailTemplateDAO = emailTemplateDAO;
	}

	public TemplateEngine getTemplatEngine() {
		return templatEngine;
	}

	public void setTemplatEngine(TemplateEngine templatEngine) {
		this.templatEngine = templatEngine;
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
		QuotationQuery managedQuery = quotationDAO.createQuotationQuery(result
				.getQuery());
		result.setQuery(managedQuery);
		quotationResultDAO.createQuotationResult(result);
		try {
			emailService.sendOfferteMail(result);
		} catch (MessagingException e) {
			logger.error("Failed to send offerte email");
			throw new RateFileException("Failed to send email");
		}
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
}
