package com.moorkensam.xlra.service.impl;

import javax.annotation.PostConstruct;
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

	@PostConstruct
	public void init() {
		this.mapper = new ExcelToModelMapper();
		this.parser = new ExcelUploadParser();
	}

	@Override
	public void uploadRateFileExcel(RateFile rf, XSSFWorkbook workBook) {
		ExcelUploadUtilData parseRateFileExcel = getParser()
				.parseRateFileExcel(workBook);
		getMapper().mapExcelToModel(rf, parseRateFileExcel);
		getRateFileService().createRateFile(rf);
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

}
