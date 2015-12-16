package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.offerte.QuotationResult;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public class EmailAttachmentHelper {

  /**
   * Generate a pdf attachment mimebody part.
   * 
   * @param result The result to generate for.
   * @return the mimebodypart.
   * @throws MessagingException when the part couldnt be generated.
   */
  public MimeBodyPart generatedPdfAttachment(QuotationResult result) throws MessagingException {
    MimeBodyPart pdfAttachment = new MimeBodyPart();
    DataSource pdfSource = new FileDataSource(result.getPdfFileName());
    pdfAttachment.setDataHandler(new DataHandler(pdfSource));
    pdfAttachment.setFileName(FileUtil.getFileNameFromPath(result.getPdfFileName()));
    return pdfAttachment;
  }

}
