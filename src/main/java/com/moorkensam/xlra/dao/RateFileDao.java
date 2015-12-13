package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.RateLine;

public interface RateFileDao {

  List<RateFile> getAllRateFiles();

  void createRateFile(RateFile rateFile);

  RateFile updateRateFile(RateFile rateFile);

  List<RateFile> getRateFilesForFilter(RateFileSearchFilter filter);

  void deleteRateFile(RateFile rateFile);

  RateFile getFullRateFile(long rateFileId);

  public List<RateLine> findRateLinesForRateFile(RateFile rf);

  public void lazyLoadRateFile(RateFile rf);

  public RateFile getFullRateFileForFilter(RateFileSearchFilter filter);
}
