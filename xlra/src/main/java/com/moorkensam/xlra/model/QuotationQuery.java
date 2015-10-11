package com.moorkensam.xlra.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;

@Entity
@Table(name = "quotationquery")
@NamedQueries({ @NamedQuery(name = "QuotationQuery.findAll", query = "SELECT q FROM QuotationQuery q where q.deleted = false") })
public class QuotationQuery extends BaseEntity {

	private static final long serialVersionUID = -555523071215522763L;

	private BaseCustomer customer;

	@Enumerated(EnumType.STRING)
	private Kind kindOfRate;

	@Enumerated(EnumType.STRING)
	private Measurement measurement;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private Country country;

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
		return "Country: " + country.getName() + " Type: " + kindOfRate
				+ " Measurement: " + measurement + " Customer: "
				+ customer.getName();
	}
}
