package com.moorkensam.xlra.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
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

	private ConfigurationLoader configLoader;

	@Resource(name = "java:/mail/xlra")
	private Session mailSession;

	private TemplateParseService templateEngine;

	private TransportDelegate transportDelegate;

	@PostConstruct
	public void init() {
		setConfigLoader(ConfigurationLoader.getInstance());
		setTemplateEngine(TemplateParseService.getInstance());
		setTransportDelegate(TransportDelegate.getInstance());
	}

	@Override
	public void sendOfferteMail(QuotationResult result)
			throws MessagingException {
		logger.info("Sending offerte mail to "
				+ result.getEmailResult().getToAddress() + " with subject "
				+ result.getEmailResult().getSubject()
				+ " for content look up result " + result.getId());
		try {
			String fromAddress = configLoader
					.getProperty(ConfigurationLoader.MAIL_SENDER);
			Message message = createOfferteMail(result, fromAddress);

			transportDelegate.send(message);
			if (logger.isDebugEnabled()) {
				logger.debug("Email succesfully sent");
			}
			result.getEmailResult().setSend(true);
		} catch (MessagingException e) {
			result.getEmailResult().setSend(false);
			logger.error("Error sending email: " + e);
			throw new MessagingException("Error sending offerte email");
		}
	}

	protected Message createOfferteMail(QuotationResult result,
			String fromAddress) throws MessagingException, AddressException {
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(fromAddress));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(result.getEmailResult().getToAddress()));
		message.setSubject(result.getEmailResult().getSubject());
		message.setContent(result.getEmailResult().getEmail(),
				"text/html; charset=utf-8");
		return message;
	}

	@Override
	public void sendResetPasswordEmail(User user) throws MessagingException {
		logger.info("Sending user reset password to " + user.getEmail());
		try {
			String fromAddress = configLoader
					.getProperty(ConfigurationLoader.MAIL_SENDER);
			Message message = new MimeMessage(getMailSession());
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("Extra logistics Rates application - reset password");
			message.setContent("TEST", "text/html; charset=utf-8");

			transportDelegate.send(message);
			if (logger.isDebugEnabled()) {
				logger.debug("Email succesfully sent");
			}
		} catch (MessagingException e) {
			logger.error("Error sending email: " + e);
			throw new MessagingException("Error sending password reset email");
		}
	}

	@Override
	public void sendUserCreatedEmail(User user) throws MessagingException {
		logger.info("Sending user reset password to " + user.getEmail());
		try {
			String fromAddress = configLoader
					.getProperty(ConfigurationLoader.MAIL_SENDER);
			Message message = new MimeMessage(getMailSession());
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			String filledInTemplate = getTemplateEngine()
					.parseUserCreatedTemplate(user);
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

	public TemplateParseService getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateParseService templateEngine) {
		this.templateEngine = templateEngine;
	}

}
