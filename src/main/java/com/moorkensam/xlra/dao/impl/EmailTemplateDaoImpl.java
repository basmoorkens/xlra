package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.EmailTemplateDao;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.mail.MailTemplate;

public class EmailTemplateDaoImpl extends BaseDao implements EmailTemplateDao {

  @Override
  public void updateEmailTemplate(MailTemplate mailTemplate) {
    getEntityManager().merge(mailTemplate);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<MailTemplate> getAllTemplates() {
    Query query = getEntityManager().createNamedQuery("MailTemplate.findAll");
    return (List<MailTemplate>) query.getResultList();
  }

  @Override
  public MailTemplate getMailTemplateForLanguage(Language language) {
    Query query = getEntityManager().createNamedQuery("MailTemplate.findByLanguage");
    query.setParameter("language", language);
    return (MailTemplate) query.getSingleResult();
  }

}
