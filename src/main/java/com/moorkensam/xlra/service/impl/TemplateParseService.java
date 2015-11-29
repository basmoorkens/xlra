package com.moorkensam.xlra.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
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
public class TemplateParseService {

	private final static Logger logger = LogManager.getLogger();

	private StringTemplateLoader stringTemplateLoader;

	private Configuration freemarkerConfiguration;

	private ConfigurationLoader configLoader;

	private TranslationConfigurationLoader translationLoader;

	private void inializeEngine() {
		setStringTemplateLoader(new StringTemplateLoader());
		setConfiguration(new Configuration());
		setConfigLoader(ConfigurationLoader.getInstance());
		translationLoader = TranslationConfigurationLoader.getInstance();
	}

	private static TemplateParseService instance;

	private TemplateParseService() {
		super();
		inializeEngine();
	}

	public static TemplateParseService getInstance() {
		if (instance == null) {
			instance = new TemplateParseService();
		}
		return instance;
	}

	/**
	 * Parses a freemarker template with a given map of values.
	 * 
	 * @param templateFromDb
	 *            The freemarker template to parse.
	 * @param dataModel
	 *            The values to put in the template.
	 * @return The parsed template as a string.
	 * @throws TemplatingException
	 *             When the template could not be parsed
	 */
	public String parseOfferteEmailTemplate(String templateFromDb,
			QuotationResult offerte, String fullDetail)
			throws TemplatingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing email template " + templateFromDb);
		}
		stringTemplateLoader = new StringTemplateLoader();
		Map<String, Object> dataModel = createOfferteEmailTemplateParams(
				offerte, fullDetail);
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

	public String parseOffertePdf(QuotationResult offerte, Language language)
			throws TemplatingException {
		String templateName = "templates/offertepdf.ftl";
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

	private Map<String, Object> createOffertePdfParameterMap(
			QuotationResult offerte, Language language)
			throws TemplatingException {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
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
		String fullDetailAsHtml = parseHtmlFullDetailCalculation(
				offerte.getCalculation(), language);
		parameterMap.put("detailCalculation", fullDetailAsHtml);
		fillInOfferteEmailTranslations(parameterMap, language);
		return parameterMap;
	}

	protected void fillInOfferteEmailTranslations(
			Map<String, Object> parameterMap, Language language) {
		parameterMap.put("pdftitle",
				translationLoader.getProperty("pdf.title", language));
		parameterMap.put("pdfrequestdate",
				translationLoader.getProperty("pdf.request.date", language));
		parameterMap.put("pdfrequestcountry",
				translationLoader.getProperty("pdf.request.country", language));
		parameterMap.put("pdfrequestpostalcode", translationLoader.getProperty(
				"pdf.request.postalcode", language));
		parameterMap
				.put("pdfrequestquantity", translationLoader.getProperty(
						"pdf.request.quantity", language));
		parameterMap.put("pdfrequesttransporttype", translationLoader
				.getProperty("pdf.request.transporttype", language));
		parameterMap.put("pdfrequesttitle",
				translationLoader.getProperty("pdf.request.title", language));
		parameterMap.put("pdfoffertetitle",
				translationLoader.getProperty("pdf.offerte.title", language));
	}

	public String parseUserCreatedTemplate(User user)
			throws TemplatingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing user created email for " + user.getEmail());
		}
		Map<String, Object> dataModel = createUserTemplateParams(user);
		StringWriter writer = new StringWriter();
		try {
			Template template = loadTemplate("templates/account_created.ftl");
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

	/**
	 * Creates the template parameter map for the email.
	 * 
	 * @param query
	 * @param result
	 * @return
	 */
	private Map<String, Object> createOfferteEmailTemplateParams(
			QuotationResult offerte, String fullDetail) {
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("customer", offerte.getQuery().getCustomer()
				.getName());
		templateModel.put("quantity", offerte.getQuery().getQuantity());
		templateModel.put("measurement", offerte.getQuery().getMeasurement());
		templateModel.put("detailCalculation", fullDetail);
		getCountryNameForEmail(offerte.getQuery());
		templateModel.put("destination", offerte.getQuery().getPostalCode()
				+ " " + getCountryNameForEmail(offerte.getQuery()));
		templateModel.put("offerteKey", offerte.getOfferteUniqueIdentifier());
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

	private Map<String, Object> createFullDetailParameterMap(Language language) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
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
		return parameterMap;
	}

	public String parseHtmlFullDetailCalculation(
			final PriceCalculation priceCalculation, Language language)
			throws TemplatingException {
		stringTemplateLoader = new StringTemplateLoader();
		String unParsedTemplate = buildFullDetailTemplate(priceCalculation);
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

	private String buildFullDetailTemplate(
			final PriceCalculation priceCalculation) {
		StringBuilder detailCalculationBuilder = new StringBuilder();
		detailCalculationBuilder.append("<table>");
		detailCalculationBuilder
				.append("<tr><td>${calculationfulldetailbasicprice}</td><td> "
						+ priceCalculation.getBasePrice() + "</td></tr>");
		if (priceCalculation.getAppliedOperations().contains(
				TranslationKey.DIESEL_SURCHARGE)) {
			detailCalculationBuilder
					.append("<tr><td>${calculationfulldetaildieselsurcharge}</td><td>"
							+ priceCalculation.getDieselPrice() + "</td></tr>");
		}
		if (priceCalculation.getAppliedOperations().contains(
				TranslationKey.CHF_SURCHARGE)) {
			detailCalculationBuilder
					.append("<tr><td>${calculationfulldetailswissfrancsurcharge}</td><td>"
							+ priceCalculation.getChfPrice() + "</td></tr>");
		}
		if (priceCalculation.getAppliedOperations().contains(
				TranslationKey.IMPORT_FORM)) {
			detailCalculationBuilder
					.append("<tr><td>${calculationfulldetailimportformalities}</td><td>"
							+ priceCalculation.getImportFormalities()
							+ "</td></tr>");
		}
		if (priceCalculation.getAppliedOperations().contains(
				TranslationKey.EXPORT_FORM)) {
			detailCalculationBuilder
					.append("<tr><td>${calculationfulldetailexportformalities}</td><td>"
							+ priceCalculation.getExportFormalities()
							+ "</td></tr>");
		}
		if (priceCalculation.getAppliedOperations().contains(
				TranslationKey.ADR_SURCHARGE)) {
			detailCalculationBuilder
					.append("<tr><td>${calculationfulldetailadrsurcharge}</td><td>"
							+ priceCalculation.getResultingPriceSurcharge()
							+ "</td></tr>");
		}
		detailCalculationBuilder.append("</table>");
		detailCalculationBuilder
				.append("<h3>${calculationfulldetailtotalprice}"
						+ priceCalculation.getFinalPrice() + "</h3><br />");

		return detailCalculationBuilder.toString();
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

}
