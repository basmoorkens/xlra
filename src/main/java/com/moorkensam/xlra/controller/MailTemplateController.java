package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.MailTemplatesForForLanguages;
import com.moorkensam.xlra.service.MailTemplateService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class MailTemplateController {

  @Inject
  private MailTemplateService mailTemplateService;

  private MailTemplatesForForLanguages mailTemplateDto;

  @PostConstruct
  public void init() {
    refreshTemplates();
  }

  public void saveMailTemplates() {
    mailTemplateService.saveMailTemplateDto(mailTemplateDto);
    MessageUtil.addMessage("Update succesful", "Succesfully updated email templates");
  }

  private void refreshTemplates() {
    mailTemplateDto = mailTemplateService.getAllTemplates();
  }

  public MailTemplateService getMailTemplateService() {
    return mailTemplateService;
  }

  public void setMailTemplateService(MailTemplateService mailTemplateService) {
    this.mailTemplateService = mailTemplateService;
  }

  public MailTemplatesForForLanguages getMailTemplateDto() {
    return mailTemplateDto;
  }

  public void setMailTemplateDto(MailTemplatesForForLanguages mailTemplateDto) {
    this.mailTemplateDto = mailTemplateDto;
  }
}
