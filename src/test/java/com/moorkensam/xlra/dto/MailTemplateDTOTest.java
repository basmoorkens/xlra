package com.moorkensam.xlra.dto;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

public class MailTemplateDTOTest extends UnitilsJUnit4 {

	@TestedObject
	private MailTemplatesForForLanguages dto;

	private MailTemplate template, template2;

	@Before
	public void init() {
		template = new MailTemplate();
		template.setLanguage(Language.NL);
		template.setTemplate("templateNL");
		template2 = new MailTemplate();
		template2.setLanguage(Language.EN);
		template2.setTemplate("templateEN");
	}

	@Test
	public void testGetTemplateForLangTemplatesNull() {
		dto = new MailTemplatesForForLanguages(null);
		Assert.assertNull(dto.getTemplateForLang(Language.NL));
	}

	@Test
	public void testGetTemplateForLangNull() {
		dto = new MailTemplatesForForLanguages(Arrays.asList(template, template2));
		Assert.assertNull(dto.getTemplateForLang(null));
	}

	@Test
	public void testGetValidTemplate() {
		dto = new MailTemplatesForForLanguages(Arrays.asList(template, template2));
		Assert.assertEquals("templateNL", dto.getNlTemplate().getTemplate());
		Assert.assertEquals("templateEN", dto.getEnTemplate().getTemplate());
	}

}
