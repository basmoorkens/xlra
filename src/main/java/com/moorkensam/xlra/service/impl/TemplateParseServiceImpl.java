package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class parses a freemarker template with the variables and template provided.
 * 
 * @author bas
 *
 */
public class TemplateParseServiceImpl implements TemplateParseService {

  private static final String TEMPLATES_ACCOUNT_CREATED_FTL = "templates/account_created.ftl";

  private static final String TEMPLATES_USER_RESET_FTL = "templates/password_reset.ftl";

  private static final Logger logger = LogManager.getLogger();

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

  /**
   * Gets an instance of the class.
   * 
   * @return The class instance
   */
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
   * parseOfferteEmailTemplate(java.lang.String, com.moorkensam.xlra.model.offerte.QuotationResult,
   * java.lang.String, java.lang.String)
   */
  @Override
  public String parseOfferteEmailTemplate(String templateFromDb, QuotationResult offerte)
      throws TemplatingException {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing email template " + templateFromDb);
    }
    stringTemplateLoader = new StringTemplateLoader();
    Map<String, Object> dataModel = createOfferteEmailTemplateParams(offerte);

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
   * @see com.moorkensam.xlra.service.impl.TemplateParseService#parseOffertePdf
   * (com.moorkensam.xlra.model.offerte.QuotationResult,
   * com.moorkensam.xlra.model.configuration.Language)
   */
  @Override
  public String parseOffertePdf(QuotationResult offerte, Language language)
      throws TemplatingException {
    String templateName = "templates/offertepdf_" + language.toString().toLowerCase() + ".ftl";
    StringWriter writer = new StringWriter();
    Map<String, Object> parameters = createOffertePdfParameterMap(offerte, language);
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
  public String parseUserCreatedTemplate(User user) throws TemplatingException {
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
   * @param query The query to create the template for.
   * @param result The resulting map.
   * @return The map with parameters for the email template.
   */
  private Map<String, Object> createOfferteEmailTemplateParams(QuotationResult offerte) {
    Map<String, Object> templateModel = new HashMap<String, Object>();
    templateModel.put("customer", offerte.getQuery().getCustomer().getName());
    templateModel.put("quantity", offerte.getQuery().getQuantity());
    templateModel.put("measurement", offerte.getQuery().getMeasurement());
    getCountryNameForEmail(offerte.getQuery());
    templateModel.put("destination", offerte.getQuery().getPostalCode() + " "
        + getCountryNameForEmail(offerte.getQuery()));
    templateModel.put("offerteKey", offerte.getOfferteUniqueIdentifier());
    templateModel.put("createdUserFullName", offerte.getCreatedUserFullName());

    return templateModel;
  }

  private String getCountryNameForEmail(QuotationQuery query) {
    String countryName = query.getCountry().getNameForLanguage(query.getResultLanguage());
    if (countryName == null) {
      logger
          .warn("Country name for Country " + query.getCountry().getShortName() + " and language "
              + query.getResultLanguage() + " not found, falling back on shortname.");
      countryName = query.getCountry().getShortName();
    }

    return countryName;
  }

  @Override
  public String parseUserResetPasswordEmail(User user) throws TemplatingException {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing password reset template for " + user.getEmail());
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

  private Template loadTemplate(String templateName) throws TemplatingException {
    try {
      freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/");
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

  public void setStringTemplateLoader(StringTemplateLoader stringTemplateLoader) {
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

  public void setTranslationLoader(TranslationConfigurationLoader translationLoader) {
    this.translationLoader = translationLoader;
  }

  private Map<String, Object> createUserTemplateParams(User user) {
    Map<String, Object> dataModel = new HashMap<String, Object>();
    dataModel.put("fullName", user.getFirstName() + " " + user.getName());
    dataModel.put("userName", user.getUserName());
    dataModel.put("applicationUrl",
        getConfigLoader().getProperty(ConfigurationLoader.APPLICATION_PASSWORD_SETUP_URL)
            + "?token=" + user.getTokenInfo().getVerificationToken() + "&email=" + user.getEmail());
    return dataModel;
  }

  private Map<String, Object> createPasswordResetTemplateParams(User user) {
    Map<String, Object> dataModel = new HashMap<String, Object>();
    dataModel.put("fullName", user.getFirstName() + " " + user.getName());
    dataModel.put("applicationUrl",
        getConfigLoader().getProperty(ConfigurationLoader.APPLICAITON_PASSWORD_RESET_URL)
            + "?token=" + user.getTokenInfo().getVerificationToken() + "&email=" + user.getEmail());
    return dataModel;
  }

  private Map<String, Object> createOffertePdfParameterMap(QuotationResult offerte,
      Language language) throws TemplatingException {
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
      for (OfferteOptionDto option : offerte.getSelectableOptions()) {
        if (option.isSelected() && !option.isCalculationOption()) {
          conditionsMap.put(translationLoader.getProperty(option.getI8nKey(), language),
              option.getValue());
        }
      }
    }
    if (conditionsMap.keySet().size() > 0) {
      parameterMap.put("conditions", conditionsMap);
    }
  }

  private void fillInCalculationValues(QuotationResult offerte, Map<String, Object> parameterMap) {
    if (offerte.getCalculation().getBasePrice() != null) {
      parameterMap.put("baseprice", offerte.getCalculation().getBasePrice());
    }
    if (offerte.getCalculation().getDieselPrice() != null) {
      parameterMap.put("dieselprice", offerte.getCalculation().getDieselPrice());
    }
    if (offerte.getCalculation().getChfPrice() != null) {
      parameterMap.put("chfprice", offerte.getCalculation().getChfPrice());
    }
    if (offerte.getCalculation().getImportFormalities() != null) {
      parameterMap.put("importforms", offerte.getCalculation().getImportFormalities());
    }
    if (offerte.getCalculation().getExportFormalities() != null) {
      parameterMap.put("exportforms", offerte.getCalculation().getExportFormalities());
    }
    if (offerte.getCalculation().getResultingPriceSurcharge() != null) {
      parameterMap.put("adr", offerte.getCalculation().getResultingPriceSurcharge());
    }
    if (offerte.getCalculation().getFinalPrice() != null) {
      parameterMap.put("finalprice", offerte.getCalculation().getFinalPrice());
    }
  }

  private void fillInTemplateValues(QuotationResult offerte, Language language,
      Map<String, Object> parameterMap) throws TemplatingException {
    parameterMap.put("customerName", offerte.getQuery().getCustomer().getName());
    fillInCustomerParams(offerte, parameterMap);
    parameterMap.put("offerteKeyValue", offerte.getOfferteUniqueIdentifier());
    parameterMap.put("offerteDate", offerte.getQuery().getQuotationDate());
    parameterMap.put("country", offerte.getQuery().getCountry().getNameForLanguage(language));
    parameterMap.put("postalCode", offerte.getQuery().getPostalCode());
    parameterMap.put("quantityAndMeasurement", offerte.getQuery().getQuantity() + " "
        + offerte.getQuery().getMeasurement().getDescription());
    parameterMap.put("transportType", offerte.getQuery().getKindOfRate().getDescription());
    parameterMap.put("createdUserFullName", offerte.getCreatedUserFullName());
  }

  private void fillInCalculationTranslationKeys(Language language, Map<String, Object> parameterMap) {
    parameterMap.put("calculationfulldetailtitle",
        translationLoader.getProperty("calculation.fulldetail.title", language));
    parameterMap.put("calculationfulldetailbasicprice",
        translationLoader.getProperty("calculation.fulldetail.basic.price", language));
    parameterMap.put("calculationfulldetaildieselsurcharge",
        translationLoader.getProperty("calculation.fulldetail.diesel.surcharge", language));
    parameterMap.put("calculationfulldetailswissfrancsurcharge",
        translationLoader.getProperty("calculation.fulldetail.swiss.franc.surcharge", language));
    parameterMap.put("calculationfulldetailimportformalities",
        translationLoader.getProperty("calculation.fulldetail.import.formalities", language));
    parameterMap.put("calculationfulldetailexportformalities",
        translationLoader.getProperty("calculation.fulldetail.export.formalities", language));
    parameterMap.put("calculationfulldetailadrsurcharge",
        translationLoader.getProperty("calculation.fulldetail.adr.surcharge", language));
    parameterMap.put("calculationfulldetailtotalprice",
        translationLoader.getProperty("calculation.fulldetail.total.price", language));
  }

  private void fillInCustomerParams(QuotationResult offerte, Map<String, Object> parameterMap) {
    if (offerte.getQuery().getCustomer().getAddress() != null) {
      if (StringUtils.isNotBlank(offerte.getQuery().getCustomer().getAddress().getStreet())
          || StringUtils.isNotBlank(offerte.getQuery().getCustomer().getAddress().getNumber())) {
        parameterMap.put("customerStreetAndNumber", offerte.getQuery().getCustomer().getAddress()
            .getStreet()
            + " " + offerte.getQuery().getCustomer().getAddress().getNumber());
      }
      if (StringUtils.isNotBlank(offerte.getQuery().getCustomer().getAddress().getZip())
          || StringUtils.isNotBlank(offerte.getQuery().getCustomer().getAddress().getCity())) {
        parameterMap.put("customerAdres", offerte.getQuery().getCustomer().getAddress().getZip()
            + " " + offerte.getQuery().getCustomer().getAddress().getCity());
      }
    }
  }

  private boolean hasOptionSelected(List<OfferteOptionDto> options) {
    for (OfferteOptionDto o : options) {
      if (o.isSelected()) {
        return true;
      }
    }
    return false;
  }


  public QuotationUtil getQuotationUtil() {
    return quotationUtil;
  }

  public void setQuotationUtil(QuotationUtil quotationUtil) {
    this.quotationUtil = quotationUtil;
  }
}
