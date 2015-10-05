package com.moorkensam.xlra.model.rate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;

@Entity
@Cacheable
@Table(name = "ratefile")
@NamedQueries({
		@NamedQuery(name = "RateFile.findAll", query = "SELECT r FROM RateFile r WHERE r.deleted = false"),
		@NamedQuery(name = "RateFile.findDistinctZones", query = "SELECT distinct rs.zone FROM RateFile r join r.rateLines rs WHERE r.id = :ratefileid ORDER BY rs.zone"),
		@NamedQuery(name = "RateFile.findDistinctMeasurements", query = "SELECT distinct rs.measurement FROM RateFile r join r.rateLines rs WHERE r.id = :ratefileid ORDER BY rs.measurement") })
public class RateFile extends BaseEntity {

	private static final long serialVersionUID = 830015468011487605L;

	public RateFile() {
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL)
	private List<Condition> conditions;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL)
	private List<Zone> zones;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL)
	private List<RateLine> rateLines = new ArrayList<RateLine>();

	@Enumerated(EnumType.STRING)
	private Language language;

	@Transient
	private List<String> columns;

	@Transient
	private List<Double> measurementRows;

	@Transient
	private List<List<RateLine>> relationalRateLines;

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

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<Double> getMeasurementRows() {
		return measurementRows;
	}

	public void setMeasurementRows(List<Double> measurementRows) {
		this.measurementRows = measurementRows;
	}

	public List<List<RateLine>> getRelationalRateLines() {
		return relationalRateLines;
	}

	public void setRelationalRateLines(List<List<RateLine>> relationalRateLines) {
		this.relationalRateLines = relationalRateLines;
	}

	public RateLine getRateLineById(long id) {
		for (RateLine rl : getRateLines()) {
			if (rl.getId() == id) {
				return rl;
			}
		}
		return null;
	}

	public RateFile deepCopy() {
		RateFile rf = new RateFile();
		rf.setName(getName());
		rf.setColumns(getColumns());
		rf.setCountry(getCountry());
		rf.setCustomer(getCustomer());
		rf.setKindOfRate(getKindOfRate());
		rf.setMeasurement(getMeasurement());
		rf.setMeasurementRows(getMeasurementRows());
		rf.setRateLines(new ArrayList<RateLine>());
		for (RateLine rl : getRateLines()) {
			RateLine deepCopy = rl.deepCopy();
			deepCopy.setRateFile(rf);
			rf.getRateLines().add(deepCopy);
		}
		List<List<RateLine>> relationRatelines = new ArrayList<List<RateLine>>();
		for (List<RateLine> rateList : getRelationalRateLines()) {
			List<RateLine> rls = new ArrayList<RateLine>();
			for (RateLine rl : rateList) {
				rls.add(rl);
			}
			relationRatelines.add(rls);
		}

		rf.setConditions(new ArrayList<Condition>());
		for (Condition c : conditions) {
			Condition copy = c.deepCopy();
			copy.setRateFile(rf);
			rf.getConditions().add(copy);
		}

		rf.setRelationalRateLines(relationRatelines);
		return rf;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

}
