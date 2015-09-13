package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConditionTypeDAO;
import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
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
	public void updateRateFile(RateFile rateFile) {
		rateFileDAO.updateRateFile(rateFile);
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
		return rateFileDAO.getFullRateFile(id);
	}

	@Override
	public Condition updateTermsAndConditions(Condition condition) {
		return conditionTypeDAO.updateCondition(condition); 
	}

}
