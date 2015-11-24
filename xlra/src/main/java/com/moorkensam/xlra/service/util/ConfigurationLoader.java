package com.moorkensam.xlra.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationLoader {

	private final static Logger logger = LogManager.getLogger();

	public final static String MAIL_SENDER = "email.from";

	public final static String APPLICATION_PASSWORD_SETUP_URL = "application.password.setup.url";

	public final static String APPLICATION_BASE_URL = "application.base.url";

	public final static String PDF_GENERATION_PATH = "pdf.generate.path";

	public final static String PDF_AUTHOR = "pdf.author";

	public final static String PDF_TITLE_PREFIX = "pdf.title.prefix";

	public static ConfigurationLoader instance;

	private Properties properties;

	private ConfigurationLoader() {
		try {
			loadProperties();
		} catch (IOException e) {
			logger.error("Failed to read in properties file " + e);
		}
	}

	public static ConfigurationLoader getInstance() {
		if (instance == null) {
			instance = new ConfigurationLoader();
		}
		return instance;
	}

	private void loadProperties() throws IOException {
		properties = new Properties();
		InputStream in = ConfigurationLoader.class
				.getResourceAsStream("/configuration.properties");
		properties.load(in);
		in.close();
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getProperty(String propertyToFind) {
		return properties.getProperty(propertyToFind);
	}

}
