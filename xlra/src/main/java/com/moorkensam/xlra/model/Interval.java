package com.moorkensam.xlra.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Interval implements Serializable {

	private static final long serialVersionUID = -5504002933410190586L;

	private double start, end;

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
	
}
