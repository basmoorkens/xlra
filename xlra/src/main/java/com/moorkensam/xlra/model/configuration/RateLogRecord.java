package com.moorkensam.xlra.model.configuration;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ratelogrecord")
public class RateLogRecord extends LogRecord {

	private static final long serialVersionUID = -9102740273707056738L;

	private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	} 
	
}
