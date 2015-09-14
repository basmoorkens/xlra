package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

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

	@Inject
	private RateFileDAO rateFileDAO;

	@Inject
	private ConditionTypeDAO conditionTypeDAO;

	@Override
	public List<RateFile> getAllRateFiles() {
		return rateFileDAO.getAllRateFiles();
	}

	@Override
	public void createRateFile(RateFile rateFile) {
		rateFileDAO.createRateFile(rateFile);
	}

	@Override
	public RateFile updateRateFile(RateFile rateFile) {
		return rateFileDAO.updateRateFile(rateFile);
	}

	@Override
	public List<RateFile> getRateFilesForFilter(RateFileSearchFilter filter) {
		return rateFileDAO.getRateFilesForFilter(filter);
	}

	@Override
	public void deleteRateFile(RateFile rateFile) {
		rateFileDAO.deleteRateFile(rateFile);
	}

	@Override
	public RateFile getFullRateFile(long id) {
		RateFile rateFile = rateFileDAO.getFullRateFile(id);
		rateFile.setRateLines(rateFileDAO.findRateLinesForRateFile(rateFile));
		fillUpRelationalProperties(rateFile);
		return rateFile;
	}

	private void fillUpRelationalProperties(RateFile rateFile) {
		rateFile.setColumns(rateFileDAO.getDistinctZonesForRateFile(rateFile));
		rateFile.setMeasurementRows(rateFileDAO
				.getDistinctMeasurementsForRateFile(rateFile));
		fillUpRateLineRelationalMap(rateFile);
	}

	private void fillUpRateLineRelationalMap(RateFile rateFile) {
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
	public Condition updateTermsAndConditions(Condition condition) {
		return conditionTypeDAO.updateCondition(condition);
	}

}
