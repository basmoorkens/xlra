package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConditionTypeDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;
import com.moorkensam.xlra.service.RateFileService;

@Stateless
public class RateFileServiceImpl extends BaseDAO implements RateFileService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private RateFileDAO rateFileDAO;

	@Inject
	private ConditionTypeDAO conditionTypeDAO;

	@Override
	public List<RateFile> getAllRateFiles() {
		return rateFileDAO.getAllRateFiles();
	}

	@Override
	public void createRateFile(final RateFile rateFile) {
		logger.info("Creating ratefile for " + rateFile.getName());
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
		for (Integer i : rateFile.getMeasurementRows()) {
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
	public Condition updateTermsAndConditions(final Condition condition) {
		return conditionTypeDAO.updateCondition(condition);
	}

	@Override
	public void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			int percentage) {
		double rateLineMultiplier = convertPercentageToMultiplier(percentage);
		for (RateFile rf : rateFiles) {
			raiseRateLinesOfRateFile(rateLineMultiplier, rf);
		}

		for (RateFile rf : rateFiles) {
			rateFileDAO.updateRateFile(rf);
		}
	}

	private void raiseRateLinesOfRateFile(double rateLineMultiplier, RateFile rf) {
		for (RateLine rl : rf.getRateLines()) {
			rl.setValue(rl.getValue() * rateLineMultiplier);
		}
	}

	/**
	 * Converts the percentage to raise ratelines with from a value between 0
	 * and 100 to a double with which we can multiply each rateline value.
	 * 
	 * @param percentage
	 * @return
	 */
	protected double convertPercentageToMultiplier(int percentage) {
		int percentagePlus100 = percentage + 100;
		double result = (double) percentagePlus100 / 100;
		return result;
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

}
