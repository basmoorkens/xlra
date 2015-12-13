package com.moorkensam.xlra.service;

import java.io.File;

import com.moorkensam.xlra.model.error.PdfException;


public interface FileService {

  public File getOffertePdfFromFileSystem(String offerteFileName);

  public String getFullPdfFilePathForIdentifier(String identifier);

  public String getTemporaryFilePathForPdf(String identifier);

  public String convertTransientOfferteToFinal(String identifier) throws PdfException;
}
