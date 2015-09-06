package com.moorkensam.xlra.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="dieselrate")
@NamedQueries(
		@NamedQuery(name = "DieselRate.findAll", query="SELECT d FROM DieselRate d"))
public class DieselRate extends BaseEntity {

	private static final long serialVersionUID = 1424312481303614643L;
	
	public DieselRate()  {
		dieselInterval = new Interval();
	}
	
	@Embedded
	private Interval dieselInterval;

	private double multiplier;
	
	public Interval getDieselInterval() {
		return dieselInterval;
	}

	public void setDieselInterval(Interval dieselInterval) {
		this.dieselInterval = dieselInterval;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}
