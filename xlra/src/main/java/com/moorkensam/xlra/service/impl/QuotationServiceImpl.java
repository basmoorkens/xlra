package com.moorkensam.xlra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
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
	private RateFileService rateFileService;

	@Inject
	private QuotationQueryDAO quotationDAO;

	@Inject
	private QuotationResultDAO quotationResultDAO;

	@Inject
	private EmailService emailService;

	@PostConstruct
	public void init() {
		templatEngine = TemplateEngine.getInstance();
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
		RateFile rf = rateFileService.getFullRateFileForFilter(filter);
		RateLine result;
		try {
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			calculatePriceAccordingToConditions();
			initializeOfferteEmail(query, dto, rf, result);
			emailService.sendOfferteMail(dto);
		} catch (RateFileException e1) {
			logger.error("Could find value for parameters: " + query.toString());
			throw new RateFileException(
					"Could not find price for given input parameters.");
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

	private void initializeOfferteEmail(QuotationQuery query,
			OfferteMailDTO dto, RateFile rf, RateLine result)
			throws TemplatingException {
		MailTemplate template = emailTemplateDAO.getMailTemplateForLanguage(rf
				.getLanguage());
		Map<String, Object> templateParameters = createTemplateParams(query,
				result);
		String emailMessage = templatEngine.parseEmailTemplate(
				template.getTemplate(), templateParameters);
		dto.setAddress(query.getCustomer().getEmail());
		dto.setSubject(template.getSubject());
		dto.setContent(emailMessage);
	}

	private Map<String, Object> createTemplateParams(QuotationQuery query,
			RateLine result) {
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("customer", query.getCustomer().getName());
		templateModel.put("quantity", query.getQuantity());
		templateModel.put("measurement", query.getMeasurement());
		templateModel.put("destination",
				query.getCountry().getName() + query.getPostalCode());
		templateModel.put("price", result.getValue());
		return templateModel;
	}

	private void calculatePriceAccordingToConditions() {
		// TODO IMPLEMENT CONDITIONS
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
