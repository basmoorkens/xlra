package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.MailTemplate;

public interface EmailTemplateDAO {

	void updateEmailTemplate(MailTemplate mailTemplate);
	
	List<MailTemplate> getAllTemplates();
	
}
