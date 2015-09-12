package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public interface RateFileDAO {

	List<RateFile> getAllRateFiles();

	void createRateFile(RateFile rateFile);

	void updateRateFile(RateFile rateFile);

	List<RateFile> getRateFilesForFilter(RateFileSearchFilter filter);

	void deleteRateFile(RateFile rateFile);
	
	RateFile getFullRateFile(long rateFileId);
	
}
