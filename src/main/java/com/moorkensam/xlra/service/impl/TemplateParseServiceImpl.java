package com.moorkensam.xlra.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
import com.moorkensam.xlra.service.util.QuotationUtil;
import com.moorkensam.xlra.service.util.TranslationConfigurationLoader;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * This class parses a freemarker template with the variables and template
 * provided.
 * 
 * @author bas
 *
 */
public class TemplateParseServiceImpl implements TemplateParseService {

	private static final String TEMPLATES_ACCOUNT_CREATED_FTL = "templates/account_created.ftl";

	private static final String TEMPLATES_USER_RESET_FTL = "templates/password_reset.ftl";

	private final static Logger logger = LogManager.getLogger();

	private StringTemplateLoader stringTemplateLoader;

	private Configuration freemarkerConfiguration;

	private ConfigurationLoader configLoader;

	private QuotationUtil quotationUtil;

	private TranslationConfigurationLoader translationLoader;

	private void inializeEngine() {
		setStringTemplateLoader(new StringTemplateLoader());
		setConfiguration(new Configuration());
		setConfigLoader(ConfigurationLoader.getInstance());
		translationLoader = TranslationConfigurationLoader.getInstance();
		quotationUtil = QuotationUtil.getInstance();
	}

	private static TemplateParseServiceImpl instance;

	private TemplateParseServiceImpl() {
		super();
		inializeEngine();
	}

	public static TemplateParseServiceImpl getInstance() {
		if (instance == null) {
			instance = new TemplateParseServiceImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.moorkensam.xlra.service.impl.TemplateParseService#
	 * parseOfferteEmailTemplate(java.lang.String,
	 * com.moorkensam.xlra.model.offerte.QuotationResult, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String parseOfferteEmailTemplate(String templateFromDb,
			QuotationResult offerte, String fullDetail,
			String additionalConditions) throws TemplatingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing email template " + templateFromDb);
		}
		stringTemplateLoader = new StringTemplateLoader();
		Map<String, Object> dataModel = createOfferteEmailTemplateParams(
				offerte, fullDetail, additionalConditions);

		StringWriter writer = new StringWriter();
		getStringTemplateLoader().putTemplate("template", templateFromDb);
		getConfiguration().setTemplateLoader(getStringTemplateLoader());
		Template template;
		try {
			template = getConfiguration().getTemplate("template");
			template.process(dataModel, writer);
		} catch (TemplateNotFoundException e) {
			logger.error(e);
			throw new TemplatingException("Template not found internaly", e);
		} catch (MalformedTemplateNameException e) {
			logger.error(e);
			throw new TemplatingException("Template malformated", e);
		} catch (ParseException e) {
			logger.error(e);
			throw new TemplatingException("Failed to parse template", e);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}
		return writer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.moorkensam.xlra.service.impl.TemplateParseService#parseOffertePdf
	 * (com.moorkensam.xlra.model.offerte.QuotationResult,
	 * com.moorkensam.xlra.model.configuration.Language)
	 */
	@Override
	public String parseOffertePdf(QuotationResult offerte, Language language)
			throws TemplatingException {
		String templateName = "templates/offertepdf_"
				+ language.toString().toLowerCase() + ".ftl";
		StringWriter writer = new StringWriter();
		Map<String, Object> parameters = createOffertePdfParameterMap(offerte,
				language);
		try {
			Template template = loadTemplate(templateName);
			template.process(parameters, writer);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}
		return writer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.moorkensam.xlra.service.impl.TemplateParseService#
	 * parseUserCreatedTemplate(com.moorkensam.xlra.model.security.User)
	 */
	@Override
	public String parseUserCreatedTemplate(User user)
			throws TemplatingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing user created email for " + user.getEmail());
		}
		Map<String, Object> dataModel = createUserTemplateParams(user);
		StringWriter writer = new StringWriter();
		try {
			Template template = loadTemplate(TEMPLATES_ACCOUNT_CREATED_FTL);
			template.process(dataModel, writer);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}

		return writer.toString();
	}

	/**
	 * Creates the template parameter map for the email.
	 * 
	 * @param query
	 * @param result
	 * @return
	 */
	private Map<String, Object> createOfferteEmailTemplateParams(
			QuotationResult offerte, String fullDetail,
			String additionalConditions) {
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("customer", offerte.getQuery().getCustomer()
				.getName());
		templateModel.put("quantity", offerte.getQuery().getQuantity());
		templateModel.put("measurement", offerte.getQuery().getMeasurement());
		getCountryNameForEmail(offerte.getQuery());
		templateModel.put("destination", offerte.getQuery().getPostalCode()
				+ " " + getCountryNameForEmail(offerte.getQuery()));
		templateModel.put("offerteKey", offerte.getOfferteUniqueIdentifier());
		templateModel.put("createdUserFullName",
				offerte.getCreatedUserFullName());

		templateModel.put("detailCalculation", fullDetail);
		templateModel.put("additionalConditions", additionalConditions);
		return templateModel;
	}

	private String getCountryNameForEmail(QuotationQuery query) {
		String countryName = query.getCountry().getNameForLanguage(
				query.getResultLanguage());
		if (countryName == null) {
			logger.warn("Country name for Country "
					+ query.getCountry().getShortName() + " and language "
					+ query.getResultLanguage()
					+ " not found, falling back on shortname.");
			countryName = query.getCountry().getShortName();
		}

		return countryName;
	}

	private Map<String, Object> createAdditionConditionsParameterMap(
			List<OfferteOptionDTO> options, Language language) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("calculationfulldetailadditionalconditions",
				translationLoader.getProperty(
						"calculation.fulldetail.additional.conditions",
						language));
		for (OfferteOptionDTO option : options) {
			if (option.isSelected()) {
				parameterMap.put(option.getI8nKey().replace(".", ""),
						translationLoader.getProperty(option.getI8nKey(),
								language));
			}
		}
		return parameterMap;
	}

	private Map<String, Object> createFullDetailParameterMap(Language language) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		fillInCalculationTranslationKeys(language, parameterMap);
		return parameterMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.moorkensam.xlra.service.impl.TemplateParseService#
	 * parseHtmlAdditionalConditions(java.util.List,
	 * com.moorkensam.xlra.model.configuration.Language)
	 */
	@Override
	public String parseHtmlAdditionalConditions(List<OfferteOptionDTO> options,
			Language language) throws TemplatingException {
		stringTemplateLoader = new StringTemplateLoader();
		String unparsedTemplate = buildAdditionalConditionsTemplate(options);
		Map<String, Object> parameterMap = createAdditionConditionsParameterMap(
				options, language);
		StringWriter writer = new StringWriter();
		getStringTemplateLoader().putTemplate("template", unparsedTemplate);
		getConfiguration().setTemplateLoader(getStringTemplateLoader());
		Template template;
		try {
			template = getConfiguration().getTemplate("template");
			template.process(parameterMap, writer);
		} catch (TemplateNotFoundException e) {
			logger.error(e);
			throw new TemplatingException("Template not found internaly", e);
		} catch (MalformedTemplateNameException e) {
			logger.error(e);
			throw new TemplatingException("Template malformated", e);
		} catch (ParseException e) {
			logger.error(e);
			throw new TemplatingException("Failed to parse template", e);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}
		return writer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.moorkensam.xlra.service.impl.TemplateParseService#
	 * parseHtmlFullDetailCalculation(java.util.List,
	 * com.moorkensam.xlra.model.offerte.PriceCalculation,
	 * com.moorkensam.xlra.model.configuration.Language)
	 */
	@Override
	public String parseHtmlFullDetailCalculation(
			final List<OfferteOptionDTO> options,
			final PriceCalculation priceCalculation, Language language)
			throws TemplatingException {
		stringTemplateLoader = new StringTemplateLoader();
		String unParsedTemplate = buildFullDetailTemplate(options,
				priceCalculation);
		Map<String, Object> parameterMap = createFullDetailParameterMap(language);
		StringWriter writer = new StringWriter();
		getStringTemplateLoader().putTemplate("template", unParsedTemplate);
		getConfiguration().setTemplateLoader(getStringTemplateLoader());
		Template template;
		try {
			template = getConfiguration().getTemplate("template");
			template.process(parameterMap, writer);
		} catch (TemplateNotFoundException e) {
			logger.error(e);
			throw new TemplatingException("Template not found internaly", e);
		} catch (MalformedTemplateNameException e) {
			logger.error(e);
			throw new TemplatingException("Template malformated", e);
		} catch (ParseException e) {
			logger.error(e);
			throw new TemplatingException("Failed to parse template", e);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}
		return writer.toString();
	}

	@Override
	public String parseUserResetPasswordEmail(User user)
			throws TemplatingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing password reset template for "
					+ user.getEmail());
		}
		Map<String, Object> dataModel = createPasswordResetTemplateParams(user);
		StringWriter writer = new StringWriter();
		try {
			Template template = loadTemplate(TEMPLATES_USER_RESET_FTL);
			template.process(dataModel, writer);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}

		return writer.toString();
	}

	private Template loadTemplate(String templateName)
			throws TemplatingException {
		try {
			freemarkerConfiguration.setClassForTemplateLoading(this.getClass(),
					"/");
			return freemarkerConfiguration.getTemplate(templateName);
		} catch (TemplateNotFoundException e) {
			logger.error(e);
			throw new TemplatingException("Template not found internaly", e);
		} catch (MalformedTemplateNameException e) {
			logger.error(e);
			throw new TemplatingException("Template malformated", e);
		} catch (ParseException e) {
			logger.error(e);
			throw new TemplatingException("Failed to parse template", e);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		}
	}

	public StringTemplateLoader getStringTemplateLoader() {
		return stringTemplateLoader;
	}

	public void setStringTemplateLoader(
			StringTemplateLoader stringTemplateLoader) {
		this.stringTemplateLoader = stringTemplateLoader;
	}

	public Configuration getConfiguration() {
		return freemarkerConfiguration;
	}

	public void setConfiguration(Configuration configuration) {
		this.freemarkerConfiguration = configuration;
	}

	public ConfigurationLoader getConfigLoader() {
		return configLoader;
	}

	public void setConfigLoader(ConfigurationLoader configLoader) {
		this.configLoader = configLoader;
	}

	public TranslationConfigurationLoader getTranslationLoader() {
		return translationLoader;
	}

	public void setTranslationLoader(
			TranslationConfigurationLoader translationLoader) {
		this.translationLoader = translationLoader;
	}

	private Map<String, Object> createUserTemplateParams(User user) {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("fullName", user.getFirstName() + " " + user.getName());
		dataModel.put(
				"applicationUrl",
				getConfigLoader().getProperty(
						ConfigurationLoader.APPLICATION_PASSWORD_SETUP_URL)
						+ "?token="
						+ user.getTokenInfo().getVerificationToken()
						+ "&email=" + user.getEmail());
		return dataModel;
	}

	private Map<String, Object> createPasswordResetTemplateParams(User user) {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("fullName", user.getFirstName() + " " + user.getName());
		dataModel.put(
				"applicationUrl",
				getConfigLoader().getProperty(
						ConfigurationLoader.APPLICAITON_PASSWORD_RESET_URL)
						+ "?token="
						+ user.getTokenInfo().getVerificationToken()
						+ "&email=" + user.getEmail());
		return dataModel;
	}

	private Map<String, Object> createOffertePdfParameterMap(
			QuotationResult offerte, Language language)
			throws TemplatingException {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		fillInCalculationTranslationKeys(language, parameterMap);

		fillInTemplateValues(offerte, language, parameterMap);

		fillInCalculationValues(offerte, parameterMap);

		fillInAdditionalParameters(offerte, parameterMap, language);
		return parameterMap;
	}

	private void fillInAdditionalParameters(QuotationResult offerte,
			Map<String, Object> parameterMap, Language language) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();

		if (hasOptionSelected(offerte.getSelectableOptions())) {
			for (OfferteOptionDTO option : offerte.getSelectableOptions()) {
				if (option.isSelected() && !option.isCalculationOption()) {
					conditionsMap.put(translationLoader.getProperty(option
							.getI8nKey().replace(".", ""), language), option
							.getValue());
				}
			}
		}
		if (conditionsMap.keySet().size() > 0) {
			parameterMap.put("conditions", conditionsMap);
		}
	}

	private void fillInCalculationValues(QuotationResult offerte,
			Map<String, Object> parameterMap) {
		if (offerte.getCalculation().getBasePrice() != null) {
			parameterMap.put("baseprice", offerte.getCalculation()
					.getBasePrice());
		}
		if (offerte.getCalculation().getDieselPrice() != null) {
			parameterMap.put("dieselprice", offerte.getCalculation()
					.getDieselPrice());
		}
		if (offerte.getCalculation().getChfPrice() != null) {
			parameterMap
					.put("chfprice", offerte.getCalculation().getChfPrice());
		}
		if (offerte.getCalculation().getImportFormalities() != null) {
			parameterMap.put("importforms", offerte.getCalculation()
					.getImportFormalities());
		}
		if (offerte.getCalculation().getExportFormalities() != null) {
			parameterMap.put("exportforms", offerte.getCalculation()
					.getExportFormalities());
		}
		if (offerte.getCalculation().getResultingPriceSurcharge() != null) {
			parameterMap.put("adr", offerte.getCalculation()
					.getResultingPriceSurcharge());
		}
		if (offerte.getCalculation().getFinalPrice() != null) {
			parameterMap.put("finalprice", offerte.getCalculation()
					.getFinalPrice());
		}
	}

	private void fillInTemplateValues(QuotationResult offerte,
			Language language, Map<String, Object> parameterMap)
			throws TemplatingException {
		parameterMap.put("customerName", offerte.getQuery().getCustomer()
				.getName());
		fillInCustomerParams(offerte, parameterMap);
		parameterMap.put("offerteKeyValue",
				offerte.getOfferteUniqueIdentifier());
		parameterMap.put("offerteDate", offerte.getQuery().getQuotationDate());
		parameterMap.put("country", offerte.getQuery().getCountry()
				.getNameForLanguage(language));
		parameterMap.put("postalCode", offerte.getQuery().getPostalCode());
		parameterMap.put("quantityAndMeasurement", offerte.getQuery()
				.getQuantity()
				+ " "
				+ offerte.getQuery().getMeasurement().getDescription());
		parameterMap.put("transportType", offerte.getQuery().getKindOfRate()
				.getDescription());
		String additionalConditions = parseHtmlAdditionalConditions(
				offerte.getSelectableOptions(), language);
		if (StringUtils.isBlank(additionalConditions)) {
			additionalConditions = " ";
		}
		parameterMap.put("additionalConditions", additionalConditions);
		parameterMap.put("createdUserFullName",
				offerte.getCreatedUserFullName());
	}

	private void fillInCalculationTranslationKeys(Language language,
			Map<String, Object> parameterMap) {
		parameterMap.put("calculationfulldetailtitle", translationLoader
				.getProperty("calculation.fulldetail.title", language));
		parameterMap.put("calculationfulldetailbasicprice", translationLoader
				.getProperty("calculation.fulldetail.basic.price", language));
		parameterMap.put("calculationfulldetaildieselsurcharge",
				translationLoader.getProperty(
						"calculation.fulldetail.diesel.surcharge", language));
		parameterMap.put("calculationfulldetailswissfrancsurcharge",
				translationLoader.getProperty(
						"calculation.fulldetail.swiss.franc.surcharge",
						language));
		parameterMap.put("calculationfulldetailimportformalities",
				translationLoader.getProperty(
						"calculation.fulldetail.import.formalities", language));
		parameterMap.put("calculationfulldetailexportformalities",
				translationLoader.getProperty(
						"calculation.fulldetail.export.formalities", language));
		parameterMap.put("calculationfulldetailadrsurcharge", translationLoader
				.getProperty("calculation.fulldetail.adr.surcharge", language));
		parameterMap.put("calculationfulldetailtotalprice", translationLoader
				.getProperty("calculation.fulldetail.total.price", language));
	}

	private void fillInCustomerParams(QuotationResult offerte,
			Map<String, Object> parameterMap) {
		if (offerte.getQuery().getCustomer().getAddress() != null) {
			if (StringUtils.isNotBlank(offerte.getQuery().getCustomer()
					.getAddress().getStreet())
					|| StringUtils.isNotBlank(offerte.getQuery().getCustomer()
							.getAddress().getNumber())) {
				parameterMap.put("customerStreetAndNumber", offerte.getQuery()
						.getCustomer().getAddress().getStreet()
						+ " "
						+ offerte.getQuery().getCustomer().getAddress()
								.getNumber());
			}
			if (StringUtils.isNotBlank(offerte.getQuery().getCustomer()
					.getAddress().getZip())
					|| StringUtils.isNotBlank(offerte.getQuery().getCustomer()
							.getAddress().getCity())) {
				parameterMap.put("customerAdres", offerte.getQuery()
						.getCustomer().getAddress().getZip()
						+ " "
						+ offerte.getQuery().getCustomer().getAddress()
								.getCity());
			}
		}
	}

	private String buildAdditionalConditionsTemplate(
			final List<OfferteOptionDTO> options) {
		if (options == null || options.isEmpty()) {
			return "";
		}
		if (hasOptionSelected(options)) {
			StringBuilder builder = new StringBuilder();
			builder.append("<h3>${calculationfulldetailadditionalconditions}</h3>");
			builder.append("<table style=\"width:100%;\">");
			for (OfferteOptionDTO option : options) {
				if (option.isSelected() && !option.isCalculationOption()
						&& option.isShowToCustomer()) {
					builder.append("<tr><td style=\"width:30%;\">${"
							+ option.getI8nKey().replace(".", "") + "}</td>");
					builder.append("<td>" + option.getValue() + "</td></tr>");
				}
			}
			builder.append("</table>");
			return builder.toString();
		} else {
			return "";
		}
	}

	private boolean hasOptionSelected(List<OfferteOptionDTO> options) {
		for (OfferteOptionDTO o : options) {
			if (o.isSelected())
				return true;
		}
		return false;
	}

	private String buildFullDetailTemplate(
			final List<OfferteOptionDTO> options,
			final PriceCalculation priceCalculation) {
		StringBuilder detailCalculationBuilder = new StringBuilder();
		detailCalculationBuilder.append("<table style=\"width:100%;\">");
		detailCalculationBuilder
				.append("<tr><td style=\"width:20%;\">${calculationfulldetailbasicprice}</td><td> "
						+ priceCalculation.getBasePrice() + " EUR</td></tr>");
		if (options != null && !options.isEmpty()) {
			for (OfferteOptionDTO option : options) {
				if (option.isSelected() && option.isCalculationOption()
						&& option.isShowToCustomer()) {
					detailCalculationBuilder.append("<tr><td>${"
							+ option.getI8nKey().replace(".", "") + "}</td>");

					detailCalculationBuilder.append("<td>");
					switch (option.getKey()) {
					case DIESEL_SURCHARGE:
						detailCalculationBuilder.append(priceCalculation
								.getDieselPrice());
						break;
					case CHF_SURCHARGE:
						detailCalculationBuilder.append(priceCalculation
								.getChfPrice());
						break;
					case IMPORT_FORM:
						detailCalculationBuilder.append(priceCalculation
								.getImportFormalities());
						break;
					case EXPORT_FORM:
						detailCalculationBuilder.append(priceCalculation
								.getExportFormalities());
						break;
					case ADR_SURCHARGE:
						detailCalculationBuilder.append(priceCalculation
								.getResultingPriceSurcharge());
						break;
					default:
						break;
					}
					detailCalculationBuilder.append(" EUR</td></tr>");
				}
			}
		}
		detailCalculationBuilder.append("</table>");
		detailCalculationBuilder
				.append("<h3>${calculationfulldetailtotalprice} EUR."
						+ priceCalculation.getFinalPrice() + "</h3><br />");

		return detailCalculationBuilder.toString();
	}

	public QuotationUtil getQuotationUtil() {
		return quotationUtil;
	}

	public void setQuotationUtil(QuotationUtil quotationUtil) {
		this.quotationUtil = quotationUtil;
	}
}
