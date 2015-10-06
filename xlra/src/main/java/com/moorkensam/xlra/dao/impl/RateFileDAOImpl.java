package com.moorkensam.xlra.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public class RateFileDAOImpl extends BaseDAO implements RateFileDAO {

	private final static Logger logger = LogManager.getLogger();

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
		return getEntityManager().merge(rateFile);
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
		}
		if (filter.getCountry() != null) {
			builder.append("AND r.country = :country ");
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
		rf.getRateLines().size();
		rf.getConditions().size();
		rf.getZones().size();
		return rf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctZonesForRateFile(RateFile rateFile) {
		Query query = getEntityManager().createNamedQuery(
				"RateFile.findDistinctZones");
		query.setParameter("ratefileid", rateFile.getId());
		return (List<String>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RateLine> findRateLinesForRateFile(RateFile rf) {
		Query query = getEntityManager().createNamedQuery(
				"RateLine.findAllForRateFile", RateLine.class);
		query.setParameter("ratefileid", rf.getId());
		return (List<RateLine>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getDistinctMeasurementsForRateFile(RateFile rf) {
		Query query = getEntityManager().createNamedQuery(
				"RateFile.findDistinctMeasurements");
		query.setParameter("ratefileid", rf.getId());
		return (List<Double>) query.getResultList();
	}
}
