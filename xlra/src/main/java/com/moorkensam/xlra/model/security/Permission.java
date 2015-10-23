package com.moorkensam.xlra.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

	private static final long serialVersionUID = 5223990771708418257L;

	@Column(name = "permission_name")
	private String name;

	@Column(name = "permission_description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
