package com.moorkensam.xlra.model.rate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.FullCustomer;

@Entity
@Table(name = "ratefile")
@NamedQueries(@NamedQuery(name = "RateFile.findAll", query = "SELECT r FROM RateFile r WHERE r.deleted = false"))
public class RateFile extends BaseEntity {

	private static final long serialVersionUID = 830015468011487605L;

	public RateFile() {
		condition = new Condition();
	}

	@OneToOne
	@JoinColumn(name = "customerId")
	private FullCustomer customer;

	@Enumerated(EnumType.STRING)
	private Kind kindOfRate;

	@Enumerated(EnumType.STRING)
	private Measurement measurement;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private Country country;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile")
	private List<RateLine> rateLines = new ArrayList<RateLine>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "conditionId")
	private Condition condition;

	private String name;

	public FullCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(FullCustomer customer) {
		this.customer = customer;
	}

	public Kind getKindOfRate() {
		return kindOfRate;
	}

	public void setKindOfRate(Kind kindOfRate) {
		this.kindOfRate = kindOfRate;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RateLine> getRateLines() {
		return rateLines;
	}

	public void setRateLines(List<RateLine> rateLines) {
		this.rateLines = rateLines;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
