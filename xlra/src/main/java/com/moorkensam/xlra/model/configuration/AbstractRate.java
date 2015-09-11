package com.moorkensam.xlra.model.configuration;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import com.moorkensam.xlra.model.BaseEntity;

@MappedSuperclass
public abstract class AbstractRate extends BaseEntity {

	private static final long serialVersionUID = -2217292746928883653L;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "start", column = @Column(name = "start")),
			@AttributeOverride(name = "end", column = @Column(name = "end")) })
	private Interval interval;

	private double multiplier;

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractRate)) {
			return false;
		}
		AbstractRate otherRate = (AbstractRate) obj;
		if (this.getInterval().equals(otherRate.getInterval())
				&& this.getMultiplier() == otherRate.getMultiplier()) {
			return true;
		}
		return false;
	}

}
