package com.moorkensam.xlra.service;

import java.io.FileNotFoundException;

import com.itextpdf.text.DocumentException;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;

public interface PdfService {

	public void generateTransientOffertePdf(QuotationResult offerte, Language language)
			throws FileNotFoundException, DocumentException,
			TemplatingException;
}
