package com.moorkensam.xlra.model.rate;

import com.moorkensam.xlra.model.BaseEntity;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "rateline")
@NamedQueries(@NamedQuery(name = "RateLine.findAllForRateFile",
    query = "SELECT r FROM RateLine r WHERE r.rateFile.id = :ratefileid"))
public class RateLine extends BaseEntity implements Comparable<RateLine> {

  private static final long serialVersionUID = 1L;

  private double measurement;

  @ManyToOne
  @JoinColumn(name = "zoneId")
  private Zone zone;

  private BigDecimal value;

  @ManyToOne
  @JoinColumn(name = "rateFileId")
  private RateFile rateFile;

  public RateFile getRateFile() {
    return rateFile;
  }

  public void setRateFile(RateFile rateFile) {
    this.rateFile = rateFile;
  }

  public double getMeasurement() {
    return measurement;
  }

  public void setMeasurement(double measurement) {
    this.measurement = measurement;
  }

  @Override
  public int compareTo(RateLine rateline) {
    if (rateline.measurement == this.measurement) {
      return 0;
    }
    if (rateline.measurement > this.measurement) {
      return 1;
    }
    return -1;
  }

  @Override
  public String toString() {
    return this.getId() + " - " + getZone() + " - " + getMeasurement() + " - " + getValue();
  }

  /**
   * Deep copys the object.
   * 
   * @return The deep copy.
   */
  public RateLine deepCopy() {
    RateLine rl = new RateLine();
    rl.setMeasurement(getMeasurement());
    rl.setZone(getZone().deepCopy());
    rl.setValue(getValue());
    return rl;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Zone getZone() {
    return zone;
  }

  public void setZone(Zone zone) {
    this.zone = zone;
  }

}
