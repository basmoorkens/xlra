package com.moorkensam.xlra.service.impl;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import com.itextpdf.text.DocumentException;
import com.moorkensam.xlra.service.PdfService;

public class PdfServiceTest extends UnitilsJUnit4 {

	private PdfService pdfService;

	@Before
	public void init() {
		pdfService = new PdfServiceImpl();
	}

	@Test
	public void testGeneratePdf() throws FileNotFoundException,
			DocumentException {
		pdfService.generatePdf();
	}

}
