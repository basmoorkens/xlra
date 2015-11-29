package com.moorkensam.xlra.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

public class PdfServiceImpl implements PdfService {

	private ConfigurationLoader configLoader;

	@Inject
	private TemplateParseService templateParseService;

	private FileService fileService;

	private final static Logger logger = LogManager.getLogger();

	@PostConstruct
	public void init() {
		configLoader = ConfigurationLoader.getInstance();
		setFileService(new FileServiceImpl());
	}

	@Override
	public void generateTransientOffertePdf(QuotationResult offerte,
			Language language) throws FileNotFoundException, DocumentException,
			TemplatingException {
		Document document = new Document();
		String fullPdfFileName = fileService.getTemporaryFilePathForPdf(offerte
				.getOfferteUniqueIdentifier());

		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream(fullPdfFileName));
		offerte.setPdfFileName(fullPdfFileName);
		document.open();
		fillInHeaderProperties(document);
		XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		String pdfBody = templateParseService
				.parseOffertePdf(offerte, language);
		try {
			worker.parseXHtml(pdfWriter, document, new StringReader(pdfBody));
			document.close();

		} catch (IOException e) {
			logger.error("Error creating inputstream from pdfbod text", e);
		}
	}

	private void fillInHeaderProperties(Document document) {
		document.addAuthor(configLoader
				.getProperty(ConfigurationLoader.PDF_AUTHOR));
		document.addCreator(configLoader
				.getProperty(ConfigurationLoader.PDF_AUTHOR));
		document.addCreationDate();
		document.addTitle(configLoader
				.getProperty(ConfigurationLoader.PDF_TITLE_PREFIX));
	}

	public ConfigurationLoader getConfigLoader() {
		return configLoader;
	}

	public void setConfigLoader(ConfigurationLoader configLoader) {
		this.configLoader = configLoader;
	}

	public TemplateParseService getTemplateParseService() {
		return templateParseService;
	}

	public void setTemplateParseService(
			TemplateParseService templateParseService) {
		this.templateParseService = templateParseService;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
}
