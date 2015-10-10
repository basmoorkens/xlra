package com.moorkensam.xlra.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.ExcelService;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.ExcelToModelMapper;
import com.moorkensam.xlra.service.util.ExcelUploadParser;

@Stateless
public class ExcelServiceImpl implements ExcelService {

	@Inject
	private RateFileService rateFileService;

	private final static Logger logger = LogManager.getLogger();

	@Override
	public void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook) {
		logger.info("Parsing excel file to model");
		ExcelToModelMapper mapper = new ExcelToModelMapper();
		ExcelUploadParser parser = new ExcelUploadParser();
		ExcelUploadUtilData parseRateFileExcel = parser
				.parseRateFileExcel(workBook);
		mapper.mapExcelToModel(rf, parseRateFileExcel);
		rateFileService.createRateFile(rf);
	}

}
