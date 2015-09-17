package com.moorkensam.xlra.model.configuration;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "ratelogrecord")
public class RateLogRecord extends LogRecord {

	private static final long serialVersionUID = -9102740273707056738L;

	private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += " rate";
		return result;
	}
}
