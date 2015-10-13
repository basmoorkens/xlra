package com.moorkensam.xlra.service;

import javax.mail.MessagingException;

import com.moorkensam.xlra.dto.OfferteMailDTO;

public interface EmailService {

	public void sendOfferteMail(OfferteMailDTO dto) throws MessagingException;

}
