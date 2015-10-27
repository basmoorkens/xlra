package com.moorkensam.xlra.service.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

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

	@Override
	public void sendOfferteMail(QuotationResult result)
			throws MessagingException {
		logger.info("Sending offerte mail to "
				+ result.getEmailResult().getToAddress() + " with subject "
				+ result.getEmailResult().getSubject()
				+ " for content look up result " + result.getId());
		try {
			ConfigurationLoader configLoader = ConfigurationLoader
					.getInstance();
			String fromAddress = configLoader
					.getProperty(ConfigurationLoader.MAIL_SENDER);
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(result.getEmailResult().getToAddress()));
			message.setSubject(result.getEmailResult().getSubject());
			message.setContent(result.getEmailResult().getEmail(),
					"text/html; charset=utf-8");

			Transport.send(message);
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

	@Override
	public void sendResetPasswordEmail(User user) throws MessagingException {
		logger.info("Sending user reset password to " + user.getEmail());
		try {
			ConfigurationLoader configLoader = ConfigurationLoader
					.getInstance();
			String fromAddress = configLoader
					.getProperty(ConfigurationLoader.MAIL_SENDER);
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("Extra logistics Rates application - reset password");
			message.setContent("TEST", "text/html; charset=utf-8");

			Transport.send(message);
			if (logger.isDebugEnabled()) {
				logger.debug("Email succesfully sent");
			}
		} catch (MessagingException e) {
			logger.error("Error sending email: " + e);
			throw new MessagingException("Error sending password reset email");
		}
	}

}
