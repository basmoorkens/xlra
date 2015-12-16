package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

public interface PdfService {

  public void generateTransientOffertePdf(QuotationResult offerte, Language language)
      throws FileNotFoundException, DocumentException, TemplatingException;
}
