package com.moorkensam.xlra.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.service.MailTemplateService;

@Stateless
public class MailTemplateServiceImpl implements MailTemplateService {

	@Inject
	private EmailTemplateDAO mailTemplateDAO;

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		mailTemplateDAO.updateEmailTemplate(mailTemplate);
	}

	@Override
	public MailTemplateDTO getAllTemplates() {
		MailTemplateDTO dto = new MailTemplateDTO(
				mailTemplateDAO.getAllTemplates());
		return dto;
	}

	@Override
	public void saveMailTemplateDTO(MailTemplateDTO mailTemplateDTO) {
		for (MailTemplate mt : mailTemplateDTO.getMailTemplates()) {
			mailTemplateDAO.updateEmailTemplate(mt);
		}
	}

	@Override
	public MailTemplate getMailTemplateForLanguage(Language language) {
		return mailTemplateDAO.getMailTemplateForLanguage(language);
	}

}
