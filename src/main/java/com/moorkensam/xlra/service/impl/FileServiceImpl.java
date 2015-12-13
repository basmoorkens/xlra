package com.moorkensam.xlra.service.impl;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.error.PdfException;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

public class FileServiceImpl implements FileService {

  private ConfigurationLoader configurationLoader;

  private final static Logger logger = LogManager.getLogger();

  private final static String PDF_SUFFIX = "_offerte.pdf";

  public FileServiceImpl() {
    configurationLoader = ConfigurationLoader.getInstance();
  }

  @Override
  public File getOffertePdfFromFileSystem(String offerteFileName) {
    return new File(offerteFileName);
  }

  @Override
  public String getFullPdfFilePathForIdentifier(String identifier) {
    String baseUrl = configurationLoader.getProperty(ConfigurationLoader.PDF_GENERATION_PATH);
    return baseUrl + identifier + PDF_SUFFIX;
  }

  @Override
  public String getTemporaryFilePathForPdf(String identifier) {
    String baseUrl = configurationLoader.getProperty(ConfigurationLoader.PDF_TEMPORARY_PATH);
    return baseUrl + identifier + PDF_SUFFIX;
  }

  @Override
  public String convertTransientOfferteToFinal(String identifier) throws PdfException {
    String temporaryFilePathForPdf = getTemporaryFilePathForPdf(identifier);
    File transientPdf = new File(temporaryFilePathForPdf);
    String finalFile = getFullPdfFilePathForIdentifier(identifier);
    if (!transientPdf.renameTo(new File(finalFile))) {
      logger.error("Could not move transient pdf from " + temporaryFilePathForPdf + " to "
          + finalFile);
      throw new PdfException("Could not finalize PDF, it still exists in the temp file under "
          + temporaryFilePathForPdf);
    }
    return finalFile;
  }

}
