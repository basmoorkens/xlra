package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

public class EmailTemplateDAOImpl extends BaseDAO implements EmailTemplateDAO {

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		getEntityManager().merge(mailTemplate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MailTemplate> getAllTemplates() {
		Query query = getEntityManager().createNamedQuery(
				"MailTemplate.findAll");
		return (List<MailTemplate>) query.getResultList();
	}

	@Override
	public MailTemplate getMailTemplateForLanguage(Language language) {
		Query query = getEntityManager().createNamedQuery(
				"MailTemplate.findByLanguage");
		query.setParameter("language", language);
		return (MailTemplate) query.getSingleResult();
	}

}
