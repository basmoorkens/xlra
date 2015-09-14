package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public interface RateFileService {

	List<RateFile> getAllRateFiles();

	void createRateFile(RateFile rateFile);

	RateFile updateRateFile(RateFile rateFile);

	List<RateFile> getRateFilesForFilter(RateFileSearchFilter filter);

	void deleteRateFile(RateFile rateFile);
	
	RateFile getFullRateFile(long id); 
	
	Condition updateTermsAndConditions(Condition condition);
}
