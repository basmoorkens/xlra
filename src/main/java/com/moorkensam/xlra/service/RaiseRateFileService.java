package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.RaiseRatesRecord;
import com.moorkensam.xlra.model.rate.RateFile;

public interface RaiseRateFileService {

	/**
	 * Fetches all the records of raises.
	 * 
	 * @return
	 */
	List<RaiseRatesRecord> getRaiseRatesLogRecordsThatAreNotUndone();

	/**
	 * Finds the last raise that happened on rates and undos it by reversing it.
	 */
	public void undoLatestRatesRaise();

	/**
	 * Raise all the ratelines in the given ratefiles with a given percentage.
	 * 
	 * @param rateFiles
	 * @param percentage
	 */
	void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			double percentage);
}
