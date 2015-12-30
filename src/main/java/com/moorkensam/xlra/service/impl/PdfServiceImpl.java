package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.PdfService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class PdfServiceImpl implements PdfService {

  private ConfigurationLoader configLoader;

  @Inject
  private TemplateParseService templateParseService;

  private FileService fileService;

  private static final Logger logger = LogManager.getLogger();

  @PostConstruct
  public void init() {
    configLoader = ConfigurationLoader.getInstance();
    setFileService(new FileServiceImpl());
  }

  @Override
  public void generateTransientOffertePdf(QuotationResult offerte, Language language)
      throws FileNotFoundException, DocumentException, TemplatingException {
    if (logger.isDebugEnabled()) {
      logger.debug("Generating pdf result for " + offerte.getOfferteUniqueIdentifier());
    }
    Document document = new Document();
    String fullPdfFileName =
        fileService.getTemporaryFilePathForPdf(offerte.getOfferteUniqueIdentifier());

    offerte.setPdfFileName(fullPdfFileName);
    final PdfWriter pdfWriter =
        PdfWriter.getInstance(document, new FileOutputStream(fullPdfFileName));
    document.open();
    fillInHeaderProperties(document);

    Pipeline<?> pipeline = setupPipelines(document, pdfWriter);

    XMLParser parser = new XMLParser(new XMLWorker(pipeline, true));
    String pdfBody = templateParseService.parseOffertePdf(offerte, language);
    logger.info("Generated pdf " + fullPdfFileName + " for offerte "
        + offerte.getOfferteUniqueIdentifier());
    try {
      parser.parse(new StringReader(pdfBody));
      document.close();
    } catch (IOException e) {
      logger.error("Error creating inputstream from pdfbod text", e);
    }
  }

  private Pipeline<?> setupPipelines(Document document, final PdfWriter pdfWriter) {
    HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
    htmlContext.setImageProvider(new AbstractImageProvider() {

      @Override
      public String getImageRootPath() {
        return configLoader.getProperty(ConfigurationLoader.IMAGE_PATH);
      }
    });

    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
    CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
    Pipeline<?> pipeline =
        new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(
            document, pdfWriter)));
    return pipeline;
  }

  private void fillInHeaderProperties(Document document) {
    document.addAuthor(configLoader.getProperty(ConfigurationLoader.PDF_AUTHOR));
    document.addCreator(configLoader.getProperty(ConfigurationLoader.PDF_AUTHOR));
    document.addCreationDate();
    document.addTitle(configLoader.getProperty(ConfigurationLoader.PDF_TITLE_PREFIX));
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

  public void setTemplateParseService(TemplateParseService templateParseService) {
    this.templateParseService = templateParseService;
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }
}
