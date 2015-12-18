package com.moorkensam.xlra.model.configuration;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Cacheable
@Table(name = "currencyrate")
@NamedQueries({
    @NamedQuery(name = "CurrencyRate.findAll",
        query = "SELECT c FROM CurrencyRate c where c.deleted = false "
            + "ORDER BY c.interval.start ASC"),
    @NamedQuery(name = "CurrencyRate.findAllChf",
        query = "SELECT c FROM CurrencyRate c WHERE c.deleted = false "
            + "and c.currencyType = com.moorkensam.xlra.model.configuration.XlraCurrency.CHF "
            + "ORDER BY c.interval.start ASC")})
public class CurrencyRate extends AbstractRate {

  private static final long serialVersionUID = -7687370103653996637L;

  @Enumerated(EnumType.STRING)
  @NotNull
  private XlraCurrency currencyType;

  public XlraCurrency getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(XlraCurrency currencyType) {
    this.currencyType = currencyType;
  }

  @Override
  public int hashCode() {
    return currencyType.hashCode() + getInterval().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CurrencyRate) {
      CurrencyRate cr = (CurrencyRate) obj;
      if (this.getCurrencyType().equals(cr.getCurrencyType())
          && this.getSurchargePercentage() == cr.getSurchargePercentage()
          && this.getInterval().equals(cr.getInterval())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "Id: " + id + " interval: " + getInterval() + " Value: " + getSurchargePercentage();
  }

}
