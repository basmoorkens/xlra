package com.moorkensam.xlra.service.impl;

import java.util.HashMap;
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
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.CalculationService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.RateFileService;

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
		templatEngine = TemplateEngine.getInstance();
		mapper = new PriceCalculationDTOToResultMapper();
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
			logger.error("Could not find value for parameters: "
					+ query.toString());
			throw new RateFileException(
					"Could not find price for given input parameters. Quantity: "
							+ query.getQuantity() + " Postal code: "
							+ query.getPostalCode());
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
	private void initializeOfferteEmail(QuotationQuery query,
			OfferteMailDTO dto, RateFile rf, PriceCalculationDTO priceDTO)
			throws TemplatingException {
		PriceResultDTO resultDTO = new PriceResultDTO();
		mapper.map(priceDTO, resultDTO);

		MailTemplate template = emailTemplateDAO.getMailTemplateForLanguage(rf
				.getLanguage());
		Map<String, Object> templateParameters = createTemplateParams(query,
				resultDTO);
		String emailMessage = templatEngine.parseEmailTemplate(
				template.getTemplate(), templateParameters);
		dto.setAddress(query.getCustomer().getEmail());
		dto.setSubject(template.getSubject());
		dto.setContent(emailMessage);
	}

	/**
	 * Creates the template parameter map for the email.
	 * 
	 * @param query
	 * @param result
	 * @return
	 */
	private Map<String, Object> createTemplateParams(QuotationQuery query,
			PriceResultDTO priceDTO) {
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("customer", query.getCustomer().getName());
		templateModel.put("quantity", query.getQuantity());
		templateModel.put("measurement", query.getMeasurement());
		templateModel.put("detailCalculation",
				priceDTO.getDetailedCalculation());
		templateModel.put("destination",
				query.getCountry().getName() + query.getPostalCode());
		return templateModel;
	}

	private RateFileSearchFilter createRateFileSearchFilterForQuery(
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
}
