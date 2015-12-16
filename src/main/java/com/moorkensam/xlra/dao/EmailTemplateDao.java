package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

import java.util.List;

public interface EmailTemplateDao {

  void updateEmailTemplate(MailTemplate mailTemplate);

  List<MailTemplate> getAllTemplates();

  MailTemplate getMailTemplateForLanguage(Language language);
}
