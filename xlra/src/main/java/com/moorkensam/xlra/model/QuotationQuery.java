package com.moorkensam.xlra.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.TransportType;

@Entity
@Cacheable
@Table(name = "quotationquery")
@NamedQueries({ @NamedQuery(name = "QuotationQuery.findAll", query = "SELECT q FROM QuotationQuery q where q.deleted = false") })
public class QuotationQuery extends BaseEntity {

	private static final long serialVersionUID = -555523071215522763L;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private BaseCustomer customer;

	@Enumerated(EnumType.STRING)
	private Kind kindOfRate;

	@Enumerated(EnumType.STRING)
	private Measurement measurement;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private Country country;

	private double quantity;

	private boolean adrSurcharge;

	private boolean importFormality;

	private boolean exportFormality;

	@Enumerated(EnumType.STRING)
	private TransportType transportType;

	private String postalCode;

	public QuotationQuery() {
	}

	public BaseCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(BaseCustomer customer) {
		this.customer = customer;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Kind getKindOfRate() {
		return kindOfRate;
	}

	public void setKindOfRate(Kind kindOfRate) {
		this.kindOfRate = kindOfRate;
	}

	@Override
	public String toString() {
		return "Country: " + country.getShortName() + " Type: " + kindOfRate
				+ " Measurement: " + measurement + " Customer: "
				+ customer.getName();
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public boolean isImportFormality() {
		return importFormality;
	}

	public void setImportFormality(boolean importFormality) {
		this.importFormality = importFormality;
	}

	public boolean isAdrSurcharge() {
		return adrSurcharge;
	}

	public void setAdrSurcharge(boolean adrSurcharge) {
		this.adrSurcharge = adrSurcharge;
	}

	public boolean isExportFormality() {
		return exportFormality;
	}

	public void setExportFormality(boolean exportFormality) {
		this.exportFormality = exportFormality;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}
}
