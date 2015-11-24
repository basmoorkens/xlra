package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

public interface EmailTemplateDAO {

	void updateEmailTemplate(MailTemplate mailTemplate);

	List<MailTemplate> getAllTemplates();

	MailTemplate getMailTemplateForLanguage(Language language);
}
