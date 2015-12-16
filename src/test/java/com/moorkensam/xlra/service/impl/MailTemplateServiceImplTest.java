package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.EmailTemplateDao;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.EmailService;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;

public class MailTemplateServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private MailTemplateServiceImpl mailTemplateService;

  @Mock
  private TemplateParseService templateParseService;

  private PriceCalculation priceDto;

  @Mock
  private EmailService mailService;

  @Mock
  private EmailTemplateDao emailTemplateDao;

  private MailTemplate template;

  private QuotationQuery query;

  /**
   * init the test.
   */
  @Before
  public void init() {
    query = new QuotationQuery();
    query.setCustomer(new Customer());
    query.getCustomer().setEmail("test@test.com");
    query.setCountry(new Country());
    priceDto = new PriceCalculation();
    priceDto.setAppliedOperations(new ArrayList<TranslationKey>());
    template = new MailTemplate();
    template.setTemplate("test template + ${detailCalculation}");
    template.setSubject("SUBJECT");
    mailTemplateService = new MailTemplateServiceImpl();
    mailTemplateService.setMailTemplateDao(emailTemplateDao);
    mailTemplateService.setTemplateParseService(templateParseService);
  }

  @Test
  public void testInitOfferteEmail() throws TemplatingException, RateFileException {
    QuotationResult offerte = new QuotationResult();
    offerte.setSelectableOptions(new ArrayList<OfferteOptionDto>());
    offerte.setQuery(query);
    query.setResultLanguage(Language.NL);
    EasyMock.expect(emailTemplateDao.getMailTemplateForLanguage(Language.NL)).andReturn(template);
    EasyMock
        .expect(templateParseService.parseOfferteEmailTemplate(template.getTemplate(), offerte))
        .andReturn("test template + filled in");
    EasyMockUnitils.replay();

    QuotationResult result = new QuotationResult();
    result.setQuery(query);
    result.setSelectableOptions(new ArrayList<OfferteOptionDto>());
    result.setOfferteUniqueIdentifier("REF-001");
    result.setCalculation(priceDto);

    EmailResult dto = mailTemplateService.initializeOfferteEmail(result);
    Assert.assertNotNull(dto);
    Assert.assertEquals("SUBJECT", dto.getSubject());
    Assert.assertEquals("test@test.com", dto.getToAddress());
  }
}
