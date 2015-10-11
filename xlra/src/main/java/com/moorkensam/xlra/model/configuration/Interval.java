package com.moorkensam.xlra.model.configuration;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Interval implements Serializable {

	private static final long serialVersionUID = -5504002933410190586L;

	private double start, end;

	public Interval() {

	}

	public Interval(String start, String end) {
		this.start = Double.parseDouble(start);
		this.end = Double.parseDouble(end);
	}

	public Interval(String[] ints) {
		this.start = Double.parseDouble(ints[0]);
		this.end = Double.parseDouble(ints[1]);
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return start + " - " + end;
	}

	public String toIntString() {
		return (int) start + " - " + (int) end;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Interval)) {
			return false;
		}
		Interval otherInterval = (Interval) obj;
		if (this.getStart() == otherInterval.getStart()
				&& this.getEnd() == otherInterval.getEnd()) {
			return true;
		}
		return false;
	}
}
