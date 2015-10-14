package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.CalcUtil;

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
	private RateFileDAO rateFileDAO;

	@Inject
	private LogDAO logDAO;

	@Override
	public List<RateFile> getAllRateFiles() {
		return rateFileDAO.getAllRateFiles();
	}

	@Override
	public void createRateFile(final RateFile rateFile) {
		logger.info("Creating ratefile for " + rateFile.getName());
		convertStringCodesToObjects(rateFile);
		rateFileDAO.createRateFile(rateFile);
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
		return rateFileDAO.updateRateFile(rateFile);
	}

	@Override
	public List<RateFile> getRateFilesForFilter(
			final RateFileSearchFilter filter) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetch ratefiles for filter " + filter);
		}
		return rateFileDAO.getRateFilesForFilter(filter);
	}

	@Override
	public void deleteRateFile(final RateFile rateFile) {
		logger.info("Deleting ratefile " + rateFile.getId());
		rateFileDAO.deleteRateFile(rateFile);
	}

	@Override
	public RateFile getFullRateFile(long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetching details for ratefile with id " + id);
		}
		RateFile rateFile = rateFileDAO.getFullRateFile(id);
		prepareRateFileForFrontend(rateFile);
		return rateFile;
	}

	private void prepareRateFileForFrontend(RateFile rateFile) {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparing ratefile for front end");
		}
		for (Zone z : rateFile.getZones()) {
			if (z.getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
				z.convertAlphaNumericPostalCodeListToString();
			} else {
				z.convertNumericalPostalCodeListToString();
			}
		}
		fillUpRelationalProperties(rateFile);
		fillUpRateLineRelationalMap(rateFile);
	}

	/**
	 * This method fetches the details for the ratelines of the ratefile. The
	 * details fetched are the columns and measurements.
	 * 
	 * @param rateFile
	 */
	protected void fillUpRelationalProperties(RateFile rateFile) {
		logger.debug("Filling in measurement rows & columnheaders for ratefile");
		rateFile.setColumns(new ArrayList<String>());
		for (Zone z : rateFile.getZones()) {
			rateFile.getColumns().add(z.getAsColumnHeader());
		}
		List<Double> measurementRows = new ArrayList<Double>();
		for (RateLine rl : rateFile.getRateLines()) {
			if (!measurementRows.contains(rl.getMeasurement())) {
				measurementRows.add(rl.getMeasurement());
			}
		}
		Collections.sort(measurementRows);
		rateFile.setMeasurementRows(measurementRows);

	}

	/**
	 * Fills up the transient attribute relationRatelines of the ratefile
	 * object. In order for this to work the columns and measurements have to be
	 * set on the ratefile object.
	 * 
	 * @param rateFile
	 */
	protected void fillUpRateLineRelationalMap(RateFile rateFile) {
		logger.info("Building relation rateline map for ratefile "
				+ rateFile.getId());
		List<List<RateLine>> relationRateLines = new ArrayList<List<RateLine>>();
		for (Double i : rateFile.getMeasurementRows()) {
			List<RateLine> rateLines = new ArrayList<RateLine>();
			for (RateLine rl : rateFile.getRateLines()) {
				if (rl.getMeasurement() == (i)) {
					rateLines.add(rl);
				}
			}
			Collections.sort(rateLines);
			relationRateLines.add(rateLines);
		}
		rateFile.setRelationalRateLines(relationRateLines);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			double percentage) {
		logger.info("Raising ratelines with percentage " + percentage);
		applyRateOperation(rateFiles, percentage, RateOperation.RAISE);
	}

	/**
	 * Applies a RateOperation to a set of ratefiles. Concrete this means that
	 * the values in each rateline in each ratefile will be diminished or added
	 * with the percentage.
	 * 
	 * @param rateFiles
	 * @param percentage
	 * @param operation
	 */
	protected void applyRateOperation(List<RateFile> rateFiles,
			double percentage, RateOperation operation) {
		BigDecimal rateLineMultiplier = new BigDecimal(0.0d);
		rateLineMultiplier = CalcUtil.convertPercentageToMultiplier(percentage);
		List<RateFile> fullRateFiles = fetchFullRateFiles(rateFiles);

		raiseRateFiles(rateLineMultiplier, fullRateFiles, operation);

		createAndSaveRateOperationLogRecord(percentage, fullRateFiles,
				operation);

		for (RateFile rf : fullRateFiles) {
			if (operation == RateOperation.RAISE) {
				logger.info("Saving raised rates file " + rf.getName());
			} else {
				logger.info("Saving subtracted rates file " + rf.getName());
			}
			rateFileDAO.updateRateFile(rf);
		}
	}

	/**
	 * Raises a list of ratefiles their values.
	 * 
	 * @param rateLineMultiplier
	 * @param fullRateFiles
	 * @param operation
	 */
	protected void raiseRateFiles(BigDecimal rateLineMultiplier,
			List<RateFile> fullRateFiles, RateOperation operation) {
		for (RateFile rf : fullRateFiles) {
			if (operation == RateOperation.RAISE) {
				raiseRateLinesOfRateFile(rateLineMultiplier, rf);
			}
			if (operation == RateOperation.SUBTRACT) {
				lowerRateLinesOfRateFile(rateLineMultiplier, rf);
			}
		}
	}

	protected void lowerRateLinesOfRateFile(BigDecimal multiplier, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal bd = new BigDecimal(rl.getValue().doubleValue()
					/ multiplier.doubleValue());
			BigDecimal bd2 = bd.setScale(2, RoundingMode.HALF_UP);
			rl.setValue(bd2);
		}
	}

	/**
	 * Fetches a list of ratefiles that are fully eager loaded.
	 * 
	 * @param rateFiles
	 * @param rateLineMultiplier
	 * @return
	 */
	protected List<RateFile> fetchFullRateFiles(List<RateFile> rateFiles) {
		List<RateFile> fullRateFiles = new ArrayList<RateFile>();
		for (RateFile rf : rateFiles) {
			fullRateFiles.add(rateFileDAO.getFullRateFile(rf.getId()));
		}

		return fullRateFiles;
	}

	private void createAndSaveRateOperationLogRecord(double percentage,
			List<RateFile> fullRateFiles, RateOperation operation) {
		logger.debug("Creating log record for " + operation);
		RaiseRatesRecord logRecord = new RaiseRatesRecord();
		logRecord.setPercentage(percentage);
		logRecord.setRateFiles(fullRateFiles);
		logRecord.setLogDate(new Date());
		logRecord.setOperation(operation);
		logDAO.createLogRecord(logRecord);
	}

	private void raiseRateLinesOfRateFile(BigDecimal rateLineMultiplier,
			RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal bd = new BigDecimal(rl.getValue().doubleValue()
					* rateLineMultiplier.doubleValue());
			BigDecimal bd2 = bd.setScale(2, RoundingMode.HALF_UP);
			rl.setValue(bd2);
		}
	}

	@Override
	public RateFile getRateFileWithoutLazyLoad(Long id) {
		logger.info("Fetching ratefiles and finding in memory");
		List<RateFile> rfs = rateFileDAO.getAllRateFiles();
		for (RateFile rf : rfs) {
			if (rf.getId() == id) {
				return rf;
			}
		}
		return null;
	}

	@Override
	public RateFile getCopyOfRateFileForFilter(RateFileSearchFilter filter) {
		List<RateFile> rateFiles = rateFileDAO.getRateFilesForFilter(filter);
		if (rateFiles.isEmpty()) {
			return null;
		}
		RateFile original = rateFiles.get(0);
		RateFile fullOriginal = getFullRateFile(original.getId());
		RateFile copy = fullOriginal.deepCopy();
		return copy;
	}

	@Override
	public List<RaiseRatesRecord> getRaiseRatesLogRecordsThatAreNotUndone() {
		return logDAO.getAllRaiseRateLogRecords();
	}

	@Override
	public void undoLatestRatesRaise() {
		RaiseRatesRecord lastRaise = logDAO.getLastRaiseRates();
		if (lastRaise == null) {
			logger.info("No raise found in the database");
		} else {
			logger.info("Subtracting latest raise");
			applyRateOperation(lastRaise.getRateFiles(),
					lastRaise.getPercentage(), RateOperation.SUBTRACT);
			lastRaise.setUndone(true);
			logDAO.updateRaiseRatesRecord(lastRaise);
		}
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
		rateFileDAO.lazyLoadRateFile(rf);
		prepareRateFileForFrontend(rf);
		return rf;
	}

	@Override
	public RateFile getFullRateFileForFilter(RateFileSearchFilter filter) {
		RateFile rf = rateFileDAO.getFullRateFileForFilter(filter);
		prepareRateFileForFrontend(rf);
		return rf;
	}
}
