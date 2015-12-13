package com.moorkensam.xlra.model.configuration;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.mail.MailTemplate;

/**
 * Configuration object for the application. Contains all necessairy parameters to bootstrap the
 * application correctly.
 * 
 * @author bas
 *
 */
@Entity
@Cacheable
@Table(name = "configuration")
@NamedQueries(@NamedQuery(name = "Configuration.findMainConfig",
    query = "SELECT x FROM Configuration x where x.configurationName = :configName"))
public class Configuration extends BaseEntity {

  public Configuration() {
    super();
  }

  public final static String MAIN_CONFIG_NAME = "mainconfig";

  private static final long serialVersionUID = -7748281252917328759L;

  private String configurationName;

  private BigDecimal currentDieselPrice;

  private BigDecimal currentChfValue;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "xlraConfiguration")
  private List<MailTemplate> mailTemplates;

  public BigDecimal getCurrentChfValue() {
    return currentChfValue;
  }

  public void setCurrentChfValue(BigDecimal currentChfValue) {
    this.currentChfValue = currentChfValue;
  }

  public BigDecimal getCurrentDieselPrice() {
    return currentDieselPrice;
  }

  public void setCurrentDieselPrice(BigDecimal currentDieselPrice) {
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
