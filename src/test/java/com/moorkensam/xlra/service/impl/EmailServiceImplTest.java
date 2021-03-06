package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.EmailHistoryDao;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.generator.UserGenerator;
import com.moorkensam.xlra.model.mail.EmailHistoryRecord;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
import com.moorkensam.xlra.service.util.EmailAttachmentHelper;
import com.moorkensam.xlra.service.util.TransportDelegate;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public class EmailServiceImplTest extends UnitilsJUnit4 {

  private EmailServiceImpl emailServiceImpl;

  @Mock
  private TemplateParseService templateParseService;

  @Mock
  private TransportDelegate transportDelegate;

  @Mock
  private UserSessionService userSessionService;

  @Mock
  private EmailAttachmentHelper helper;

  @Mock
  private EmailHistoryDao emailHistoryDao;

  private ConfigurationLoader configLoader;

  /**
   * init the test.
   */
  @Before
  public void init() {
    emailServiceImpl = new EmailServiceImpl();
    emailServiceImpl.setTemplateParseService(templateParseService);
    emailServiceImpl.setTransportDelegate(transportDelegate);
    configLoader = ConfigurationLoader.getInstance();
    emailServiceImpl.setConfigLoader(configLoader);
    emailServiceImpl.setHelper(helper);
    emailServiceImpl.setUserSessionService(userSessionService);
    emailServiceImpl.setEmailHistoryDao(emailHistoryDao);
  }

  @Test
  public void testSendUserCreatedEmail() throws MessagingException, TemplatingException {
    User user = new User();
    String parsedTemplate = "";
    user.setEmail("test@test.com");
    EasyMock.expect(templateParseService.parseUserCreatedTemplate(user)).andReturn(parsedTemplate);
    transportDelegate.send(EasyMock.isA(MimeMessage.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();

    emailServiceImpl.sendUserCreatedEmail(user);
  }

  @Test(expected = MessagingException.class)
  public void testSendUserCreatedMailParseException() throws TemplatingException,
      MessagingException {
    User user = new User();
    user.setEmail("test@test.com");
    EasyMock.expect(templateParseService.parseUserCreatedTemplate(user)).andThrow(
        new TemplatingException("", null));
    EasyMockUnitils.replay();

    emailServiceImpl.sendUserCreatedEmail(user);
  }

  @Test
  public void testsendOfferteMail() throws MessagingException {
    User user = UserGenerator.getStandardUser();
    EmailResult emailResult = new EmailResult();
    emailResult.setEmail("test email");
    emailResult.setSubject("test");
    emailResult.addRecipient("test@test.com");
    QuotationResult offerte = new QuotationResult();
    offerte.setOfferteUniqueIdentifier("uq123");
    offerte.setEmailResult(emailResult);
    EasyMock.expect(helper.generatedPdfAttachment(offerte)).andReturn(new MimeBodyPart());
    transportDelegate.send(EasyMock.isA(MimeMessage.class));
    EasyMock.expectLastCall();
    EasyMock.expect(userSessionService.getLoggedInUser()).andReturn(user);
    emailHistoryDao.createEmailHistoryRecord(EasyMock.isA(EmailHistoryRecord.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();

    emailServiceImpl.sendOfferteMail(offerte);
  }

  @Test
  public void testsendResetPasswordEmail() throws MessagingException, TemplatingException {
    User user = new User();
    user.setEmail("test@test.com");
    String template = "reset template";
    EasyMock.expect(templateParseService.parseUserResetPasswordEmail(user)).andReturn(template);
    transportDelegate.send(EasyMock.isA(MimeMessage.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();
    emailServiceImpl.sendResetPasswordEmail(user);
  }
}
