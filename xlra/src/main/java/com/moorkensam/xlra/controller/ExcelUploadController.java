package com.moorkensam.xlra.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

@ViewScoped
@ManagedBean
public class ExcelUploadController {

	
	public void handleFileUpload(FileUploadEvent event) throws IOException {
		InputStream inputStream;
		inputStream = event.getFile().getInputstream();
	}
}
