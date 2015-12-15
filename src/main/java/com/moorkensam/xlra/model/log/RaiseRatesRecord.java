package com.moorkensam.xlra.model.log;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateOperation;

@Entity
@Cacheable
@Table(name = "raiseratelogrecord")
@NamedQueries({
    @NamedQuery(name = "RaiseRatesRecord.findAll",
        query = "SELECT r FROM RaiseRatesRecord r WHERE r.deleted = false"),
    @NamedQuery(
        name = "RaiseRatesRecord.findLast",
        query = "SELECT r FROM RaiseRatesRecord r WHERE r.operation = com.moorkensam.xlra.model.rate.RateOperation.RAISE AND r.id = (SELECT MAX(r2.id) FROM RaiseRatesRecord r2)"),
    @NamedQuery(
        name = "RaiseRatesRecord.findByDates",
        query = "SELECT r FROM RaiseRatesRecord r WHERE r.logDate > :startDate AND r.logDate < :endDate")})
public class RaiseRatesRecord extends LogRecord {

  private static final long serialVersionUID = 7232906192502786501L;

  @ManyToMany
  @JoinTable(name = "raiseRatesRateFileRecords", joinColumns = @JoinColumn(
      name = "raiseRateRecordId"), inverseJoinColumns = @JoinColumn(name = "rateFileId"))
  private List<RateFile> rateFiles;

  @Enumerated(EnumType.STRING)
  private RateOperation operation;

  private boolean undone;

  private double percentage;

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public List<RateFile> getRateFiles() {
    return rateFiles;
  }

  public void setRateFiles(List<RateFile> rateFiles) {
    this.rateFiles = rateFiles;
  }

  public RateOperation getOperation() {
    return operation;
  }

  public void setOperation(RateOperation operation) {
    this.operation = operation;
  }

  public boolean isUndone() {
    return undone;
  }

  public void setUndone(boolean undone) {
    this.undone = undone;
  }

}
