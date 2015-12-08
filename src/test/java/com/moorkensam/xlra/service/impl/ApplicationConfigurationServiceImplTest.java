package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.mail.MailTemplate;

public class ApplicationConfigurationServiceImplTest extends UnitilsJUnit4 {

	private ApplicationConfigurationServiceImpl service;

	@Mock
	private ConfigurationDao configurationDao;

	@Mock
	private EmailTemplateDAO emailTemplateDao;

	@Before
	public void init() {
		service = new ApplicationConfigurationServiceImpl();
		service.setXlraConfigurationDAO(configurationDao);
		service.setEmailTemplateDAO(emailTemplateDao);
	}

	@Test
	public void testGetAllMailTemplates() {
		EasyMock.expect(emailTemplateDao.getAllTemplates()).andReturn(
				new ArrayList<MailTemplate>());
		EasyMockUnitils.replay();
		List<MailTemplate> templates = service.getAllEmailTemplates();
		Assert.assertNotNull(templates);
	}

	@Test
	public void testGetApplicationConfiguration() {
		EasyMock.expect(configurationDao.getXlraConfiguration()).andReturn(
				new Configuration());
		EasyMockUnitils.replay();

		Configuration config = service.getConfiguration();
		Assert.assertNotNull(config);
	}

	@Test
	public void testUpdateConfiguration() {
		Configuration config = new Configuration();
		configurationDao.updateXlraConfiguration(config);
		EasyMock.expectLastCall();
		EasyMockUnitils.replay();

		service.updateXlraConfiguration(config);
	}

	@Test
	public void testUpdateEmailTemplate() {
		MailTemplate template = new MailTemplate();

		emailTemplateDao.updateEmailTemplate(template);
		EasyMock.expectLastCall();
		EasyMockUnitils.replay();
		service.updateEmailTemplate(template);
	}
}
