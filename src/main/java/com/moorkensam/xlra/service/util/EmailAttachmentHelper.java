package com.moorkensam.xlra.service.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.moorkensam.xlra.model.offerte.QuotationResult;

public class EmailAttachmentHelper {

	public MimeBodyPart generatedPdfAttachment(QuotationResult result)
			throws MessagingException {
		MimeBodyPart pdfAttachment = new MimeBodyPart();
		DataSource pdfSource = new FileDataSource(result.getPdfFileName());
		pdfAttachment.setDataHandler(new DataHandler(pdfSource));
		pdfAttachment.setFileName(FileUtil.getFileNameFromPath(result
				.getPdfFileName()));
		return pdfAttachment;
	}
	
}
