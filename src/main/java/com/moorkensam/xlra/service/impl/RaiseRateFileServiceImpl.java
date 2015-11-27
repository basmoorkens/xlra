package com.moorkensam.xlra.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.configuration.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.RateOperation;
import com.moorkensam.xlra.service.RaiseRateFileService;
import com.moorkensam.xlra.service.util.CalcUtil;
import com.moorkensam.xlra.service.util.LogRecordFactory;

@Stateless
public class RaiseRateFileServiceImpl implements RaiseRateFileService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private LogDAO logDAO;

	private LogRecordFactory logRecordFactory;

	@Inject
	private RateFileDAO rateFileDAO;

	private CalcUtil calcUtil;

	@PostConstruct
	public void init() {
		setCalcUtil(CalcUtil.getInstance());
		setLogRecordFactory(LogRecordFactory.getInstance());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			double percentage) {
		logger.info("Raising ratelines with percentage " + percentage);
		applyRateOperation(rateFiles, percentage, RateOperation.RAISE);
	}

	@Override
	public List<RaiseRatesRecord> getRaiseRatesLogRecordsThatAreNotUndone() {
		return getLogDAO().getAllRaiseRateLogRecords();
	}

	@Override
	public void undoLatestRatesRaise() {
		RaiseRatesRecord lastRaise = getLogDAO().getLastRaiseRates();
		if (lastRaise == null) {
			logger.info("No raise found in the database");
		} else {
			logger.info("Subtracting latest raise");
			applyRateOperation(lastRaise.getRateFiles(),
					lastRaise.getPercentage(), RateOperation.SUBTRACT);
			lastRaise.setUndone(true);
			getLogDAO().updateRaiseRatesRecord(lastRaise);
		}
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
		List<RateFile> fullRateFiles = fetchFullRateFiles(rateFiles);

		raiseRateFiles(percentage, fullRateFiles, operation);

		RaiseRatesRecord createRaiseRatesRecord = getLogRecordFactory()
				.createRaiseRatesRecord(operation, percentage, fullRateFiles);
		logDAO.createLogRecord(createRaiseRatesRecord);

		for (RateFile rf : fullRateFiles) {
			if (operation == RateOperation.RAISE) {
				logger.info("Saving raised rates file " + rf.getName());
			} else {
				logger.info("Saving subtracted rates file " + rf.getName());
			}
			getRateFileDAO().updateRateFile(rf);
		}
	}

	/**
	 * Raises a list of ratefiles their values.
	 * 
	 * @param rateLineMultiplier
	 * @param fullRateFiles
	 * @param operation
	 */
	protected void raiseRateFiles(double percentage,
			List<RateFile> fullRateFiles, RateOperation operation) {
		for (RateFile rf : fullRateFiles) {
			if (operation == RateOperation.RAISE) {
				raiseRateLinesOfRateFile(percentage, rf);
			}
			if (operation == RateOperation.SUBTRACT) {
				lowerRateLinesOfRateFile(percentage, rf);
			}
		}
	}

	private void raiseRateLinesOfRateFile(double percentage, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal percentageDividedby100 = new BigDecimal(
					percentage / 100d);
			percentageDividedby100 = calcUtil
					.roundBigDecimal(percentageDividedby100);
			BigDecimal addition = new BigDecimal(rl.getValue().doubleValue()
					* percentageDividedby100.doubleValue());
			addition = calcUtil.roundBigDecimal(addition);
			BigDecimal result = new BigDecimal(rl.getValue().doubleValue()
					+ addition.doubleValue());
			result = calcUtil.roundBigDecimal(result);
			rl.setValue(result);
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
			fullRateFiles.add(getRateFileDAO().getFullRateFile(rf.getId()));
		}

		return fullRateFiles;
	}

	protected void lowerRateLinesOfRateFile(double percentage, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			BigDecimal dividedBy100 = new BigDecimal(percentage / 100d);
			dividedBy100 = calcUtil.roundBigDecimal(dividedBy100);
			BigDecimal subtraction = new BigDecimal(rl.getValue().doubleValue()
					* dividedBy100.doubleValue());
			subtraction = calcUtil.roundBigDecimal(subtraction);
			BigDecimal bd2 = new BigDecimal(rl.getValue().doubleValue()
					- subtraction.doubleValue());
			bd2 = calcUtil.roundBigDecimal(bd2);
			rl.setValue(bd2);
		}
	}

	public LogDAO getLogDAO() {
		return logDAO;
	}

	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public LogRecordFactory getLogRecordFactory() {
		return logRecordFactory;
	}

	public void setLogRecordFactory(LogRecordFactory logRecordFactory) {
		this.logRecordFactory = logRecordFactory;
	}

	public RateFileDAO getRateFileDAO() {
		return rateFileDAO;
	}

	public void setRateFileDAO(RateFileDAO rateFileDAO) {
		this.rateFileDAO = rateFileDAO;
	}

	public CalcUtil getCalcUtil() {
		return calcUtil;
	}

	public void setCalcUtil(CalcUtil calcUtil) {
		this.calcUtil = calcUtil;
	}

}