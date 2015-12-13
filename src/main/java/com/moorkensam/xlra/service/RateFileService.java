package com.moorkensam.xlra.service;

import java.util.List;

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
   * @param filter The filter to search for.
   * @return The list of ratefiles that match.
   */
  List<RateFile> getRateFilesForFilter(final RateFileSearchFilter filter);

  /**
   * Delete a rateifle.
   * 
   * @param rateFile The ratefile to delete.
   */
  void deleteRateFile(RateFile rateFile);

  /**
   * Get a full ratefile.
   * 
   * @param id The id of the ratefile to search.
   * @return The ratefile.
   */
  RateFile getFullRateFile(long id);

  /**
   * Get a ratefile without lazy loading it.
   * 
   * @param id The id of the ratefile.
   * @return The ratefile.
   */
  RateFile getRateFileWithoutLazyLoad(Long id);

  /**
   * Deletes a zone from a ratefile. Removes all ratelines that are attached to the zone.
   * 
   * @param zone The zone to delete.
   * @return The ratefile.
   */
  RateFile deleteZone(Zone zone);

  /**
   * Fetches a fully eager loaded ratefile for a set of filters given.
   * 
   * @param filter The filter to lookup.
   * @return The ratefile.
   */
  RateFile getFullRateFileForFilter(RateFileSearchFilter filter);

  Condition updateCondition(Condition condition);

  public RateFile getRateFileById(long id);

  public RateFile getRateFileForQuery(QuotationQuery query) throws RateFileException;

  /**
   * This method returns a new instance of ratefile for a set of searchcriteria. it is not persisted
   * in anyway its just a full copy of the ratefile that matched the searchfilter.
   * 
   * @param filter The filter to search for.
   * @return The resulting rf.
   */
  RateFile getCopyOfRateFileForFilter(RateFileSearchFilter filter);
}
