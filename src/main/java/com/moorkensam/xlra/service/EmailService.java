package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;

import javax.mail.MessagingException;

public interface EmailService {

  public void sendOfferteMail(QuotationResult result) throws MessagingException;

  public void sendResetPasswordEmail(User user) throws MessagingException;

  public void sendUserCreatedEmail(User user) throws MessagingException;
}
