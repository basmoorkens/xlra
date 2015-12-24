package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.dto.MailTemplatesForForLanguages;
import com.moorkensam.xlra.model.mail.MailTemplate;
import com.moorkensam.xlra.service.MailTemplateService;

import org.primefaces.context.RequestContext;

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

  private MailTemplate selectedTemplate;

  private String popupHeader;

  @PostConstruct
  public void init() {
    refreshTemplates();
  }

  /**
   * Cancel the edit of a template. hide the popup again.
   */
  public void cancelEdit() {
    selectedTemplate = null;
    hideEditDialog();
  }

  /**
   * Setup the page to edit a email template.
   * 
   * @param mailTemplate The template to edit.
   */
  public void setupPageForEdit(MailTemplate mailTemplate) {
    selectedTemplate = mailTemplate;
    popupHeader = "Edit " + mailTemplate.getLanguage().getDescription() + " template";
    showEditDialog();
  }

  private void hideEditDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editMailDialog').hide();");
  }

  private void showEditDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editMailDialog').show();");
  }

  /**
   * Saves the active template that was selected in the overviewgrid.
   */
  public void saveActiveTemplate() {
    mailTemplateService.updateEmailTemplate(selectedTemplate);
    MessageUtil.addMessage("Saved successfull", "The mail template was successfully saved");
    selectedTemplate = null;
    refreshTemplates();
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

  public String getPopupHeader() {
    return popupHeader;
  }

  public void setPopupHeader(String popupHeader) {
    this.popupHeader = popupHeader;
  }

  public MailTemplate getSelectedTemplate() {
    return selectedTemplate;
  }

  public void setSelectedTemplate(MailTemplate selectedTemplate) {
    this.selectedTemplate = selectedTemplate;
  }
}
