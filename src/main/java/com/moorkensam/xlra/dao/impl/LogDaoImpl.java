package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.LogDao;
import com.moorkensam.xlra.model.log.LogRecord;
import com.moorkensam.xlra.model.log.QuotationLogRecord;
import com.moorkensam.xlra.model.log.RaiseRatesRecord;
import com.moorkensam.xlra.model.log.RateLogRecord;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class LogDaoImpl extends BaseDao implements LogDao {

  @Override
  public void createLogRecord(LogRecord record) {
    getEntityManager().persist(record);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RaiseRatesRecord> getAllRaiseRateLogRecords() {
    Query query = getEntityManager().createNamedQuery("RaiseRatesRecord.findAll");
    List<RaiseRatesRecord> records = (List<RaiseRatesRecord>) query.getResultList();
    for (RaiseRatesRecord r : records) {
      r.getRateFiles().size();
    }
    return records;
  }

  @Override
  public RaiseRatesRecord getLastRaiseRates() {
    Query query = getEntityManager().createNamedQuery("RaiseRatesRecord.findLast");
    try {
      return (RaiseRatesRecord) query.getSingleResult();
    } catch (NoResultException exc) {
      return null;
    }
  }

  @Override
  public void updateRaiseRatesRecord(RaiseRatesRecord rr) {
    getEntityManager().merge(rr);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<RateLogRecord> getRateLogRecordsByDate(Date startDate, Date endDate) {
    Query query = getEntityManager().createNamedQuery("RateLogRecord.findByDates");
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return (List<RateLogRecord>) query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RaiseRatesRecord> getRaiseRatesLogRecordByDate(Date startDate, Date endDate) {
    Query query = getEntityManager().createNamedQuery("RaiseRatesRecord.findByDates");
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return (List<RaiseRatesRecord>) query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<QuotationLogRecord> getQuotationLogRecordsByDate(Date startDate, Date endDate) {
    Query query =
        getEntityManager().createNamedQuery("QuotationLogRecord.getQuotationLogRecordsByDate");
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return (List<QuotationLogRecord>) query.getResultList();
  }

}
