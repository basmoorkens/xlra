package com.moorkensam.xlra.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "customer")
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

}
