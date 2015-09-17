package com.moorkensam.xlra.model.configuration;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Cacheable
@Table(name="configuration")
@NamedQueries(
		@NamedQuery(name="Configuration.findMainConfig", query="SELECT x FROM Configuration x where x.configurationName = :configName"))
public class Configuration extends BaseEntity {

	public final static String MAIN_CONFIG_NAME = "mainconfig";
	
	private static final long serialVersionUID = -7748281252917328759L;

	private String configurationName;
	
	private double currentDieselPrice; 
	
	private double currentChfValue;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="xlraConfiguration")
	private List<MailTemplate> mailTemplates;
	
	public double getCurrentChfValue() {
		return currentChfValue;
	}

	public void setCurrentChfValue(double currentChfValue) {
		this.currentChfValue = currentChfValue;
	}

	public double getCurrentDieselPrice() {
		return currentDieselPrice;
	}

	public void setCurrentDieselPrice(double currentDieselPrice) {
		this.currentDieselPrice = currentDieselPrice;
	}

	public List<MailTemplate> getMailTemplates() {
		return mailTemplates;
	}

	public void setMailTemplates(List<MailTemplate> mailTemplates) {
		this.mailTemplates = mailTemplates;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
	
}
