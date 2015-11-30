package com.moorkensam.xlra.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConditionDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.LogRecordFactory;
import com.moorkensam.xlra.service.util.QuotationUtil;

/**
 * This service contains the business logic for ratefiles.
 * 
 * @author bas
 *
 */
@Stateless
public class RateFileServiceImpl extends BaseDAO implements RateFileService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private ConditionDAO conditionDAO;

	@Inject
	private RateFileDAO rateFileDAO;

	private QuotationUtil quotationUtil;

	private LogRecordFactory logRecordFactory;

	@PostConstruct
	public void init() {
		setLogRecordFactory(LogRecordFactory.getInstance());
		quotationUtil = QuotationUtil.getInstance();
	}

	@Override
	public List<RateFile> getAllRateFiles() {
		return getRateFileDAO().getAllRateFiles();
	}

	@Override
	public void createRateFile(final RateFile rateFile) {
		logger.info("Creating ratefile for " + rateFile.getName());
		convertStringCodesToObjects(rateFile);
		getRateFileDAO().createRateFile(rateFile);
	}

	private void convertStringCodesToObjects(final RateFile rateFile) {
		for (Zone zone : rateFile.getZones()) {
			zone.convertAlphaNumericPostalCodeStringToList();
			zone.convertNumericalPostalCodeStringToList();
		}
	}

	@Override
	public RateFile updateRateFile(final RateFile rateFile) {
		convertStringCodesToObjects(rateFile);
		return getRateFileDAO().updateRateFile(rateFile);
	}

	@Override
	public List<RateFile> getRateFilesForFilter(
			final RateFileSearchFilter filter) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetch ratefiles for filter " + filter);
		}
		return getRateFileDAO().getRateFilesForFilter(filter);
	}

	@Override
	public void deleteRateFile(final RateFile rateFile) {
		logger.info("Deleting ratefile " + rateFile.getId());
		getRateFileDAO().deleteRateFile(rateFile);
	}

	@Override
	public RateFile getFullRateFile(long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetching details for ratefile with id " + id);
		}
		RateFile rateFile = getRateFileDAO().getFullRateFile(id);
		return rateFile;
	}

	@Override
	public RateFile getRateFileWithoutLazyLoad(Long id) {
		List<RateFile> rfs = getRateFileDAO().getAllRateFiles();
		for (RateFile rf : rfs) {
			if (rf.getId() == id) {
				return rf;
			}
		}
		return null;
	}

	@Override
	public RateFile getCopyOfRateFileForFilter(RateFileSearchFilter filter) {
		List<RateFile> rateFiles = getRateFileDAO().getRateFilesForFilter(
				filter);
		if (rateFiles.isEmpty()) {
			return null;
		}
		RateFile original = rateFiles.get(0);
		RateFile fullOriginal = getFullRateFile(original.getId());
		RateFile copy = fullOriginal.deepCopy();

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
		getRateFileDAO().getFullRateFile(rf.getId());
		return rf;
	}

	@Override
	public RateFile getFullRateFileForFilter(RateFileSearchFilter filter) {
		RateFile rf = getRateFileDAO().getFullRateFileForFilter(filter);
		return rf;
	}

	@Override
	public Condition updateCondition(Condition condition) {
		return conditionDAO.updateCondition(condition);
	}

	public LogRecordFactory getLogRecordFactory() {
		return logRecordFactory;
	}

	public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
		this.logRecordFactory = logRecordFactory;
	}

	@Override
	public RateFile getRateFileById(long id) {
		return getRateFileDAO().getFullRateFile(id);
	}

	@Override
	public RateFile getRateFileForQuery(QuotationQuery query)
			throws RateFileException {
		RateFileSearchFilter firstFilter = quotationUtil
				.createRateFileSearchFilterForQuery(query, false);
		RateFile rf = null;
		try {
			rf = getRateFileDAO().getFullRateFileForFilter(firstFilter);
		} catch (NoResultException nre) {
			try {
				logger.warn("Could not find specific ratefile for fullcustomer, falling back on standard filter properties.");
				RateFileSearchFilter fallBackFilter = quotationUtil
						.createRateFileSearchFilterForQuery(query, true);
				rf = getRateFileDAO().getFullRateFileForFilter(fallBackFilter);
			} catch (NoResultException nre2) {
				throw new RateFileException(
						"Could not find ratefile for searchfilter "
								+ firstFilter);
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

	public RateFileDAO getRateFileDAO() {
		return rateFileDAO;
	}

	public void setRateFileDAO(RateFileDAO rateFileDAO) {
		this.rateFileDAO = rateFileDAO;
	}
}
