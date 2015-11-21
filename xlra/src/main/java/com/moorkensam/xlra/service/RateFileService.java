package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.RaiseRatesRecord;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.Zone;

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

	/**
	 * Raise all the ratelines in the given ratefiles with a given percentage.
	 * 
	 * @param rateFiles
	 * @param percentage
	 */
	void raiseRateFileRateLinesWithPercentage(List<RateFile> rateFiles,
			double percentage);

	RateFile getRateFileWithoutLazyLoad(Long id);

	/**
	 * This method returns a new instance of ratefile for a set of
	 * searchcriteria. it is not persisted in anyway its just a full copy of the
	 * ratefile that matched the searchfilter.
	 * 
	 * @param filter
	 * @return
	 */
	RateFile getCopyOfRateFileForFilter(RateFileSearchFilter filter);

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
	 * Deletes a zone from a ratefile. Removes all ratelines that are attached
	 * to the zone.
	 * 
	 * @param zone
	 * @return
	 */
	RateFile deleteZone(Zone zone);

	/**
	 * Fetches a fully eager loaded ratefile for a set of filters given.
	 * 
	 * @param filter
	 * @return
	 */
	RateFile getFullRateFileForFilter(RateFileSearchFilter filter);

	Condition updateCondition(Condition condition);

	public RateFile getRateFileById(long id);

	public RateFile getRateFileForQuery(QuotationQuery query)
			throws RateFileException;

}
