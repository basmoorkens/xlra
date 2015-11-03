package com.moorkensam.xlra.service.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

public class TemplateEngineTEst extends UnitilsJUnit4 {

	@TestedObject
	private TemplateEngine templateEngine;

	@Before
	public void setup() {
		templateEngine = new TemplateEngine();
		templateEngine.setStringTemplateLoader(new StringTemplateLoader());
		templateEngine.setConfiguration(new Configuration());
		templateEngine.setConfigLoader(ConfigurationLoader.getInstance());
	}

	@Test
	public void testParseUSerCreatedTemplate() throws TemplatingException {
		User user = new User();
		user.setFirstName("bas");
		user.setName("moorkens");
		String generatedMail = templateEngine.parseUserCreatedTemplate(user);
		Assert.assertTrue(generatedMail.contains("bas moorkens"));
	}

	@Test
	public void testParseTemplate() throws TemplatingException {
		String template = "Dear ${customer}, your offerte is here. please see attached pdf.";
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("customer", "Bas");
		String result = templateEngine.parseOfferteEmailTemplate(template, model);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.contains("Bas"));
	}
}
