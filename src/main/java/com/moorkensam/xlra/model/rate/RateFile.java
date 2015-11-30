package com.moorkensam.xlra.model.rate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

import org.hibernate.annotations.BatchSize;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.service.util.CalcUtil;

@Entity
@Cacheable
@Table(name = "ratefile")
@NamedQueries({
		@NamedQuery(name = "RateFile.findAll", query = "SELECT r FROM RateFile r WHERE r.deleted = false"),
		@NamedQuery(name = "RateFile.findDistinctMeasurements", query = "SELECT distinct rs.measurement FROM RateFile r join r.rateLines rs WHERE r.id = :ratefileid ORDER BY rs.measurement") })
public class RateFile extends BaseEntity {

	private static final long serialVersionUID = 830015468011487605L;

	public RateFile() {
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 5)
	private List<Condition> conditions;

	@OneToOne
	@JoinColumn(name = "customerId")
	private Customer customer;

	@Enumerated(EnumType.STRING)
	private Kind kindOfRate;

	@Enumerated(EnumType.STRING)
	private Measurement measurement;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private Country country;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 2)
	private List<Zone> zones;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rateFile", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 10)
	private List<RateLine> rateLines = new ArrayList<RateLine>();

	@Enumerated(EnumType.STRING)
	private TransportType transportType;

	@Transient
	private List<String> columns;

	@Transient
	private List<Double> measurementRows;

	@Transient
	private List<List<RateLine>> relationalRateLines;

	public void addZone(Zone zone) {
		if (zones == null) {
			zones = new ArrayList<Zone>();
		}
		zones.add(zone);
	}

	private String name;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
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
		rf.setCountry(getCountry());
		rf.setKindOfRate(getKindOfRate());
		rf.setMeasurement(getMeasurement());
		rf.setRateLines(new ArrayList<RateLine>());
		if (zones != null) {
			for (Zone z : zones) {
				Zone deepCopy = z.deepCopy();
				deepCopy.setRateFile(rf);
				rf.addZone(deepCopy);
			}
		} else {
			rf.setZones(new ArrayList<Zone>());
		}

		if (rateLines != null) {
			for (RateLine rl : getRateLines()) {
				RateLine deepCopy = rl.deepCopy();
				deepCopy.setRateFile(rf);
				Zone copiedZone = rf.getZoneForZoneName(deepCopy.getZone()
						.getName());
				deepCopy.setZone(copiedZone);

				rf.getRateLines().add(deepCopy);
			}
		} else {
			rf.setRateLines(new ArrayList<RateLine>());
		}

		rf.setConditions(new ArrayList<Condition>());
		if (conditions != null) {
			for (Condition c : conditions) {
				Condition copy = c.deepCopy();
				copy.setRateFile(rf);
				rf.getConditions().add(copy);
			}
		}

		rf.fillUpRelationalProperties();
		rf.fillUpRateLineRelationalMap();
		return rf;
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

	public boolean isNumericalZoneRateFile() {
		if (getZones() != null && !getZones().isEmpty()) {
			return getZones().get(0).getZoneType() == ZoneType.NUMERIC_CODES;
		}
		return false;
	}

	public boolean isAlphaNumericalZoneRateFile() {
		if (getZones() != null && !getZones().isEmpty()) {
			return getZones().get(0).getZoneType() == ZoneType.ALPHANUMERIC_LIST;
		}
		return false;
	}

	public RateLine getRateLineForQuantityAndPostalCode(double quantity,
			String postalCode) throws RateFileException {
		CalcUtil calcUtil = CalcUtil.getInstance();
		BigDecimal quantityBd = new BigDecimal(quantity);
		quantityBd = calcUtil.roundBigDecimal(quantityBd);
		if (rateLines != null) {
			List<RateLine> rlsWithCorrectQuantity = new ArrayList<RateLine>();
			for (RateLine rl : getRateLines()) {
				BigDecimal roundBigDecimal = calcUtil
						.roundBigDecimal(new BigDecimal(rl.getMeasurement()));
				if (roundBigDecimal.doubleValue() == quantityBd.doubleValue()) {
					rlsWithCorrectQuantity.add(rl);
				}
			}
			if (!rlsWithCorrectQuantity.isEmpty()) {
				for (RateLine rl : rlsWithCorrectQuantity) {
					if (rl.getZone().getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
						for (String s : rl.getZone()
								.getAlphaNumericalPostalCodes()) {
							if (s.equals(postalCode)) {
								return rl;
							}
						}
					} else {
						for (Interval interval : rl.getZone()
								.getNumericalPostalCodes()) {
							int postalCodeAsInt = Integer.parseInt(postalCode);
							if (postalCodeAsInt >= interval.getStart()
									&& postalCodeAsInt < interval.getEnd()) {
								return rl;
							}

						}
					}
				}

			}
		}
		throw new RateFileException(
				"Could not find price for given input parameters. Quantity: "
						+ quantity + " Postal code: " + postalCode);
	}

	public Zone getZoneForZoneName(String zoneName) {
		if (zones != null && !zones.isEmpty()) {
			for (Zone z : zones) {
				if (z.getName().equals(zoneName)) {
					return z;
				}
			}
		}
		return null;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}

	/**
	 * This method fetches the details for the ratelines of the ratefile. The
	 * details fetched are the columns and measurements.
	 * 
	 * @param rateFile
	 */
	public void fillUpRelationalProperties() {
		setColumns(new ArrayList<String>());
		for (Zone z : getZones()) {
			getColumns().add(z.getAsColumnHeader());
		}
		List<Double> measurementRows = new ArrayList<Double>();
		for (RateLine rl : getRateLines()) {
			if (!measurementRows.contains(rl.getMeasurement())) {
				measurementRows.add(rl.getMeasurement());
			}
		}
		Collections.sort(measurementRows);
		setMeasurementRows(measurementRows);

	}

	/**
	 * Fills up the transient attribute relationRatelines of the ratefile
	 * object. In order for this to work the columns and measurements have to be
	 * set on the ratefile object.
	 * 
	 * @param rateFile
	 */
	public void fillUpRateLineRelationalMap() {
		List<List<RateLine>> relationRateLines = new ArrayList<List<RateLine>>();
		for (Double i : getMeasurementRows()) {
			List<RateLine> rateLines = new ArrayList<RateLine>();
			for (RateLine rl : getRateLines()) {
				if (rl.getMeasurement() == (i)) {
					rateLines.add(rl);
				}
			}
			Collections.sort(rateLines);
			relationRateLines.add(rateLines);
		}
		setRelationalRateLines(relationRateLines);
	}

	public void addCondition(Condition condition) {
		if (conditions == null) {
			conditions = new ArrayList<Condition>();
		}
		conditions.add(condition);
		condition.setRateFile(this);
	}
}
