package com.moorkensam.xlra.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.error.TemplatingException;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * This class parses a freemarker template with the variables and template
 * provided.
 * 
 * @author bas
 *
 */
public class TemplateEngine {

	private final static Logger logger = LogManager.getLogger();

	private StringTemplateLoader stringTemplateLoader;

	private Configuration configuration;

	private static TemplateEngine engine;

	public static TemplateEngine getInstance() {
		if (engine == null) {
			engine = new TemplateEngine();
		}
		return engine;
	}

	private TemplateEngine() {
		stringTemplateLoader = new StringTemplateLoader();
		configuration = new Configuration();
	}

	public String parseEmailTemplate(String templateFromDb,
			Map<String, Object> dataModel) throws TemplatingException {
		logger.debug("Parsing template " + templateFromDb);
		StringWriter writer = new StringWriter();
		stringTemplateLoader.putTemplate("template", templateFromDb);
		configuration.setTemplateLoader(stringTemplateLoader);
		Template template;
		try {
			template = configuration.getTemplate("template");
			template.process(dataModel, writer);
		} catch (TemplateNotFoundException e) {
			logger.error(e);
			throw new TemplatingException("Template not found internaly", e);
		} catch (MalformedTemplateNameException e) {
			logger.error(e);
			throw new TemplatingException("Template malformated", e);
		} catch (ParseException e) {
			logger.error(e);
			throw new TemplatingException("Failed to parse template", e);
		} catch (IOException e) {
			logger.error(e);
			throw new TemplatingException("Could not write template", e);
		} catch (TemplateException e) {
			logger.error(e);
			throw new TemplatingException("General templating exception", e);
		}
		return writer.toString();
	}
}
