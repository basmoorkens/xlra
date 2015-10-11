package com.moorkensam.xlra.service;

import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.MailTemplate;

public interface MailTemplateService {

	void updateEmailTemplate(MailTemplate mailTemplate);

	MailTemplateDTO getAllTemplates();

	MailTemplate getMailTemplateForLanguage(Language language);

	void saveMailTemplateDTO(MailTemplateDTO mailTemplateDTO);

}
