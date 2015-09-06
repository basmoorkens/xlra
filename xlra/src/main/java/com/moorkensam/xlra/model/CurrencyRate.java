package com.moorkensam.xlra.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="currencyrate")
@NamedQueries(
		@NamedQuery(name="CurrencyRate.findAll", query="SELECT c FROM CurrencyRate c"))
public class CurrencyRate extends BaseEntity {

	private static final long serialVersionUID = -7687370103653996637L;

	private Interval currencyInterval;

	private double multiplier;
	
	public Interval getCurrencyInterval() {
		return currencyInterval;
	}

	public void setCurrencyInterval(Interval currencyInterval) {
		this.currencyInterval = currencyInterval;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}
