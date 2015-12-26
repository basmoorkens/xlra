package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.RateFileDao;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.service.util.ZoneUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Query;

public class RateFileDaoImpl extends BaseDao implements RateFileDao {

  private static final Logger logger = LogManager.getLogger();

  private ZoneUtil zoneUtil;

  @PostConstruct
  public void init() {
    zoneUtil = new ZoneUtil();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RateFile> getAllRateFiles() {
    Query query = getEntityManager().createNamedQuery("RateFile.findAll");
    return (List<RateFile>) query.getResultList();
  }

  @Override
  public void createRateFile(RateFile rateFile) {
    getEntityManager().persist(rateFile);
  }

  @Override
  public RateFile updateRateFile(RateFile rateFile) {
    RateFile rf = getEntityManager().merge(rateFile);
    prepareRateFileForFrontend(rf);
    return rf;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RateFile> getRateFilesForFilter(RateFileSearchFilter filter) {
    StringBuilder builder = buildSearchQuery(filter);

    Query query = getEntityManager().createQuery(builder.toString());
    buildSearchParams(filter, query);
    logger.info("Searching ratefile for filter: " + builder.toString());
    return (List<RateFile>) query.getResultList();
  }

  private void buildSearchParams(RateFileSearchFilter filter, Query query) {
    if (filter.getRateKind() != null) {
      query.setParameter("kindOfRate", filter.getRateKind());
    }
    if (filter.getMeasurement() != null) {
      query.setParameter("measurement", filter.getMeasurement());
    }
    if (filter.getCustomer() != null) {
      query.setParameter("customer", filter.getCustomer());
    }
    if (filter.getCountry() != null) {
      query.setParameter("country", filter.getCountry());
    }
    if (filter.getTransportationType() != null) {
      query.setParameter("transportType", filter.getTransportationType());
    }
  }

  private StringBuilder buildSearchQuery(RateFileSearchFilter filter) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT r FROM RateFile r WHERE r.deleted = false ");
    if (filter.getRateKind() != null) {
      builder.append("AND r.kindOfRate = :kindOfRate ");
    }
    if (filter.getMeasurement() != null) {
      builder.append("AND r.measurement = :measurement ");
    }
    if (filter.getCustomer() != null) {
      builder.append("AND r.customer = :customer ");
    } else {
      builder.append("AND r.customer IS NULL ");
    }
    if (filter.getCountry() != null) {
      builder.append("AND r.country = :country ");
    }
    if (filter.getTransportationType() != null) {
      builder.append("AND r.transportType = :transportType ");
    }
    return builder;
  }

  @Override
  public void deleteRateFile(RateFile rateFile) {
    rateFile.setDeleted(true);
    rateFile.setDeletedDateTime(new Date());
    getEntityManager().merge(rateFile);
  }

  @Override
  public RateFile getFullRateFile(long rateFileId) {
    RateFile rf = getEntityManager().find(RateFile.class, rateFileId);
    lazyLoadRateFile(rf);
    prepareRateFileForFrontend(rf);
    return rf;
  }

  /**
   * lazy load the ratefile.
   */
  public void lazyLoadRateFile(RateFile rf) {
    rf.getRateLines().size();
    rf.getConditions().size();
    for (Condition c : rf.getConditions()) {
      c.getTranslations().size();
    }
    rf.getZones().size();
    for (Zone zone : rf.getZones()) {
      if (zone.getAlphaNumericalPostalCodes() != null) {
        zone.getAlphaNumericalPostalCodes().size();
      }
      if (zone.getNumericalPostalCodes() != null) {
        zone.getNumericalPostalCodes().size();
      }
    }
    for (RateLine rl : rf.getRateLines()) {
      rl.getZone();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RateLine> findRateLinesForRateFile(RateFile rf) {
    Query query =
        getEntityManager().createNamedQuery("RateLine.findAllForRateFile", RateLine.class);
    query.setParameter("ratefileid", rf.getId());
    return (List<RateLine>) query.getResultList();
  }

  @Override
  public RateFile getFullRateFileForFilter(RateFileSearchFilter filter) {
    StringBuilder builder = buildSearchQuery(filter);

    Query query = getEntityManager().createQuery(builder.toString());
    buildSearchParams(filter, query);
    logger.info("Searching ratefile for filter: " + builder.toString());
    RateFile rf = (RateFile) query.getSingleResult();
    lazyLoadRateFile(rf);
    prepareRateFileForFrontend(rf);
    return rf;
  }

  protected void prepareRateFileForFrontend(RateFile rateFile) {
    if (logger.isDebugEnabled()) {
      logger.debug("Preparing ratefile for front end");
    }
    for (Zone z : rateFile.getZones()) {
      if (z.getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
        z.setAlphaNumericPostalCodesAsString(zoneUtil.convertAlphaNumericPostalCodeListToString(z
            .getAlphaNumericalPostalCodes()));
      } else {
        z.setNumericalPostalCodesAsString(zoneUtil.convertNumericalPostalCodeListToString(z
            .getNumericalPostalCodes()));
      }
    }
    rateFile.fillUpRelationalProperties();
    rateFile.fillUpRateLineRelationalMap();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RateFileIdNameDto> getRateFilesIdAndNamesForAutoComplete() {
    Query query = getEntityManager().createNamedQuery("RateFile.findAllIdAndNames");
    return (List<RateFileIdNameDto>) query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RateFile> getRateFilesById(List<Long> ids) {
    Query query = getEntityManager().createNamedQuery("RateFile.findByIds");
    query.setParameter("ids", ids);
    List<RateFile> resultList = (List<RateFile>) query.getResultList();
    return resultList;
  }

  public ZoneUtil getZoneUtil() {
    return zoneUtil;
  }

  public void setZoneUtil(ZoneUtil zoneUtil) {
    this.zoneUtil = zoneUtil;
  }
}
