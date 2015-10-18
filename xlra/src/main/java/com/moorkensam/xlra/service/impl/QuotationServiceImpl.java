package com.moorkensam.xlra.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
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
import com.moorkensam.xlra.mapper.PriceCalculationDTOToResultMapper;
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

	private PriceCalculationDTOToResultMapper mapper;

	@PostConstruct
	public void init() {
		setTemplatEngine(TemplateEngine.getInstance());
		setMapper(new PriceCalculationDTOToResultMapper());
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
		RateFileSearchFilter filter = createRateFileSearchFilterForQuery(query);
		RateLine result;
		PriceCalculationDTO priceDTO = new PriceCalculationDTO();

		try {
			RateFile rf = rateFileDAO.getFullRateFileForFilter(filter);
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			priceDTO.setBasePrice(result.getValue());
			calculationService.calculatePriceAccordingToConditions(priceDTO,
					rf.getCountry(), rf.getConditions(), query);
			initializeOfferteEmail(query, dto, rf, priceDTO);
			emailService.sendOfferteMail(dto);
		} catch (NoResultException nre) {
			throw new RateFileException("Could not find ratefile for "
					+ filter.toString());
		} catch (RateFileException e1) {
			logger.error(e1.getBusinessException() + e1.getMessage());
			throw e1;
		} catch (TemplatingException e) {
			logger.error("Failed to parse Template" + e.getMessage());
			throw new RateFileException("Failed to parse email template.");
		} catch (MessagingException e) {
			logger.error("Failed to send offerte email");
			throw new RateFileException("Failed to send email");
		}
		// generate pdf
		return null;
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

	protected void initializeOfferteEmail(QuotationQuery query,
			OfferteMailDTO dto, RateFile rf, PriceCalculationDTO priceDTO)
			throws TemplatingException, RateFileException {
		PriceResultDTO resultDTO = new PriceResultDTO();
		getMapper().map(priceDTO, resultDTO);

		try {
			MailTemplate template = getEmailTemplateDAO()
					.getMailTemplateForLanguage(rf.getLanguage());
			Map<String, Object> templateParameters = templatEngine
					.createTemplateParams(query, resultDTO);
			String emailMessage = getTemplatEngine().parseEmailTemplate(
					template.getTemplate(), templateParameters);
			dto.setAddress(query.getCustomer().getEmail());
			dto.setSubject(template.getSubject());
			dto.setContent(emailMessage);
		} catch (NoResultException nre) {
			logger.error("Could not find email template for "
					+ rf.getLanguage());
			throw new RateFileException(
					"Could not find email template for language "
							+ rf.getLanguage());
		}

	}

	protected RateFileSearchFilter createRateFileSearchFilterForQuery(
			QuotationQuery query) {
		RateFileSearchFilter filter = new RateFileSearchFilter();
		filter.setCountry(query.getCountry());
		if (query.getCustomer() instanceof FullCustomer) {
			filter.setCustomer(query.getCustomer());
		}
		filter.setMeasurement(query.getMeasurement());
		filter.setRateKind(query.getKindOfRate());
		return filter;
	}

	public PriceCalculationDTOToResultMapper getMapper() {
		return mapper;
	}

	public void setMapper(PriceCalculationDTOToResultMapper mapper) {
		this.mapper = mapper;
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
}
