package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.EmailTemplateDAO;
import com.moorkensam.xlra.model.MailTemplate;

public class EmailTemplateDAOImpl extends BaseDAO implements EmailTemplateDAO {

	@Override
	public void updateEmailTemplate(MailTemplate mailTemplate) {
		getEntityManager().merge(mailTemplate);
	}

	@Override
	public List<MailTemplate> getAllTemplates() {
		Query query = getEntityManager().createNamedQuery("MailTemplate.findAll");
		return (List<MailTemplate>) query.getResultList();
	}

}
