package com.moorkensam.xlra.service.impl;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.EmailHistoryDao;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.mail.EmailHistoryRecord;
import com.moorkensam.xlra.model.mail.EmailSentStatus;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
import com.moorkensam.xlra.service.util.EmailAttachmentHelper;
import com.moorkensam.xlra.service.util.TransportDelegate;

/**
 * This service can be used to send emails from the application.
 * 
 * @author bas
 *
 */
@Stateless
public class EmailServiceImpl implements EmailService {

  private final static Logger logger = LogManager.getLogger();

  @Resource(name = "java:/mail/xlra")
  private Session mailSession;

  @Inject
  private EmailHistoryDao emailHistoryDao;

  @Inject
  private UserService userService;

  private ConfigurationLoader configLoader;

  private FileService fileService;

  private TemplateParseService templateParseService;

  private TransportDelegate transportDelegate;

  private EmailAttachmentHelper helper;

  /**
   * Init the service.
   */
  @PostConstruct
  public void init() {
    setConfigLoader(ConfigurationLoader.getInstance());
    setTemplateParseService(TemplateParseServiceImpl.getInstance());
    setTransportDelegate(TransportDelegate.getInstance());
    helper = new EmailAttachmentHelper();
    fileService = new FileServiceImpl();
  }

  @Override
  public void sendOfferteMail(QuotationResult result) throws MessagingException {
    logger.info("Sending offerte mail to " + result.getEmailResult().getToAddress()
        + " with subject " + result.getEmailResult().getSubject() + " for content look up result "
        + result.getId());
    EmailSentStatus status = EmailSentStatus.SENT;
    try {
      String fromAddress = configLoader.getProperty(ConfigurationLoader.MAIL_SENDER);
      Message message = createOfferteMail(result, fromAddress);

      transportDelegate.send(message);
      if (logger.isDebugEnabled()) {
        logger.debug("Email succesfully sent");
      }
      result.getEmailResult().setSend(true);
    } catch (MessagingException e) {
      result.getEmailResult().setSend(false);
      logger.error("Error sending email: " + e);
      status = EmailSentStatus.NOT_SENT;
      throw new MessagingException("Error sending offerte email");
    } finally {
      EmailHistoryRecord historyRecord = new EmailHistoryRecord();
      historyRecord.setDateTime(new Date());
      historyRecord.setOfferte(result);
      historyRecord.setStatus(status);
      historyRecord.setUsername(userService.getCurrentUsername());
      emailHistoryDao.createEmailHistoryRecord(historyRecord);
    }

  }

  protected Message createOfferteMail(QuotationResult result, String fromAddress)
      throws MessagingException, AddressException {
    Message message = new MimeMessage(getMailSession());
    message.setFrom(new InternetAddress(fromAddress));
    message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(result.getEmailResult().getToAddress()));
    message.setSubject(result.getEmailResult().getSubject());

    MimeBodyPart messageBody = new MimeBodyPart();
    messageBody.setContent(result.getEmailResult().getEmail(), "text/html; charset=utf-8");
    MimeBodyPart pdfAttachment = getHelper().generatedPdfAttachment(result);
    Multipart multiPart = new MimeMultipart();
    multiPart.addBodyPart(messageBody);
    multiPart.addBodyPart(pdfAttachment);

    message.setContent(multiPart);
    return message;
  }

  @Override
  public void sendResetPasswordEmail(User user) throws MessagingException {
    logger.info("Sending user reset password to " + user.getEmail());
    try {
      String fromAddress = configLoader.getProperty(ConfigurationLoader.MAIL_SENDER);
      Message message = new MimeMessage(getMailSession());
      message.setFrom(new InternetAddress(fromAddress));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
      message.setSubject("Extra logistics Rates application - reset password");
      String content = templateParseService.parseUserResetPasswordEmail(user);
      message.setContent(content, "text/html; charset=utf-8");

      transportDelegate.send(message);
      if (logger.isDebugEnabled()) {
        logger.debug("Email succesfully sent");
      }
    } catch (MessagingException e) {
      logger.error("Error sending email: " + e);
      throw new MessagingException("Error sending password reset email");
    } catch (TemplatingException exc) {
      logger.error("Error parsing mail template: " + exc);
      throw new MessagingException("Error parsing the reset password email");
    }
  }

  @Override
  public void sendUserCreatedEmail(User user) throws MessagingException {
    logger.info("Sending user reset password to " + user.getEmail());
    try {
      String fromAddress = configLoader.getProperty(ConfigurationLoader.MAIL_SENDER);
      Message message = new MimeMessage(getMailSession());
      message.setFrom(new InternetAddress(fromAddress));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
      String filledInTemplate = getTemplateParseService().parseUserCreatedTemplate(user);
      message.setSubject("Extra logistics Rates application - account created");
      message.setContent(filledInTemplate, "text/html; charset=utf-8");

      transportDelegate.send(message);
      if (logger.isDebugEnabled()) {
        logger.debug("Email succesfully sent");
      }
    } catch (MessagingException e) {
      logger.error("Error sending email: " + e);
      throw new MessagingException("Error sending password reset email");
    } catch (TemplatingException e) {
      logger.error(e);
      throw new MessagingException("Could not parse email template");
    }
  }

  public ConfigurationLoader getConfigLoader() {
    return configLoader;
  }

  public void setConfigLoader(ConfigurationLoader configLoader) {
    this.configLoader = configLoader;
  }

  public Session getMailSession() {
    return mailSession;
  }

  public void setMailSession(Session mailSession) {
    this.mailSession = mailSession;
  }

  public TransportDelegate getTransportDelegate() {
    return transportDelegate;
  }

  public void setTransportDelegate(TransportDelegate transportDelegate) {
    this.transportDelegate = transportDelegate;
  }

  public TemplateParseService getTemplateParseService() {
    return templateParseService;
  }

  public void setTemplateParseService(TemplateParseService templateEngine) {
    this.templateParseService = templateEngine;
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public EmailAttachmentHelper getHelper() {
    return helper;
  }

  public void setHelper(EmailAttachmentHelper helper) {
    this.helper = helper;
  }

  public EmailHistoryDao getEmailHistoryDao() {
    return emailHistoryDao;
  }

  public void setEmailHistoryDao(EmailHistoryDao emailHistoryDao) {
    this.emailHistoryDao = emailHistoryDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
