package com.moorkensam.xlra.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.moorkensam.xlra.model.rate.RateFile;

public interface ExcelService {

	/**
	 * This method uploads an excel file to the application. The excel file is
	 * saved as a BLOB in the database and it is parsed into the application
	 * specific domain model objectd.
	 * 
	 * @param rf
	 *            The rateFile to attach the excel to.
	 * @param workBook
	 *            The Excel workbook to parse.
	 */
	void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook);

}
