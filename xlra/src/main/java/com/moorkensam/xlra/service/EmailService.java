package com.moorkensam.xlra.service;

import javax.mail.MessagingException;

import com.moorkensam.xlra.model.rate.QuotationResult;

public interface EmailService {

	public void sendOfferteMail(QuotationResult result) throws MessagingException;

}
