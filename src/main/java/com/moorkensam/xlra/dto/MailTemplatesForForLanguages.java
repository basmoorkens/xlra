package com.moorkensam.xlra.dto;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

import java.util.List;

public class MailTemplatesForForLanguages {

  private List<MailTemplate> mailTemplates;

  public MailTemplatesForForLanguages(List<MailTemplate> templates) {
    this.mailTemplates = templates;
  }

  public MailTemplate getNlTemplate() {
    return getTemplateForLang(Language.NL);
  }

  public MailTemplate getEnTemplate() {
    return getTemplateForLang(Language.EN);
  }

  public MailTemplate getDeTemplate() {
    return getTemplateForLang(Language.DE);
  }

  public MailTemplate getFrTemplate() {
    return getTemplateForLang(Language.FR);
  }

  protected MailTemplate getTemplateForLang(Language lang) {
    if (mailTemplates == null) {
      return null;
    }
    if (lang == null) {
      return null;
    }
    for (MailTemplate template : mailTemplates) {
      if (lang.equals(template.getLanguage())) {
        return template;
      }
    }
    return null;
  }

  public List<MailTemplate> getMailTemplates() {
    return mailTemplates;
  }

  public void setMailTemplates(List<MailTemplate> mailTemplates) {
    this.mailTemplates = mailTemplates;
  }

}
