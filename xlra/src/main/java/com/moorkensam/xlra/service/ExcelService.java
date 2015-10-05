package com.moorkensam.xlra.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.moorkensam.xlra.model.rate.RateFile;

public interface ExcelService {

	void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook);

}
