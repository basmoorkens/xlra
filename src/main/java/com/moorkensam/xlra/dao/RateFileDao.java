package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.dto.RateFileDto;
import com.moorkensam.xlra.dto.RateFileIdNameDto;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.RateLine;

import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

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

  List<RateFileIdNameDto> getRateFilesIdAndNamesForAutoComplete();

  List<RateFile> getRateFilesById(List<Long> ids);

  List<RateFile> getLazyRateFiles(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters);

  public int countRateFiles();

  public List<RateFileDto> getRateFileDtosForCustomer(Customer customer);
}
