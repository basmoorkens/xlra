package com.moorkensam.xlra.model;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Cacheable
@NamedQueries({ @NamedQuery(name = "FullCustomer.findAll", query = "SELECT c FROM FullCustomer c where c.deleted = false") })
public class FullCustomer extends BaseCustomer {

	public FullCustomer() {
		super();
	}

	public FullCustomer(boolean fullCustomer) {
		address = new Address();
		setFullCustomer(fullCustomer);
	}

	private static final long serialVersionUID = 1L;

	@Embedded
	private Address address;

	@Length(max = 100)
	@NotNull
	@NotEmpty(message = "May not be empty")
	private String btwNumber;

	private boolean fullCustomer;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBtwNumber() {
		return btwNumber;
	}

	public void setBtwNumber(String btwNumber) {
		this.btwNumber = btwNumber;
	}

	public boolean isFullCustomer() {
		return fullCustomer;
	}

	public void setFullCustomer(boolean fullCustomer) {
		this.fullCustomer = fullCustomer;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
