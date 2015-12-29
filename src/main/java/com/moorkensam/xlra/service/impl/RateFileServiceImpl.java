package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.ConditionDao;
import com.moorkensam.xlra.dao.RateFileDao;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.LogRecordFactory;
import com.moorkensam.xlra.service.util.QuotationUtil;
import com.moorkensam.xlra.service.util.RateUtil;
import com.moorkensam.xlra.service.util.TranslationKeyToi8nMapper;
import com.moorkensam.xlra.service.util.ZoneUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 * This service contains the business logic for ratefiles.
 * 
 * @author bas
 *
 */
@Stateless
public class RateFileServiceImpl extends BaseDao implements RateFileService {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  private ConditionDao conditionDao;

  @Inject
  private RateFileDao rateFileDao;

  @Inject
  private UserService userService;

  private TranslationKeyToi8nMapper translationMapper;

  private QuotationUtil quotationUtil;

  private LogRecordFactory logRecordFactory;

  private ZoneUtil zoneUtil;

  /**
   * Inits for the service.
   */
  @PostConstruct
  public void init() {
    setLogRecordFactory(LogRecordFactory.getInstance());
    quotationUtil = QuotationUtil.getInstance();
    translationMapper = new TranslationKeyToi8nMapper();
    zoneUtil = new ZoneUtil();
  }

  @Override
  public List<RateFile> getAllRateFiles() {
    return getRateFileDao().getAllRateFiles();
  }

  @Override
  public void createRateFile(final RateFile rateFile) {
    logger.info("Creating ratefile for " + rateFile.getName());
    rateFile.setLastEditedBy(userService.getCurrentUsername());
    convertStringCodesToObjects(rateFile);
    getRateFileDao().createRateFile(rateFile);
  }

  private void convertStringCodesToObjects(final RateFile rateFile) {
    for (Zone zone : rateFile.getZones()) {
      zone.setAlphaNumericalPostalCodes(zoneUtil.convertAlphaNumericPostalCodeStringToList(zone
          .getAlphaNumericPostalCodesAsString()));
      zone.setNumericalPostalCodes(zoneUtil.convertNumericalPostalCodeStringToList(zone
          .getNumericalPostalCodesAsString()));
    }
  }

  @Override
  public RateFile updateRateFile(final RateFile rateFile) {
    convertStringCodesToObjects(rateFile);
    rateFile.setLastEditedBy(userService.getCurrentUsername());
    RateFile updateRateFile = getRateFileDao().updateRateFile(rateFile);
    fillInConditionKeyTranslations(updateRateFile);
    return updateRateFile;
  }

  @Override
  public List<RateFile> getRateFilesForFilter(final RateFileSearchFilter filter) {
    if (logger.isDebugEnabled()) {
      logger.debug("Fetch ratefiles for filter " + filter);
    }
    return getRateFileDao().getRateFilesForFilter(filter);
  }

  @Override
  public void deleteRateFile(final RateFile rateFile) {
    logger.info("Deleting ratefile " + rateFile.getId());
    getRateFileDao().deleteRateFile(rateFile);
  }

  @Override
  public RateFile getFullRateFile(long id) {
    if (logger.isDebugEnabled()) {
      logger.debug("Fetching details for ratefile with id " + id);
    }
    RateFile rateFile = getRateFileDao().getFullRateFile(id);
    fillInConditionKeyTranslations(rateFile);
    return rateFile;
  }

  protected void fillInConditionKeyTranslations(final RateFile rf) {
    if (rf.getConditions() != null && !rf.getConditions().isEmpty()) {
      for (Condition condition : rf.getConditions()) {
        condition.setI8nKey(translationMapper.map(condition.getConditionKey()));
      }
    }
  }

  @Override
  public RateFile getRateFileWithoutLazyLoad(Long id) {
    List<RateFile> rfs = getRateFileDao().getAllRateFiles();
    for (RateFile rf : rfs) {
      if (rf.getId() == id) {
        return rf;
      }
    }
    return null;
  }

  @Override
  public RateFile generateCustomerRateFileForFilterAndCustomer(RateFileSearchFilter filter)
      throws RateFileException {
    try {
      rateFileDao.getFullRateFileForFilter(filter);
      throw new RateFileException("A ratefile already exists for this customer and these options.");
    } catch (NoResultException e) {
      try {
        Customer customer = filter.getCustomer();
        filter.setCustomer(null);
        RateFile baseRateFile = getRateFileDao().getFullRateFileForFilter(filter);
        fillInConditionKeyTranslations(baseRateFile);
        RateFile copy = copyRateFile(filter, customer, baseRateFile);
        filter.setCustomer(customer);
        return copy;
      } catch (NoResultException nre2) {
        throw new RateFileException("Could not find a ratefile to copy from for this filter.");
      }
    }
  }

  private RateFile copyRateFile(RateFileSearchFilter filter, Customer customer,
      RateFile baseRateFile) {
    RateFile copy = baseRateFile.deepCopy();
    copy.setName(RateUtil.generateNameForCustomerRateFile(filter, customer));
    copy.setCustomer(customer);
    return copy;
  }

  @Override
  public RateFile deleteZone(Zone zone) {
    logger.info("Deleting zone " + zone.getName());
    RateFile rf = zone.getRateFile();
    rf.getZones().remove(zone);
    zone.setRateFile(null);
    Iterator<RateLine> iterator = rf.getRateLines().iterator();
    RateLine rl;
    while (iterator.hasNext()) {
      rl = iterator.next();
      if (rl.getZone().getId() == zone.getId()) {
        iterator.remove();
        rl.setRateFile(null);
        break;
      }
    }
    rf = updateRateFile(rf);
    getRateFileDao().getFullRateFile(rf.getId());
    return rf;
  }

  @Override
  public RateFile getFullRateFileForFilter(RateFileSearchFilter filter) {
    RateFile rf = getRateFileDao().getFullRateFileForFilter(filter);
    fillInConditionKeyTranslations(rf);
    return rf;
  }

  @Override
  public Condition updateCondition(Condition condition) {
    return conditionDao.updateCondition(condition);
  }

  public LogRecordFactory getLogRecordFactory() {
    return logRecordFactory;
  }

  public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
    this.logRecordFactory = logRecordFactory;
  }

  @Override
  public RateFile getRateFileById(long id) {
    RateFile fullRateFile = getRateFileDao().getFullRateFile(id);
    fillInConditionKeyTranslations(fullRateFile);
    return fullRateFile;
  }

  @Override
  public RateFile getRateFileForQuery(QuotationQuery query) throws RateFileException {
    RateFileSearchFilter firstFilter =
        quotationUtil.createRateFileSearchFilterForQuery(query, false);
    RateFile rf = null;
    try {
      rf = getRateFileDao().getFullRateFileForFilter(firstFilter);
      fillInConditionKeyTranslations(rf);
    } catch (NoResultException nre) {
      try {
        logger.warn("Could not find specific ratefile for fullcustomer, "
            + "falling back on standard filter properties.");
        RateFileSearchFilter fallBackFilter =
            quotationUtil.createRateFileSearchFilterForQuery(query, true);
        rf = getRateFileDao().getFullRateFileForFilter(fallBackFilter);
      } catch (NoResultException nre2) {
        throw new RateFileException("Could not find ratefile for searchfilter " + firstFilter);
      }
    }
    return rf;
  }

  public QuotationUtil getQuotationUtil() {
    return quotationUtil;
  }

  public void setQuotationUtil(QuotationUtil quotationUtil) {
    this.quotationUtil = quotationUtil;
  }

  public RateFileDao getRateFileDao() {
    return rateFileDao;
  }

  public void setRateFileDao(RateFileDao rateFileDao) {
    this.rateFileDao = rateFileDao;
  }

  public TranslationKeyToi8nMapper getTranslationMapper() {
    return translationMapper;
  }

  public void setTranslationMapper(TranslationKeyToi8nMapper translationMapper) {
    this.translationMapper = translationMapper;
  }

  @Override
  public List<RateFileIdNameDto> getRateFilesIdAndNamesForAutoComplete() {
    return rateFileDao.getRateFilesIdAndNamesForAutoComplete();
  }

  @Override
  public List<RateFile> getRateFilesByIdList(List<Long> ids) {
    return rateFileDao.getRateFilesById(ids);
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public ZoneUtil getZoneUtil() {
    return zoneUtil;
  }

  public void setZoneUtil(ZoneUtil zoneUtil) {
    this.zoneUtil = zoneUtil;
  }
}
