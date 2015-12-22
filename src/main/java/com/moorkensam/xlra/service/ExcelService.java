package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.rate.RateFile;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelService {

  /**
   * This method parses a excel workbook into a ratefile.
   * 
   * @param rf The rateFile to attach the excel to.
   * @param workBook The Excel workbook to parse.
   */
  void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook);

  /**
   * Exports a ratefile back to a XSSFworkbook.
   * 
   * @param rateFile The ratefile to export.
   * @return The exported excel.
   */
  XSSFWorkbook exportRateFileToExcel(RateFile rateFile);

}
