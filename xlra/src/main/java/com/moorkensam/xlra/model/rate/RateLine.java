package com.moorkensam.xlra.model.rate;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Cacheable
@Table(name = "rateline")
@NamedQueries(@NamedQuery(name = "RateLine.findAllForRateFile", query = "SELECT r FROM RateLine r WHERE r.rateFile.id = :ratefileid"))
public class RateLine extends BaseEntity implements Comparable<RateLine> {

	private static final long serialVersionUID = 1L;

	private int measurement;

	private String zone;

	private double value;

	@ManyToOne
	@JoinColumn(name = "rateFileId")
	private RateFile rateFile;

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public RateFile getRateFile() {
		return rateFile;
	}

	public void setRateFile(RateFile rateFile) {
		this.rateFile = rateFile;
	}

	public int getMeasurement() {
		return measurement;
	}

	public void setMeasurement(int measurement) {
		this.measurement = measurement;
	}

	@Override
	public int compareTo(RateLine o) {
		if (o.measurement == this.measurement)
			return 0;
		if (o.measurement > this.measurement)
			return 1;
		return -1;
	}

	@Override
	public String toString() {
		return this.getId() + " - " + getZone() + " - " + getMeasurement()
				+ " - " + getValue();
	}

	public RateLine deepCopy() {
		RateLine rl = new RateLine();
		rl.setMeasurement(getMeasurement());
		rl.setZone(getZone());
		rl.setValue(getValue());
		return rl;
	}

}
