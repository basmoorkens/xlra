package com.moorkensam.xlra.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="customer")
public class Customer extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Length(max=100)
	@NotNull
	private String name;
	
	@Length(max=100)
	@NotNull
	private String email;

	@Embedded
	private Address address; 
	
	@Length(max=100)
	@NotNull
	private String phone; 
	
	//TODO btw number length
	private String btwNumber; 
	
	@Enumerated(EnumType.STRING)
	private Language language; 
	
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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isFullCustomer() {
		return fullCustomer;
	}

	public void setFullCustomer(boolean fullCustomer) {
		this.fullCustomer = fullCustomer;
	}
}
