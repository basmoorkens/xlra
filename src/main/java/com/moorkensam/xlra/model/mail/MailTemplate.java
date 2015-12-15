package com.moorkensam.xlra.model.mail;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.Language;

@Entity
@Table(name = "mailtemplate")
@NamedQueries({
    @NamedQuery(name = "MailTemplate.findAll", query = "SELECT m FROM MailTemplate m"),
    @NamedQuery(name = "MailTemplate.findByLanguage",
        query = "SELECT m FROM MailTemplate m where m.language = :language")})
public class MailTemplate extends BaseEntity {

  private static final long serialVersionUID = -7712260056891764597L;

  @Enumerated(EnumType.STRING)
  private Language language;

  @ManyToOne
  @JoinColumn(name = "xlraConfigurationId")
  private Configuration xlraConfiguration;

  private String subject;

  @Lob
  private String template;

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Configuration getXlraConfiguration() {
    return xlraConfiguration;
  }

  public void setXlraConfiguration(Configuration xlraConfiguration) {
    this.xlraConfiguration = xlraConfiguration;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

}
