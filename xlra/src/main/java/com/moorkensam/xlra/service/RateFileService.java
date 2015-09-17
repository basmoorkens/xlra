package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public interface RateFileService {

	List<RateFile> getAllRateFiles();

	void createRateFile(RateFile rateFile);

	RateFile updateRateFile(RateFile rateFile);

	/**
	 * Find a list of ratefiles for a given set of filters.
	 * 
	 * @param filter
	 * @return
	 */
	List<RateFile> getRateFilesForFilter(final RateFileSearchFilter filter);

	void deleteRateFile(RateFile rateFile);

	RateFile getFullRateFile(long id);

	Condition updateTermsAndConditions(Condition condition);

	/**
	 * Raise all the ratelines in the given ratefiles with a given percentage.
	 * 
	 * @param rateFiles
	 * @param percentage
	 */
	void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			int percentage);
	
	RateFile getRateFileWithoutLazyLoad(Long id);
}
