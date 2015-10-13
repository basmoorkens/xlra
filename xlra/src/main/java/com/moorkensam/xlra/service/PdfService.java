package com.moorkensam.xlra.service;

import java.io.FileNotFoundException;

import com.itextpdf.text.DocumentException;

public interface PdfService {

	public void generatePdf() throws FileNotFoundException, DocumentException;
}
