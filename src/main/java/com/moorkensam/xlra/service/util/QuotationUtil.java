package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.CalculationValueType;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.translation.TranslationKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuotationUtil {

  private static QuotationUtil instance;

  private TranslationKeyToi8nMapper mapper;

  private QuotationUtil() {

  }

  /**
   * Gets an instance of the class.
   * 
   * @return The class instance.
   */
  public static QuotationUtil getInstance() {
    if (instance == null) {
      QuotationUtil qu = new QuotationUtil();
      qu.setMapper(new TranslationKeyToi8nMapper());
      instance = qu;

    }
    return instance;
  }

  /**
   * Fills in the search params. If the customer is filled in and is of type fullCustomer that is
   * filled in. Otherwise the country / measurement / ratekind is filled in.
   * 
   * @param query The quotation query to create the search filter for.
   * @return The created filter.
   */
  public RateFileSearchFilter createRateFileSearchFilterForQuery(QuotationQuery query,
      boolean ignoreFullCustomerIfPresent) {
    RateFileSearchFilter filter = new RateFileSearchFilter();
    fillInBaseFilterProperties(query, filter);
    if (!ignoreFullCustomerIfPresent) {
      if (query.getCustomer().isHasOwnRateFile()) {
        filter.setCustomer(query.getCustomer());
      }
    }
    return filter;
  }

  private void fillInBaseFilterProperties(QuotationQuery query, RateFileSearchFilter filter) {
    filter.setCountry(query.getCountry());
    filter.setMeasurement(query.getMeasurement());
    filter.setRateKind(query.getKindOfRate());
    filter.setTransportationType(query.getTransportType());
  }

  /**
   * Generate the offertoptions from the ratefile.
   * 
   * @param rf The ratefile used to generate the optiosn.
   * @param language The langauge of the query, used to fill int he translations.
   * @return The generated list of options.
   */
  public List<OfferteOptionDto> generateOfferteOptionsForRateFileAndLanguage(RateFile rf,
      Language language) {
    List<OfferteOptionDto> options = new ArrayList<OfferteOptionDto>();
    for (Condition c : rf.getConditions()) {
      OfferteOptionDto option = createOption(c.getConditionKey(), c.isStandardSelected());
      option.setCalculationOption(isCalculationKey(option.getKey()));
      if (option.isCalculationOption()) {
        option.setValue(c.getValue());
      } else {
        option.setValue(c.getTranslationForLanguage(language).getTranslation());
      }
      options.add(option);
    }
    return options;
  }

  private OfferteOptionDto createOption(TranslationKey key, boolean selected) {
    OfferteOptionDto option = new OfferteOptionDto();
    option.setSelected(selected);
    option.setKey(key);
    option.setI8nKey(mapper.map(option.getKey()));
    option.setShowToCustomer(isShowToCustomer(option.getKey()));
    return option;
  }

  /**
   * Create a calculation option;
   * 
   * @param key The key to use in the create.
   * @param value The value to use in the create.
   * @return The generated option.
   */
  public OfferteOptionDto createCalculationOption(TranslationKey key, BigDecimal value) {
    OfferteOptionDto option = createOption(key, true);
    option.setCalculationOption(true);
    option.setValue(value + "");
    return option;
  }

  /**
   * Check if the key should be shown to the customers.
   * 
   * @param key The key to check.
   * @return True if the key is not ADR_MIN.
   */
  public boolean isShowToCustomer(TranslationKey key) {
    if (key == TranslationKey.ADR_MINIMUM) {
      return false;
    }
    return true;
  }

  /**
   * Check if its a calculation key.
   * 
   * @param key The key to check.
   * @return True when a calculattion key, false otherwise.
   */
  public boolean isCalculationKey(TranslationKey key) {
    switch (key) {
      case ADR_SURCHARGE:
        return true;
      case ADR_MINIMUM:
        return true;
      case DIESEL_SURCHARGE:
        return true;
      case CHF_SURCHARGE:
        return true;
      case IMPORT_FORM:
        return true;
      case EXPORT_FORM:
        return true;
      default:
        return false;
    }
  }

  /**
   * Get the calcaultion type for the key.
   * 
   * @param key The key to get the type for.
   * @return The calculation value type.
   */
  public CalculationValueType getCalculationValueTypeForCalculationKey(TranslationKey key) {
    switch (key) {
      case ADR_SURCHARGE:
        return CalculationValueType.PERCENTAGE;
      case ADR_MINIMUM:
        return CalculationValueType.CONSTANT;
      case DIESEL_SURCHARGE:
        return CalculationValueType.CONSTANT;
      case CHF_SURCHARGE:
        return CalculationValueType.CONSTANT;
      case IMPORT_FORM:
        return CalculationValueType.CONSTANT;
      case EXPORT_FORM:
        return CalculationValueType.CONSTANT;
      default:
        return null;
    }
  }

  /**
   * Check if a list of offerte options contains a key.
   * 
   * @param options the list of options to check.
   * @param key The key to check.
   * @return True when the key is present in the list.
   */
  public boolean offerteOptionsContainsKey(List<OfferteOptionDto> options, TranslationKey key) {
    for (OfferteOptionDto o : options) {
      if (o.getKey() == key) {
        return true;
      }
    }
    return false;
  }

  public TranslationKeyToi8nMapper getMapper() {
    return mapper;
  }

  public void setMapper(TranslationKeyToi8nMapper mapper) {
    this.mapper = mapper;
  }

}
