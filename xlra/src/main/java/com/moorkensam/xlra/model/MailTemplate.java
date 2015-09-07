package com.moorkensam.xlra.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="mailTemplate")
@NamedQueries(
		@NamedQuery(name="MailTemplate.findAll",query="SELECT m FROM MailTemplate m"))
public class MailTemplate extends BaseEntity {

	private static final long serialVersionUID = -7712260056891764597L;

	private Language language;

	@ManyToOne
	@JoinColumn(name = "xlraConfigurationId")
	private XlraConfiguration xlraConfiguration;
	
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

	public XlraConfiguration getXlraConfiguration() {
		return xlraConfiguration;
	}

	public void setXlraConfiguration(XlraConfiguration xlraConfiguration) {
		this.xlraConfiguration = xlraConfiguration;
	}
	
}
