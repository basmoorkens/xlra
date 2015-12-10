package com.moorkensam.xlra.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.MailTemplatesForForLanguages;
import com.moorkensam.xlra.service.MailTemplateService;

@ManagedBean
@ViewScoped
public class MailTemplateController {

	@Inject
	private MailTemplateService mailTemplateService;

	private MailTemplatesForForLanguages mailTemplateDTO;

	@PostConstruct
	public void init() {
		refreshTemplates();
	}

	public void saveMailTemplates() {
		mailTemplateService.saveMailTemplateDTO(mailTemplateDTO);
		MessageUtil.addMessage("Update succesful",
				"Succesfully updated email templates");
	}

	private void refreshTemplates() {
		mailTemplateDTO = mailTemplateService.getAllTemplates();
	}

	public MailTemplateService getMailTemplateService() {
		return mailTemplateService;
	}

	public void setMailTemplateService(MailTemplateService mailTemplateService) {
		this.mailTemplateService = mailTemplateService;
	}

	public MailTemplatesForForLanguages getMailTemplateDTO() {
		return mailTemplateDTO;
	}

	public void setMailTemplateDTO(MailTemplatesForForLanguages mailTemplateDTO) {
		this.mailTemplateDTO = mailTemplateDTO;
	}
}
