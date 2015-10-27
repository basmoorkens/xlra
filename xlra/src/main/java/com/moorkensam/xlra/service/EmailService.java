package com.moorkensam.xlra.service;

import javax.mail.MessagingException;

import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.model.security.User;

public interface EmailService {

	public void sendOfferteMail(QuotationResult result)
			throws MessagingException;

	public void sendResetPasswordEmail(User user) throws MessagingException;
}
