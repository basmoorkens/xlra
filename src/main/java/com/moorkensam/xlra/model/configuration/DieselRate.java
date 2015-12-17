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
    query = "SELECT d FROM DieselRate d where d.deleted = false ORDER BY d.interval.start ASC"))
public class DieselRate extends AbstractRate {
  private static final long serialVersionUID = 1424312481303614643L;

  public DieselRate() {}

  /**
   * Constructor that constructs a new rate with a specified start and end.
   * 
   * @param start Start of the interval.
   * @param end End of the interval.
   */
  public DieselRate(double start, double end) {
    interval = new Interval();
    interval.setStart(start);
    interval.setEnd(end);
  }

  @Override
  public String toString() {
    return "Id: " + getId() + " Interval: " + getInterval() + " Value: " + getSurchargePercentage();
  }
}
