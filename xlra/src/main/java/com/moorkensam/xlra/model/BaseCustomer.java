package com.moorkensam.xlra.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Cacheable
@Table(name = "customer")
@NamedQueries({ @NamedQuery(name = "BaseCustomer.findAll", query = "SELECT b FROM BaseCustomer b where b.deleted = false") })
public class BaseCustomer extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Length(max = 100)
	@NotNull
	@NotEmpty(message = "Name may not be empty")
	protected String name;

	@Length(max = 100)
	@NotNull
	@NotEmpty(message = "Email may not be empty")
	protected String email;

	@Length(max = 100)
	@NotNull
	@NotEmpty(message = "Phone may not be empty")
	protected String phone;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "A language has to be selected")
	protected Language language;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

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

}
