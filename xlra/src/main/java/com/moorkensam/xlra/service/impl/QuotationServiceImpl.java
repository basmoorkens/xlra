package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.dto.PriceCalculationDTO;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.QuotationQuery;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.DieselService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.CalcUtil;

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
	private ConfigurationDao configurationDao;

	@Inject
	private EmailService emailService;

	@Inject
	private DieselService dieselService;

	@Inject
	private CurrencyService currencyService;

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
		PriceCalculationDTO priceDTO = new PriceCalculationDTO();
		try {
			result = rf.getRateLineForQuantityAndPostalCode(
					query.getQuantity(), query.getPostalCode());
			priceDTO.setBasePrice(result.getValue());
			calculatePriceAccordingToConditions(priceDTO, rf.getCountry(),
					rf.getConditions(), query);
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

	/**
	 * Creates the template parameter map for the email.
	 * 
	 * @param query
	 * @param result
	 * @return
	 */
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

	/*
	 * Calculates the prices according to some business rules.
	 */
	private void calculatePriceAccordingToConditions(
			PriceCalculationDTO priceDTO, Country country,
			List<Condition> conditions, QuotationQuery query)
			throws RateFileException {
		Configuration config = configurationDao.getXlraConfiguration();
		calculateDieselSurchargePrice(priceDTO, config);
		if (country.getShortName().equalsIgnoreCase("chf")) {
			calculateChfSurchargePrice(priceDTO, config);
		}
		for (Condition condition : conditions) {
			switch (condition.getConditionKey()) {
			case ADR_SURCHARGE:
				if (query.isAdrSurcharge()) {
					calculateAddressSurcharge(priceDTO, condition);
				}
				break;
			case ADR_MINIMUM:
				if (query.isAdrSurcharge()) {
					calculateAddressSurchargeMinimum(priceDTO, condition);
				}
				break;
			case IMPORT_FORM:
				if (query.isImportFormality()) {
					calculateImportFormality(priceDTO, condition);
				}
				break;
			case EXPORT_FORM:
				if (query.isExportFormality()) {
					calculateExportFormality(priceDTO, condition);
				}
			default:
				break;
			}
		}
		applyAfterConditionLogic(priceDTO);
	}

	protected void calculateExportFormality(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal exportFormalities = new BigDecimal(
				Double.parseDouble(condition.getValue()));
		exportFormalities = exportFormalities.setScale(2, RoundingMode.HALF_UP);
		priceDTO.setExportFormalities(exportFormalities);
	}

	protected void calculateImportFormality(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal importFormalities = new BigDecimal(
				Double.parseDouble(condition.getValue()));
		importFormalities = importFormalities.setScale(2, RoundingMode.HALF_UP);
		priceDTO.setImportFormalities(importFormalities);
	}

	protected void calculateAddressSurchargeMinimum(
			PriceCalculationDTO priceDTO, Condition condition) {
		priceDTO.setAdrSurchargeMinimum(new BigDecimal(Double
				.parseDouble(condition.getValue())));
	}

	/**
	 * Apply some business rules to the calculation that can only be executed
	 * after all conditions are parsed.
	 * 
	 * @param priceDTO
	 */
	protected void applyAfterConditionLogic(PriceCalculationDTO priceDTO) {
		if (priceDTO.getAdrSurchargeMinimum().doubleValue() > priceDTO
				.getCalculatedAdrSurcharge().doubleValue()) {
			priceDTO.setResultingPriceSurcharge(priceDTO
					.getAdrSurchargeMinimum());
		} else {
			priceDTO.setResultingPriceSurcharge(priceDTO
					.getCalculatedAdrSurcharge());
		}
	}

	/**
	 * Calculates the address surcharge.
	 * 
	 * @param condition
	 */
	protected void calculateAddressSurcharge(PriceCalculationDTO priceDTO,
			Condition condition) {
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(Double.parseDouble(condition
						.getValue()));
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = result.setScale(2, RoundingMode.HALF_UP);
		priceDTO.setCalculatedAdrSurcharge(result);
	}

	protected void calculateChfSurchargePrice(PriceCalculationDTO priceDTO,
			Configuration config) throws RateFileException {
		CurrencyRate chfRate = getCurrencyService().getChfRateForCurrentPrice(
				config.getCurrentChfValue());
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(chfRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = result.setScale(2, RoundingMode.HALF_UP);
		priceDTO.setChfPrice(result);
	}

	/**
	 * Calculates the diesel supplement for the found base price.
	 * 
	 * @param priceDTO
	 *            The pricedto object to take the base price from and to save
	 *            the diesel surcharge into.
	 * @param config
	 *            The config object to take the current diesel price from.
	 * @throws RateFileException
	 *             Thrown when no dieselpercentage multiplier can be found for
	 *             the current diesel price.
	 */
	protected void calculateDieselSurchargePrice(PriceCalculationDTO priceDTO,
			Configuration config) throws RateFileException {
		DieselRate dieselRate = getDieselService()
				.getDieselRateForCurrentPrice(config.getCurrentDieselPrice());
		BigDecimal multiplier = CalcUtil
				.convertPercentageToBaseMultiplier(dieselRate
						.getSurchargePercentage());
		BigDecimal result = new BigDecimal(priceDTO.getBasePrice()
				.doubleValue() * multiplier.doubleValue());
		result = result.setScale(2, RoundingMode.HALF_UP);
		priceDTO.setDieselPrice(result);
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

	public DieselService getDieselService() {
		return dieselService;
	}

	public void setDieselService(DieselService dieselService) {
		this.dieselService = dieselService;
	}

	public CurrencyService getCurrencyService() {
		return currencyService;
	}

	public void setCurrencyService(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

}
