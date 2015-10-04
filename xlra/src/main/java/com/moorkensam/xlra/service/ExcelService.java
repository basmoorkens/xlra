package com.moorkensam.xlra.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelService {

	void uploadRateFileExcel(HSSFWorkbook workBook);

}
