package com.moorkensam.xlra.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import com.moorkensam.xlra.service.ExcelService;

@ViewScoped
@ManagedBean
public class ExcelUploadController {

	@Inject
	private ExcelService excelService;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		InputStream inputStream;
		inputStream = event.getFile().getInputstream();
		XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
		excelService.uploadRateFileExcel(workBook);
	}
}
