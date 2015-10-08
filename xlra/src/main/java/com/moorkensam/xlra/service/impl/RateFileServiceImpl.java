package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.RateFileService;

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
		for (Zone zone : rateFile.getZones()) {
			zone.convertAlphaNumericPostalCodeStringToList();
			zone.convertNumericalPostalCodeStringToList();
		}
		rateFileDAO.createRateFile(rateFile);
	}

	@Override
	public RateFile updateRateFile(final RateFile rateFile) {
		return rateFileDAO.updateRateFile(rateFile);
	}

	@Override
	public List<RateFile> getRateFilesForFilter(
			final RateFileSearchFilter filter) {
		return rateFileDAO.getRateFilesForFilter(filter);
	}

	@Override
	public void deleteRateFile(final RateFile rateFile) {
		rateFileDAO.deleteRateFile(rateFile);
	}

	@Override
	public RateFile getFullRateFile(long id) {
		logger.info("Fetching details for ratefile with id " + id);
		RateFile rateFile = rateFileDAO.getFullRateFile(id);
		rateFile.setRateLines(rateFileDAO.findRateLinesForRateFile(rateFile));
		prepareRateFileForFrontend(rateFile);
		return rateFile;
	}

	private void prepareRateFileForFrontend(RateFile rateFile) {
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
		rateFile.setColumns(rateFileDAO.getDistinctZonesForRateFile(rateFile));
		rateFile.setMeasurementRows(rateFileDAO
				.getDistinctMeasurementsForRateFile(rateFile));
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
		applyRateOperation(rateFiles, percentage, RateOperation.RAISE);
	}

	protected void applyRateOperation(List<RateFile> rateFiles,
			double percentage, RateOperation operation) {
		double rateLineMultiplier = 0.0d;
		rateLineMultiplier = setupRateLineMultiplier(percentage,
				rateLineMultiplier);

		List<RateFile> fullRateFiles = fetchFullRateFiles(rateFiles,
				rateLineMultiplier);

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

	protected void raiseRateFiles(double rateLineMultiplier,
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

	protected void lowerRateLinesOfRateFile(double multiplier, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal bd = new BigDecimal(rl.getValue().doubleValue()
					/ multiplier);
			BigDecimal bd2 = bd.setScale(2, RoundingMode.HALF_UP);
			rl.setValue(bd2);
		}
	}

	protected List<RateFile> fetchFullRateFiles(List<RateFile> rateFiles,
			double rateLineMultiplier) {
		List<RateFile> fullRateFiles = new ArrayList<RateFile>();
		for (RateFile rf : rateFiles) {
			fullRateFiles.add(rateFileDAO.getFullRateFile(rf.getId()));
		}

		return fullRateFiles;
	}

	protected double setupRateLineMultiplier(double percentage,
			double rateLineMultiplier) {
		rateLineMultiplier = convertPercentageToMultiplier(percentage);
		return rateLineMultiplier;
	}

	private void createAndSaveRateOperationLogRecord(double percentage,
			List<RateFile> fullRateFiles, RateOperation operation) {
		RaiseRatesRecord logRecord = new RaiseRatesRecord();
		logRecord.setPercentage(percentage);
		logRecord.setRateFiles(fullRateFiles);
		logRecord.setLogDate(new Date());
		logRecord.setOperation(operation);
		logDAO.createLogRecord(logRecord);
	}

	private void raiseRateLinesOfRateFile(double rateLineMultiplier, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal bd = new BigDecimal(rl.getValue().doubleValue()
					* rateLineMultiplier);
			BigDecimal bd2 = bd.setScale(2, RoundingMode.HALF_UP);
			rl.setValue(bd2);
		}
	}

	/**
	 * Converts the percentage to raise ratelines with from a value between 0
	 * and 100 to a double with which we can multiply each rateline value.
	 * 
	 * @param percentage
	 * @return
	 */
	protected double convertPercentageToMultiplier(double percentage) {
		double percentagePlus100 = percentage + 100d;
		BigDecimal bd = new BigDecimal((double) percentagePlus100 / 100d);
		return double2Digits(bd);
	}

	private double double2Digits(BigDecimal input) {
		DecimalFormat df = new DecimalFormat("####,##");
		try {
			return (double) df.parse(input.doubleValue() + "");
		} catch (ParseException e) {
		}
		return 0.0d;
	}

	protected double convertPercentageToReservedMultiplier(double percentage) {
		BigDecimal bd = new BigDecimal((percentage) / 100d);
		return double2Digits(bd);
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
}
