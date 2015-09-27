package com.moorkensam.xlra.dto;

import java.util.List;

import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.MailTemplate;

public class MailTemplateDTO {

	private List<MailTemplate> mailTemplates;

	public MailTemplateDTO(List<MailTemplate> templates) {
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

	private MailTemplate getTemplateForLang(Language lang) {
		for (MailTemplate template : mailTemplates) {
			if (template.getLanguage().equals(lang)) {
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
