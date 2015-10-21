package com.moorkensam.xlra.service.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.error.TemplatingException;

public class TemplateEngineTEst extends UnitilsJUnit4 {

	@TestedObject
	private TemplateEngine templateEngine;

	@Before
	public void setup() {

	}

	@Test
	public void testParseTemplate() throws TemplatingException {
		String template = "Dear ${customer}, your offerte is here. please see attached pdf.";
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("customer", "Bas");
		templateEngine = new TemplateEngine();
		String result = templateEngine.parseEmailTemplate(template, model);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.contains("Bas"));
	}
}
