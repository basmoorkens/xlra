package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.RaiseRatesRecord;
import com.moorkensam.xlra.model.configuration.LogRecord;

public class LogDAOImpl extends BaseDAO implements LogDAO {

	@Override
	public void createLogRecord(LogRecord record) {
		getEntityManager().persist(record);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RaiseRatesRecord> getAllRaiseRateLogRecords() {
		Query query = getEntityManager().createNamedQuery(
				"RaiseRatesRecord.findAll");
		List<RaiseRatesRecord> records = (List<RaiseRatesRecord>) query
				.getResultList();
		for (RaiseRatesRecord r : records) {
			r.getRateFiles().size();
		}
		return records;
	}

	@Override
	public RaiseRatesRecord getLastRaiseRates() {
		Query query = getEntityManager().createNamedQuery(
				"RaiseRatesRecord.findLast");
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
}
