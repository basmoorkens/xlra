package com.moorkensam.xlra.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

public class PdfServiceImpl implements PdfService {

	private ConfigurationLoader configLoader;

	private TemplateParseService templateParseService;

	private final static Logger logger = LogManager.getLogger();

	@PostConstruct
	public void init() {
		configLoader = ConfigurationLoader.getInstance();
	}

	@Override
	public void generateOffertePdf(QuotationResult offerte, Language language)
			throws FileNotFoundException, DocumentException,
			TemplatingException {
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream("test.pdf"));
		document.open();
		document.addAuthor(configLoader
				.getProperty(ConfigurationLoader.PDF_AUTHOR));
		document.addCreator(configLoader
				.getProperty(ConfigurationLoader.PDF_AUTHOR));
		document.addCreationDate();
		document.addTitle(configLoader
				.getProperty(ConfigurationLoader.PDF_TITLE_PREFIX));
		XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		String pdfBody = templateParseService
				.parseOffertePdf(offerte, language);
		try {
			worker.parseXHtml(pdfWriter, document, new StringReader(pdfBody));
			document.close();

		} catch (IOException e) {
			logger.error("Error creating inputstream from pdfbod text", e);
		}
		System.err.println("generated somethign..");
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
}
