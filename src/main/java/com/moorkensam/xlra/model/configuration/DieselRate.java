package com.moorkensam.xlra.model.configuration;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "dieselrate")
@NamedQueries(@NamedQuery(name = "DieselRate.findAll",
    query = "SELECT d FROM DieselRate d where d.deleted = false"))
public class DieselRate extends AbstractRate {
  private static final long serialVersionUID = 1424312481303614643L;

  @Override
  public String toString() {
    return "Id: " + getId() + " Interval: " + getInterval() + " Value: " + getSurchargePercentage();
  }
}
