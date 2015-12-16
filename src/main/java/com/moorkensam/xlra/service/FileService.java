package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.error.PdfException;

import java.io.File;


public interface FileService {

  public File getOffertePdfFromFileSystem(String offerteFileName);

  public String getFullPdfFilePathForIdentifier(String identifier);

  public String getTemporaryFilePathForPdf(String identifier);

  public String convertTransientOfferteToFinal(String identifier) throws PdfException;
}
