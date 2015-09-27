package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.MailTemplateDTO;
import com.moorkensam.xlra.model.configuration.MailTemplate;
import com.moorkensam.xlra.service.MailTemplateService;

@ManagedBean
@ViewScoped
public class MailTemplateController {

	@Inject
	private MailTemplateService mailTemplateService;

	private MailTemplateDTO mailTemplateDTO;

	@PostConstruct
	public void init() {
		refreshTemplates();
	}

	public void saveMailTemplates() {
		
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

	public MailTemplateDTO getMailTemplateDTO() {
		return mailTemplateDTO;
	}

	public void setMailTemplateDTO(MailTemplateDTO mailTemplateDTO) {
		this.mailTemplateDTO = mailTemplateDTO;
	}
}
