package com.moorkensam.xlra.model.log;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "ratelogrecord")
@NamedQueries({ @NamedQuery(name = "RateLogRecord.findByDates", query = "SELECT r FROM RateLogRecord r"
		+ " where r.logDate > :startDate AND r.logDate < :endDate") })
public class RateLogRecord extends LogRecord {

	private static final long serialVersionUID = -9102740273707056738L;

	private BigDecimal rate;

	private BigDecimal newRate;

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += " oldrate " + rate;
		result += " newrate " + newRate;
		return result;
	}

	public BigDecimal getNewRate() {
		return newRate;
	}

	public void setNewRate(BigDecimal newRate) {
		this.newRate = newRate;
	}
}
