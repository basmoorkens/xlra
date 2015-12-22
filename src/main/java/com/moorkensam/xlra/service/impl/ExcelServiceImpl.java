package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.excel.ExcelToModelMapper;
import com.moorkensam.xlra.service.util.excel.ExcelUploadParser;
import com.moorkensam.xlra.service.util.excel.RateFileToExcelMapper;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * This service can be used to upload excel files.
 * 
 * @author bas
 *
 */
@Stateless
public class ExcelServiceImpl implements ExcelService {

  @Inject
  private RateFileService rateFileService;

  private ExcelToModelMapper mapper;

  private ExcelUploadParser parser;

  private RateFileToExcelMapper inverseMapper;

  @PostConstruct
  public void init() {
    this.mapper = new ExcelToModelMapper();
    this.parser = new ExcelUploadParser();
    this.inverseMapper = new RateFileToExcelMapper();
  }

  @Override
  public void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook) {
    ExcelUploadUtilData parseRateFileExcel = getParser().parseRateFileExcel(workBook);
    getMapper().mapExcelToModel(rf, parseRateFileExcel);
    getRateFileService().createRateFile(rf);
  }

  @Override
  public XSSFWorkbook exportRateFileToExcel(RateFile rateFile) {
    return inverseMapper.map(rateFile);
  }

  public ExcelUploadParser getParser() {
    return parser;
  }

  public void setParser(ExcelUploadParser parser) {
    this.parser = parser;
  }

  public ExcelToModelMapper getMapper() {
    return mapper;
  }

  public void setMapper(ExcelToModelMapper mapper) {
    this.mapper = mapper;
  }

  public RateFileService getRateFileService() {
    return rateFileService;
  }

  public void setRateFileService(RateFileService rateFileService) {
    this.rateFileService = rateFileService;
  }

  public RateFileToExcelMapper getInverseMapper() {
    return inverseMapper;
  }

  public void setInverseMapper(RateFileToExcelMapper inverseMapper) {
    this.inverseMapper = inverseMapper;
  }
}
